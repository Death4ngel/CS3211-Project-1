import java.util.ArrayList;
import java.util.List;

public class Cloud implements Runnable {
 private final int cloudId;
 private boolean isAuthenticated;
 private int balance;
 private Atm atm;
 private boolean receivedAtm;
 private boolean receivedDb;
 private boolean accountId;
 private Action action;
 private Command command;
 private static List<Cloud> clouds = new ArrayList<Cloud>();
 
 private static Database database = new Database();
 {
  database.initWithTestData();
  new Thread(database).start();
 }

 private Cloud(int cloudId, Atm atm) {
  this.cloudId = cloudId;
  this.atm = atm;
 }

 public static Cloud allocateCloud(Atm atm) {
  Cloud cloud = new Cloud(clouds.size(), atm);
  clouds.add(cloud);
  new Thread(cloud).start();
  return cloud;
 }

 public static Cloud getCloud(int cloudId) {
  return clouds.get(cloudId);
 }

 public Action getAction() {
  return action;
 }
 
 public boolean getAuthStatus() {
   return this.isAuthenticated;
 }
 
 public int getBalance() {
   return this.balance;
 }
 
 public int getCloudId() {
   return this.cloudId;
 }

 @Override
 public void run() {
  while (true) {
   try {
    synchronized (this) {
     while (!receivedAtm) {
      wait(Channel.timeout); // for atm
     }
     receivedAtm = false;
     Action action = atm.getAction();
     int accountId = action.getSrcAccountId();
     database.query(new Query(cloudId, Command.retrieve,
       accountId));
     while (!receivedDb) {
      wait(Channel.timeout); // for database
     }
     receivedDb = false;
     BankAccount bankAccount = database.retrieveResult(cloudId);
     switch (action.getCommand()) {
     case withdraw:
      balance = bankAccount.getBalance() - action.getAmount();
      break;
     case deposit:
      balance = bankAccount.getBalance() + action.getAmount();
      break;
     case authenticate:
      this.isAuthenticated = bankAccount.getPassword() == action.getPassword();
      break;
     case transfer:
      balance = bankAccount.getBalance() - action.getAmount();
      database.query(new Query(cloudId, Command.retrieve, action.getDestAccountId()));
      while (!receivedDb) {
       wait(); // for database
      }
      BankAccount destBankAccount = database.retrieveResult(cloudId);
      int destBalance = bankAccount.getBalance() + action.getAmount();
      destBankAccount = new BankAccount(bankAccount.getAccountId(), bankAccount.getPassword(), destBalance);
      database.query(new Query(cloudId, Command.update, destBankAccount.getAccountId(), destBankAccount));
      break;
      default:
       System.err.println("Command not valid");
     }
     bankAccount = new BankAccount(
       bankAccount.getAccountId(),
       bankAccount.getPassword(), balance);
     database.query(new Query(cloudId, Command.update, bankAccount.getAccountId(), bankAccount));
    }
    synchronized (this) {
     while (!receivedDb) {
      wait(Channel.timeout); // for database
     }
    }    
    synchronized (atm) {
     atm.notify();
    }
   } catch (InterruptedException e) {

   }
  }
 }

}
