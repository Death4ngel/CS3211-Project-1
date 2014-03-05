import java.util.Scanner;

public class Main {

 static int pin_number = 0; 
 static int accountID = 0;
 static int trans = 0;
 private final int N = 4;

 public void run() {
  /*
   * for (int i = 0; i < N; i++) { new Thread(new Atm()).start(); new
   * Thread(new Cloud()).start(); } new Thread(new Database()).start();
   */
  /*Atm atm = new Atm();
  new Thread(atm).start();*/
 }

 public static void main(String[] args) {
  Scanner input = new Scanner(System.in);
  Main system = new Main();
  //system.run();
  Atm atm = new Atm();
  new Thread(atm).start();
  System.out.println("Please insert your card: ");
  accountID = input.nextInt();
  System.out.println("Please enter your PIN number: ");
  pin_number = input.nextInt();
  //Check authentication
  //if authPass
  do {
  System.out.println("Please select your transaction: ");
  System.out.println("[1]Withdrawal [2]Deposit [3]Transfer [4] Run Tests");
  trans = input.nextInt();
  if (trans == 1) {
   atm.doAction(new Action(accountID, Command.withdraw));
  }
  //System.out.println("Please enter the amount of withdrawal: ");
  //if trans == 2, execute deposit
  //System.out.println("Please enter the amount of deposit: ");
  //if trans == 3, execute transfer
  //System.out.println("Please enter the bank account number: ");
  //System.out.println("Please enter the amount of transfer: ");
  //System.out.println("Would you like to make another transaction? Type 'yes' if you want to continue, otherwise type 'exit'");
  }while(!input.equals("exit"));
 }
}
