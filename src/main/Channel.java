public class Channel {
	public static final int timeout = 10000;
	private final int P = 0.5;
	private final rand = new Random();

	public static void send(Process process) {
		int r = rand.nextInt(1);
		if (r <= P) {
			process.received = true;
			process.notify();
		}
	}
}