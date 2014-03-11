
public class DatabaseWrong extends Database implements Runnable {
	// In this wrong version of database, 
	// updates are not done atomically, hence calculations may interleave,
	// and result in wrong values.

	public DatabaseWrong() {
		super();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					while (queries.isEmpty()) {
						this.println("Waiting for query");
						this.wait(1000); // for something to do
					}
					this.println("Query received");
					ChannelQueryPair<Channel, Query> channelQueryPair = queries.poll();
					Query query = channelQueryPair.getQuery();
					Channel channelToReply = channelQueryPair.getChannel();
					int accountId = query.getAccountId();
					BankAccount bankAccount = bankAccounts.get(accountId);
					/*if (isLocked.contains(accountId) && query.getCommand() != Command.update) {
						queries.offer(channelQueryPair);
						wait(1000);
						continue;
					}*/
					switch (query.getCommand()) {
					case authenticate:
						this.println("sending password to query..");
						channelToReply.send(bankAccount.getPassword(), this);
					case retrieve:
						this.println("sending balance to query..");
						//isLocked.add(accountId);
						channelToReply.send(bankAccount.getBalance(), this);
						break;
					case update:
						this.println("updated bankAccount");
						bankAccounts.put(accountId, new BankAccount(accountId, bankAccount.getPassword(), query.getArg()));
						//isLocked.remove(accountId);
						break;
					default:
						System.err.println("Command not valid");
					}
					this.println("query done");
				}
			} catch (InterruptedException e) {

			}
		}
	}

}
