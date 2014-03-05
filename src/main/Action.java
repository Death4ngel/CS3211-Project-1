public class Action {
	private Command command;
	private int srcAccountId;
	private int amount;
	private int destAccountId;
	private String password; 
	
	public Action(int srcAccountId, Command command) {
		this.srcAccountId = srcAccountId;
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public int getSrcAccountId() {
		return srcAccountId;
	}

	public int getAmount() {
		return amount;
	}
	
	public int getDestAccountId() {
		return destAccountId;
	}
	
	public String getPassword() {
		return password;
	}
}
