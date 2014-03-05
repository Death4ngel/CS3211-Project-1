
public class Query {
	private int cloudId;
	private Command command;
	private int accountId;
	private BankAccount bankAccount;
	
	public Query(int cloudId, Command command, int accountId) {
		this.cloudId = cloudId;
		this.command = command;
		this.accountId = accountId;
	}
	
	public Query(int cloudId, Command command, int accountId, BankAccount bankAccount) {
		this(cloudId, command, accountId);
		this.bankAccount = bankAccount;
	}
	
	public int getCloudId() {
		return cloudId;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public int getAccountId() {
		return accountId;
	}
	
	public BankAccount getBankAccount() {
		return bankAccount;
	}
}
