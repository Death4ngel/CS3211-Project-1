public class Cloud implements Runnable {
	private Atm atm;
	private static Database database = new Database();
	{
		new Thread(database).start();
	}

	private Cloud(Atm atm) {
		this.atm = atm;
	}

	public static Cloud allocateCloud(Atm atm) {
		return new Cloud(atm);
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					wait(); // for atm
					database.doSomething(this);
					wait(); // for database
					System.out.println("Processing");
				}
				synchronized (database) {
					database.notify();
				}
				synchronized (this) {
					wait(); // for database
				}
				synchronized (atm) {
					atm.notify();
				}
			} catch (InterruptedException e) {

			}
		}
	}

}
