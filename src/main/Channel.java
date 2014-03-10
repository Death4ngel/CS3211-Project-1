import java.util.Random;

public class Channel {
	public static final int timeout = 1000;
	// probability that a packet is dropped
	private static final double probFail = 0.10;
	private static final Random rand = new Random();

	private final Process process1;
	private final Process process2;
	private Object object1 = null;
	private Object object2 = null;

	public Channel(Process process1, Process process2) {
		this.process1 = process1;
		this.process2 = process2;
	}
	
	public Object getResponse(Process process) {
		if (process == process1) {
			return this.object2;
		} else {
			return this.object1;
		}
	}

	public synchronized void send(Object data, Process process) {
		double p = rand.nextDouble();
		if (p < probFail) {
			process.println("dropped a packet");
		} else {
			if (process == process1) {
				this.object1 = data;
				this.object2 = null;
				process2.receive(this);
			} else {
				this.object1 = null;
				this.object2 = data;
				process1.receive(this);
			}
		}
	}
}