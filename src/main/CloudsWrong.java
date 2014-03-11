
public class CloudsWrong extends Clouds {
	// This version of Clouds has no fairness implemented.
	// Thus, it is possible for a atm to starve.
	
	public CloudsWrong(int N, Database database) {
		super(N, database);
	}
	
	@Override
	public synchronized int allocateCloud(final Atm atm) {
		Cloud cloud;
		while ((cloud = freeClouds.poll()) == null) {
			try {
				atm.println("no clouds available. waiting . . .");
				this.wait(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Channel channel = new Channel(atm, cloud);
		atm.connectToCloud(channel);
		cloud.connectToAtm(channel);
		int cloudId = cloud.getCloudId();
		atm.println("obtained Cloud: " + cloudId);
		return cloudId;
	}
}