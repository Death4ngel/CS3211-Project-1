import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Clouds {
	private final List<Cloud> clouds;
	private final Queue<Cloud> freeClouds;
	private final Queue<Atm> waitAtm;

	public Clouds(int N, Database database) {
		this.clouds = new ArrayList<Cloud>(N);
		this.freeClouds = new LinkedList<Cloud>();
		this.waitAtm = new LinkedList<Atm>();
		for (int i = 0; i < N; i++) {
			Cloud cloud = new Cloud(i);
			Channel channel = new Channel(cloud, database);
			cloud.connectToDatabase(channel);
			clouds.add(i, cloud);
			freeClouds.add(cloud);
			new Thread(cloud).start();
		}
	}

	public synchronized int allocateCloud(final Atm reqAtm) {
		Cloud cloud;
		this.waitAtm.add(reqAtm);
		while ((cloud = freeClouds.poll()) == null) {
			try {
				reqAtm.println("no clouds available. waiting . . .");
				this.wait(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		final Atm atm = waitAtm.poll();
		Channel channel = new Channel(atm, cloud);
		atm.connectToCloud(channel);
		cloud.connectToAtm(channel);
		int cloudId = cloud.getCloudId();
		atm.println("obtained Cloud: " + cloudId);
		return cloudId;
	}

	public synchronized void deallocateCloud(int cloudId) {
		System.out.println("Cloud: " + cloudId + " deallocated");
		Cloud cloud = clouds.get(cloudId);
		cloud.disconnectToAtm();
		freeClouds.offer(cloud);
	}

	public Cloud getCloud(int cloudId) {
		return clouds.get(cloudId);
	}
}
