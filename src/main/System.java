public class System {

	private final int N = 4;

	public void run() {
		for (int i = 0; i < N; i++) {
			new Thread(new Atm()).start();
			new Thread(new Cloud()).start();
		}
		new Thread(new Database()).start();
	}

	public static void main(String[] args) {
		System system = new System();
		system.run();
	}
}
