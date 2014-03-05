import java.util.Scanner;

public class Main {

	int pin_number = 0; 
	int accountID = 0;
	int trans = 0;
	private final int N = 4;

	public void run() {
		/*
		 * for (int i = 0; i < N; i++) { new Thread(new Atm()).start(); new
		 * Thread(new Cloud()).start(); } new Thread(new Database()).start();
		 */
		Atm atm = new Atm();
		new Thread(atm).start();
		try {
			Thread.sleep(1000); // wait for Atm to reach wait first
			synchronized (atm) {
				atm.notify();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Main system = new Main();
		system.run();
		System.out.println("Please insert your card: ");
		accountID = input.nextInt();
		System.out.println("Please enter your PIN number: ");
		pin_number = input.nextInt();
		//Check authentication
		//if authPass
		do {
		System.out.println("Please select your transaction: ");
		System.out.println("[1]Withdrawal [2]Deposit [3]Transfer");
		trans = input.nextInt();
		//if trans == 1, execute withdrawal
		//System.out.println("Please enter the amount of withdrawal: ");
		//if trans == 2, execute deposit
		//System.out.println("Please enter the amount of deposit: ");
		//if trans == 3, execute transfer
		//System.out.println("Please enter the bank account number: ");
		//System.out.println("Please enter the amount of transfer: ");
		//System.out.println("Would you like to make another transaction? Type 'yes' if you want to continue, otherwise type 'exit'");
		}while(!input.equals("exit");
	}
}
