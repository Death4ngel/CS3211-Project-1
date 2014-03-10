import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Database extends Process implements Runnable {
	private final Queue<ChannelQueryPair<Channel, Query>> queries = new LinkedList<ChannelQueryPair<Channel, Query>>();
	private final Map<Integer, BankAccount> results = new HashMap<Integer, BankAccount>();
	private final Map<Integer, BankAccount> bankAccounts = new HashMap<Integer, BankAccount>();


	// locks bank account to prevent concurrent updatesn
	private final Set<Integer> isLocked = new HashSet<Integer>();

	public Database() {
		this.initWithTestData();
	}

	@Override
	public synchronized void receive(Object request, Channel channel) {
		if (request instanceof Query) {
			Query query = (Query) request;
			queries.offer(new ChannelQueryPair<Channel, Query>(channel, query));
			this.notify();
		}
	}

	private void initWithTestData() {
		for (int i = 10001; i < 10023; i++) {
			bankAccounts.put(i, new BankAccount(i, 2 * i, i));
		}
	}

	public BankAccount retrieveResult(int id) {
		return results.get(id);
	}
	
	private void println(String s) {
		System.out.println("Database: " + s);
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					while (queries.isEmpty()) {
						//this.println("Waiting for query");
						this.wait(1000); // for something to do
					}
					this.println("Query received");
					ChannelQueryPair<Channel, Query> channelQueryPair = queries.poll();
					Query query = channelQueryPair.getQuery();
					Channel channelToReply = channelQueryPair.getChannel();
					int accountId = query.getAccountId();
					BankAccount bankAccount = bankAccounts.get(accountId);
					if (isLocked.contains(accountId) && query.getCommand() != Command.update) {
						queries.offer(channelQueryPair);
						continue;
					}
					switch (query.getCommand()) {
					case authenticate:
						System.out.println("Database sending back reply to query..");
						channelToReply.send(bankAccount.getPassword(), this);
					case retrieve:
						System.out.println("Database sending back reply to query..");
						isLocked.add(accountId);
						channelToReply.send(bankAccount.getBalance(), this);
						break;
					case update:
						System.out.println("Database updated bankAccount");
						bankAccounts.put(accountId, new BankAccount(accountId, bankAccount.getPassword(), query.getArg()));
						isLocked.remove(accountId);
						break;
					default:
						System.err.println("Command not valid");
					}
					this.println("Action done");
				}
			} catch (InterruptedException e) {

			}
		}
	}

}
