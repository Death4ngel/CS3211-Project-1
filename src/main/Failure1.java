public class Failure1 {
	/*
	 * In this failure, we exhibit a starvation problem.
	 * Here, we have 2 atms but only 1 cloud.
	 * If there is no proper fairness system implemented,
	 * it is possible for atm[0] to repeatedly allocate and deallocate the cloud
	 * before atm[1] ever gets a chance to allocate cloud!
	 * Hence, atm[1] will be starved.
	 * 
	 * Output
	 * Atm[0]: Waiting for an action
	 * Atm[1]: Waiting for an action
	 * Atm[0]: obtained Cloud: 0
	 * Atm[1]: no clouds available. waiting . . .
	 * Atm[1]: no clouds available. waiting . . .
	 * Atm[1]: no clouds available. waiting . . .
	 * Cloud: 0 deallocated
	 * Atm[0]: obtained Cloud: 0
	 * Atm[1]: no clouds available. waiting . . .
	 * Atm[1]: no clouds available. waiting . . .
	 * Cloud: 0 deallocated
	 * Atm[0]: obtained Cloud: 0
	 * Atm[1]: no clouds available. waiting . . .
	 * Atm[1]: no clouds available. waiting . . .
	 * Cloud: 0 deallocated
	 * Atm[0]: obtained Cloud: 0
	 */
	
	public static void main(String[] args) {
		final Database database = new Database();
		final Clouds clouds = new CloudsWrong(1, database);
		final Atms atms = new Atms(2, clouds);
		new Thread(database).start();

		final Atm atm = atms.getAtm(0);
		final Atm atm2 = atms.getAtm(1);
		clouds.allocateCloud(atm);		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {					
					while (true) {
						Thread.sleep(2000);
						clouds.deallocateCloud(0);
						clouds.allocateCloud(atm);
					}
				} catch (Exception e) {

				}
			}
		}).start();
		clouds.allocateCloud(atm2);
	}
}
