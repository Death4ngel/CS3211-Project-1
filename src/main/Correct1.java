public class Correct1 {
	/*
	 * This is the correct version to Failure1.java
	 * Fairness is implemented, and no atms are starved in the process.
	 * 
	 * Here atm[0] obtained cloud 0, deallocates it,
	 * and atm[1] manages to allocate it subsequently. 
	 * 
	 * Output
	 * Atm[0]: obtained Cloud: 0
	 * Atm[1]: no clouds available. waiting . . .
	 * Atm[0]: waiting for an action
	 * Atm[1]: waiting for an action
	 * Atm[1]: no clouds available. waiting . . .
	 * Atm[1]: no clouds available. waiting . . .
	 * Cloud: 0 deallocated
	 * Atm[1]: obtained Cloud: 0
	 */
	
	public static void main(String[] args) {
		final Database database = new Database();
		final Clouds clouds = new Clouds(1, database);
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
