public class BankAccount {
	private int accountId;
	private String password;
	private int balance;

	public BankAccount(int accountId, String password, int balance) {
		this.accountId = accountId;
		this.password = password;
		this.balance = balance;
	}
	
	public int getAccountId() {
		return accountId;
	}
	
	public String getPassword() {
		return password;
	}

	public int getBalance() {
		return balance;
	}
	
}