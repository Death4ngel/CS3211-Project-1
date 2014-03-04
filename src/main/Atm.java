public class Atm implements Runnable {
	private Cloud cloud;

	@Override
	public synchronized void run() {
		cloud = Cloud.allocateCloud(this);
		while (true) {
			try {
				wait(); // for an action
				cloud.notify();
				wait(); // for cloud
				System.out.println("Completed transanction");
			} catch (InterruptedException e) {

			}
		}
	}

}
