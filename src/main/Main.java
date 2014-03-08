public class Main {
	public static void main(String[] args) {
		final int N = 2;
		Database database = new Database();
		Clouds clouds = new Clouds(N, database);
		Atms atms = new Atms(N, clouds);
		new Thread(database).start();

		Atm atm = atms.getAtm(0);
		Atm atm2 = atms.getAtm(1);
		atm.start();
		/*clouds.allocateCloud(atm);
		clouds.allocateCloud(atm2);
		atm.deposit(10001, 1000);
		atm.deposit(10001, 1000);
		atm.deposit(10001, 1000);*/
		//atm2.withdraw(10001, 1000);
	}
}
