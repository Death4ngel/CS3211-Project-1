
public class IncorrectCalc {
	/*
	 * Here we show that if updates are not done atomically,
	 * There will be errors in the bank balance.
	 * 
	 * We create a bank account with 1000 initially,
	 * and have 2 atms accessing it concurrently.
	 * atm[1] withdrew on the old value of 1000,
	 * before atm[0] updated the value
	 * Thus, our final balance is 900.
	 * 
	 * Output
	 * Atm[0]: deposit completed
	 * Atm[0]: Your balance is now: 1100
	 * Atm[1]: withdraw completed
	 * Atm[1]: Your balance is now: 900 
	 */
	public static void main(String[] args) {
		final Database database = new DatabaseWrong();
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
