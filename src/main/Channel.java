import java.util.Random;

public class Channel {
	public static final int timeout = 10000;
	private static final double P = 0.5;
	private static final Random rand = new Random();

	private final Process process1;
	private final Process process2;

	public Channel(Process process1, Process process2) {
		this.process1 = process1;
		this.process2 = process2;
	}

	void send(Object data, Process process) {
		if (process == process1) {
			process2.receive(data, this);
		} else {
			process1.receive(data, this);
		}
	}
}