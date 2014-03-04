public class Main {

	private final int N = 4;

	public void run() {
		/*
		 * for (int i = 0; i < N; i++) { new Thread(new Atm()).start(); new
		 * Thread(new Cloud()).start(); } new Thread(new Database()).start();
		 */
		Atm atm = new Atm();
		new Thread(atm).start();
		try {
			Thread.sleep(1000); // wait for Atm to reach wait first
			synchronized (atm) {
				atm.notify();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main system = new Main();
		system.run();
	}
}
