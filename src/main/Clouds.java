import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Clouds {
	private final List<Cloud> clouds;
	private final Queue<Cloud> freeClouds;

	public Clouds(int N, Database database) {
		this.clouds = new ArrayList<Cloud>(N);
		this.freeClouds = new LinkedList<Cloud>();
	  for (int i = 0; i < N; i++) {
		  Cloud cloud = new Cloud(i);
		  Channel channel = new Channel(cloud, database);
		  cloud.connectToDatabase(channel);
		  clouds.add(i, cloud);
		  freeClouds.add(cloud);
		  new Thread(cloud).start();
	  }
  }

	public int allocateCloud(Atm atm) {
		Cloud cloud = freeClouds.poll();
		Channel channel = new Channel(atm, cloud);
		atm.connectToCloud(channel);
		cloud.connectToAtm(channel);
		return cloud.getCloudId();
	}

	public void deallocateCloud(int cloudId) {
		freeClouds.offer(clouds.get(cloudId));
	}
	
	public Cloud getCloud(int cloudId) {
		return clouds.get(cloudId);
	}
}
