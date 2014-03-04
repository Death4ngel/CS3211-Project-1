public class Main {

	private final int N = 4;

	public void run() {
		/*for (int i = 0; i < N; i++) {
			new Thread(new Atm()).start();
			new Thread(new Cloud()).start();
		}
		new Thread(new Database()).start();*/
		Atm atm = new Atm();
		new Thread(atm).start();
		atm.run();
		atm.notify();
	}

	public static void main(String[] args) {
		Main system = new Main();
		system.run();
	}
}
