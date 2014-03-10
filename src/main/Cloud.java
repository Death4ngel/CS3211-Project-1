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
	
	public void disconnectToAtm() {
		this.atmChannel = null;
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					while (atmChannel == null) {
						// wait to be allocated
						this.wait(1000);
					}
					while(atmChannel.getResponse(this) == null) {
						//this.println("Waiting for an action");
						wait(1000); // for atm
					}
					this.println("received an action");
					Action action = (Action) atmChannel.getResponse(this);
					int accountId = action.getSrcAccountId();
					while (databaseChannel.getResponse(this) == null) {
						databaseChannel.send(new Query(cloudId,Command.retrieve, accountId), this);
						this.wait(Channel.timeout);
					}
					this.println("received a balance");
					int balance = -1;
					int response = (int) databaseChannel.getResponse(this);
					int amount = action.getAmount();
					switch (action.getCommand()) {
					case withdraw:
						balance = response - amount;
						atmChannel.send(balance, this);
						break;
					case deposit:
						balance = response + amount;
						atmChannel.send(balance, this);
						break;
					case authenticate:
						boolean isAuthenticated = response == action.getPassword();
						atmChannel.send(isAuthenticated, this);
						break;
					case transfer:
						balance = response - amount;
						atmChannel.send(balance, this);
						int destAccountId = action.getDestAccountId();
						while (databaseChannel.getResponse(this) == null) {
							databaseChannel.send(new Query(cloudId, Command.retrieve, destAccountId), this);
							this.wait(Channel.timeout);
						}
						int destBalance = (int) databaseChannel.getResponse(this) + amount;
						databaseChannel.send(new Query(cloudId, Command.update, destAccountId, destBalance), this);
						break;
					default:
						System.err.println("Command not valid");
					}
					databaseChannel.send(new Query(cloudId, Command.update, accountId, balance), this);
				}
				this.println("action completed");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}