public class Cloud extends Process implements Runnable {
	private final int cloudId;
	private Channel databaseChannel;
	private Channel atmChannel;

	public Cloud(int cloudId) {
		this.cloudId = cloudId;
	}
	
	public void println(String s) {
		System.out.println("Cloud[" + this.cloudId + "]: " + s);
	}

	public int getCloudId() {
		return this.cloudId;
	}

	public void connectToDatabase(Channel channel) {
		databaseChannel = channel;
	}

	public void connectToAtm(Channel channel) {
		atmChannel = channel;
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					this.response = null;
					while (this.response == null) {
						wait(Channel.timeout); // for atm
					}
					Action action = (Action) this.response;
					int accountId = action.getSrcAccountId();
					this.response = null;
					while (this.response == null) {
						databaseChannel.send(new Query(cloudId,Command.retrieve, accountId), this);
						this.wait(Channel.timeout);
					}
					BankAccount bankAccount = (BankAccount) this.response;
					this.println("Cloud received a response");
					int balance = bankAccount.getBalance();
					switch (action.getCommand()) {
					case withdraw:
						balance = bankAccount.getBalance() - action.getAmount();
						//databaseChannel.send(new Query(cloudId, Command.update, accountId), this);
						atmChannel.send(balance, this);
						break;
					case deposit:
						balance = bankAccount.getBalance() + action.getAmount();
						//databaseChannel.send(new Query(cloudId, Command.update, accountId), this);
						atmChannel.send(balance, this);
						break;
					case authenticate:
						boolean isAuthenticated = bankAccount.getPassword() == action.getPassword();
						databaseChannel.send(isAuthenticated, this);
						atmChannel.send(isAuthenticated, this);
						break;
					case transfer:
						balance = bankAccount.getBalance() - action.getAmount();
						databaseChannel.send(new Query(cloudId, Command.retrieve, action.getDestAccountId()), this);
						BankAccount destBankAccount = (BankAccount) this.response;
						int destBalance = bankAccount.getBalance() + action.getAmount();
						destBankAccount = new BankAccount(bankAccount.getAccountId(), bankAccount.getPassword(), destBalance);
						databaseChannel.send(new Query(cloudId, Command.update, destBankAccount.getAccountId(), destBankAccount), this);
						break;
					default:
						System.err.println("Command not valid");
					}
					bankAccount = new BankAccount(bankAccount.getAccountId(), bankAccount.getPassword(), balance);
					databaseChannel.send(new Query(cloudId, Command.update, bankAccount.getAccountId(), bankAccount), this);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
