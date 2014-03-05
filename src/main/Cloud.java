 public class Cloud implements Runnable {
	private Atm atm;
	private boolean receivedAtm;
	private boolean receivedDb;
	private boolean accountId;
	private Command command;
	private static Database database = new Database();
	{
		new Thread(database).start();
	}

	private Cloud(Atm atm) {
		this.atm = atm;
	}

	public static Cloud allocateCloud(Atm atm) {
		Cloud cloud = new Cloud(atm);
		new Thread(cloud).start();
		return cloud;
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					while (!receivedAtm) {
						wait(Channel.timeout); // for atm
					}
					receivedAtm = false;
					Action action = atm.getAction();
					command = action.getCommand();					
					database.doSomething(this);
					while (!receivedDb) {
						wait(Channel.timeout); // for database
					}
					receivedDb = false;
					int balance = database.getBalance();
					switch (action.getCommand()) {
						withdraw:
							balance -= action.getAmount();
							break;
						deposit:
							balance += action.getAmount();
							break;
						authenticate:
							accountId = atm.getAccountId();
							break;
						transfer:
							accountId = atm.getAccountId();
							break;
					}
					System.out.println("Processing");
				}
				synchronized (database) {
					database.notify();
				}
				synchronized (this) {
					while (!receivedDb) {
						wait(Channel.timeout); // for database
					}
				}
				synchronized (atm) {
					atm.notify();
				}
			} catch (InterruptedException e) {

			}
		}
	}

}
