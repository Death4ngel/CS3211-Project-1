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
					this.println("Cloud received a response");
					int balance = -1;
					switch (action.getCommand()) {
					case withdraw:
						balance = (int) this.response - action.getAmount();
						//databaseChannel.send(new Query(cloudId, Command.update, accountId), this);
						atmChannel.send(balance, this);
						break;
					case deposit:
						int amount = action.getAmount();
						System.out.println(this.response);
						int bla = (int) this.response;
						balance = bla + amount;
						//databaseChannel.send(new Query(cloudId, Command.update, accountId), this);
						atmChannel.send(balance, this);
						break;
					case authenticate:
						boolean isAuthenticated = (int) this.response == action.getPassword();
						//databaseChannel.send(isAuthenticated, this);
						atmChannel.send(isAuthenticated, this);
						break;
					case transfer:
						balance = (int) this.response - action.getAmount();
						this.response = null;
						int destAccountId = action.getDestAccountId();
						while (this.response == null) {
							databaseChannel.send(new Query(cloudId, Command.retrieve, destAccountId), this);
							this.wait(Channel.timeout);
						}
						int destBalance = (int) this.response + action.getAmount();
						databaseChannel.send(new Query(cloudId, Command.update, destAccountId, destBalance), this);
						break;
					default:
						System.err.println("Command not valid");
					}
					databaseChannel.send(new Query(cloudId, Command.update, accountId, balance), this);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}