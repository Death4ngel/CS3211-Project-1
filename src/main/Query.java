
public class Query {
	private final int cloudId;
	private final Command command;
	private final int accountId;
	private int arg;
	
	public Query(int cloudId, Command command, int accountId) {
		this.cloudId = cloudId;
		this.command = command;
		this.accountId = accountId;
	}
	
	public Query(int cloudId, Command command, int accountId, int arg) {
		this(cloudId, command, accountId);
		this.arg = arg;
	}
	
	public int getCloudId() {
		return this.cloudId;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public int getAccountId() {
		return this.accountId;
	}
	
	public int getArg() {
		return this.arg;
	}
}
