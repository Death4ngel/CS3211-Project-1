import java.util.LinkedList;
import java.util.Queue;

public class Database implements Runnable {
	public Queue<Cloud> queue = new LinkedList<Cloud>();

	public synchronized void doSomething(Cloud cloud) {
		queue.add(cloud);
		notify(); // run
	}

	@Override
	public void run() {
		while (true) {
			try {
				Cloud cloud = null;
				synchronized (this) {
					wait(); // for something to do
					cloud = queue.poll();
					System.out.println("Retrieve");
				}
				synchronized (cloud) {
					cloud.notify();
				}
				synchronized (this) {
					wait(); // wait for cloud
				}
				System.out.println("Update");
				synchronized (cloud) {
					cloud.notify();
				}
			} catch (InterruptedException e) {

			}
		}
	}

}
