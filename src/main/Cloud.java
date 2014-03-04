public class Cloud implements Runnable {
	private Atm atm;
	private static Database database = new Database();

	private Cloud(Atm atm) {
		this.atm = atm;
	}

	public static Cloud allocateCloud(Atm atm) {
		return new Cloud(atm);
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {
				wait(); // for atm
				database.doSomething(this);
				wait(); // for database
				System.out.println("Processing");
				database.notify();
				wait(); // for database
				atm.notify();
			} catch (InterruptedException e) {

			}
		}
	}

}
