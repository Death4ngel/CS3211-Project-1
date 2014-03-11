
public class CorrectCalc {
	/*
	 * Here we show the correct version of IncorrectCalc.java
	 * We made the updates atomic such that there will be no interleaving.
	 * 
	 * Output
	 * Atm[1]: withdraw completed
	 * Atm[1]: Your balance is now: 900
	 * Atm[0]: deposit completed
	 * Atm[0]: Your balance is now: 1000
	 */
	public static void main(String[] args) {
		final Database database = new Database();
		final Clouds clouds = new Clouds(2, database);
		final Atms atms = new Atms(2, clouds);
		new Thread(database).start();

		final Atm atm = atms.getAtm(0);
		final Atm atm2 = atms.getAtm(1);
		clouds.allocateCloud(atm);
		clouds.allocateCloud(atm2);
		BankAccount bankAccount = new BankAccount(0, 0, 1000);
		database.insert(bankAccount);
		atm.deposit(0, 100);
		atm2.withdraw(0, 100);
	}
}
