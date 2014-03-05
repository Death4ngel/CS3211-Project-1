public class Atm implements Runnable {

	public enum Command { deposit, withdraw, transfer, authenticate }

	private Cloud cloud;
	private boolean received;
	private Action action;

	public void do(Action action) {
		this.action = action;
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		cloud = Cloud.allocateCloud(this);
		while (true) {
			try {
				synchronized (this) {
					while (!received) {
						wait(Channel.timeout); // for an action
					}
					received = false;
				}				
				synchronized (cloud) {
					cloud.notify();
				}
				synchronized (this) {
					while (!received) {
						wait(Channel.timeout); // for cloud
					}
					received = false;
				}

				System.out.println("Completed transanction");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
