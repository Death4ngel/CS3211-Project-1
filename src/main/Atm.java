public class Atm implements Runnable {
	private Cloud cloud;

	@Override
	public void run() {
		cloud = Cloud.allocateCloud(this);
		new Thread(cloud).start();
		while (true) {
			try {
				synchronized (this) {
					this.wait(); // for an action
				}
				synchronized (cloud) {
					cloud.notify();
				}
				synchronized (this) {
					wait(); // for cloud
				}
				System.out.println("Completed transanction");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
