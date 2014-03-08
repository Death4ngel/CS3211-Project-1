import java.util.Scanner;

public class Atm extends Process implements Runnable {

	private class AtmTerminal {
		private final Atm atm;

		private AtmTerminal(Atm atm) {
			this.atm = atm;
		}

		private void start() {
			try (Scanner input = new Scanner(System.in)) {
				System.out.println("Please insert your card: ");
				int accountId = input.nextInt();
				System.out.println("Please enter your PIN number: ");
				int pinNumber = input.nextInt();
				int cloudId = clouds.allocateCloud(this.atm);
				// Check authentication
				System.out.println("Authenticating.. ");
				atm.authenticate(accountId, pinNumber);
				synchronized (this) {
					this.wait();
				}
				if (isAuthenticated) {// authPass
					String s;
					do {
						System.out.println("Please select your transaction: ");
						System.out.println("[1]Withdrawal [2]Deposit [3]Transfer [4] Run Tests");
						int trans = input.nextInt();
						synchronized (atm) {
							switch (trans) {
							case 1:
								System.out.println("Please enter the amount of withdrawal: ");
								int amount = input.nextInt();
								atm.withdraw(accountId, amount);
								break;
							case 2:
								System.out.println("Please enter the amount of deposit: ");
								amount = input.nextInt();
								atm.deposit(accountId, amount);
								break;
							case 3:
								System.out.println("Please enter the bank account number: ");
								int destAccountId = input.nextInt();
								System.out.println("Please enter the amount of transfer: ");
								amount = input.nextInt();
								atm.transfer(accountId, amount, destAccountId);
								break;
							case 4:
								break;
							default:
								break;
							}
							atm.notify();
						}
						synchronized (this) {
							this.wait();
						}
						System.out.println("Would you like to make another transaction?");
						System.out.println("Type 'yes' if you want to continue, otherwise type 'exit'");
						s = input.nextLine();
					} while (!s.equals("exit"));
				} else {
					System.out.println("Auth failed");
				}
				clouds.deallocateCloud(cloudId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private final int atmId;
	private final Clouds clouds;
	private final AtmTerminal atmTerminal;
	private boolean isAuthenticated = false;
	private Action action;
	private Channel cloudChannel;

	public Atm(int atmId, Clouds clouds) {
		this.atmId = atmId;
		this.clouds = clouds;
		this.atmTerminal = new AtmTerminal(this);
	}
	
	public void println(String s) {
		System.out.println("Atm[" + this.atmId + "]: " + s);
	}

	public void start() {
		this.atmTerminal.start();
	}
	
	public void connectToCloud(Channel channel) {
		this.cloudChannel = channel;
	}
	
	public synchronized void authenticate(int accountId, int pinNumber) {
		this.action = new Action(accountId, Command.authenticate, pinNumber);
		this.notify();
	}
	
	public synchronized void withdraw(int accountId, int amount) {
		this.action = new Action(accountId, Command.withdraw, amount);
		this.notify();
	}
	
	public synchronized void deposit(int accountId, int amount) {
		this.action = new Action(accountId, Command.deposit, amount);
		this.notify();
	}
	
	public synchronized void transfer(int accountId, int amount, int destAccountId) {
		this.action = new Action(accountId, Command.transfer, amount, destAccountId);
		this.notify();
	}

	@Override
	public void run() {
		try {
			while (true) {
				synchronized (this) {
					this.action = null;
					while (this.action == null) {
						this.wait();
					}
					this.send(this.action, cloudChannel);
					// output messages based on choice of action.
					this.println("Cloud notified");
					switch (this.action.getCommand()) {
					case authenticate:
						this.println("Auth status received");
						this.isAuthenticated = (boolean) this.response;
						break;
					default:
						this.println("Transaction completed");
						int balance = (int) this.response;
						this.println("Your balance is now: " + balance);
						break;
					}
				}
				synchronized (atmTerminal) {
					atmTerminal.notify();
				}
			}
		} catch (Exception e) {
		}
	}
}
