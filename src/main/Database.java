import java.util.LinkedList;
import java.util.Queue;

public class Database implements Runnable {
	public Queue<Cloud> queue = new LinkedList<Cloud>();

	public synchronized void doSomething(Cloud cloud) {
		queue.add(cloud);
		notify(); // run
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {
				wait(); // for something to do
				Cloud cloud = queue.poll();
				System.out.println("Retrieve");
				cloud.notify();
				wait(); // wait for cloud
				System.out.println("Update");
			} catch (InterruptedException e) {

			}
		}
	}

}
