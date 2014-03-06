import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Database implements Runnable {
 private Queue<Query> queries = new LinkedList<Query>();
 private Map<Integer, BankAccount> results = new HashMap<Integer, BankAccount>();
 private Map<Integer, BankAccount> bankAccounts = new HashMap<Integer, BankAccount>();
 
 public void initWithTestData() {
   for(int i=10001; i<10023; i++) {
     bankAccounts.put(i, new BankAccount(i, 2*i, i));
   }
 }

 public synchronized void query(Query query) {
  queries.add(query);
  notify(); // run
 }

 public synchronized BankAccount retrieveResult(int id) {
  return results.get(id);
 }

 @Override
 public void run() {
  while (true) {
   try {
    int cloudId = -1;
    synchronized (this) {
     while (queries.isEmpty()) {
      wait(); // for something to do
     }
     Query query = queries.poll();
     int accountId = query.getAccountId();
     switch (query.getCommand()) {
     case retrieve:
      cloudId = query.getCloudId();
      results.put(cloudId, bankAccounts.get(accountId));
      break;
     case update:
      bankAccounts.put(accountId, query.getBankAccount());
      break;
     default:
      System.err.println("Command not valid");
     }
    }
    Cloud cloud = Cloud.getCloud(cloudId);
    synchronized (cloud) {
     cloud.notify();
    }
   } catch (InterruptedException e) {

   }
  }
 }

}
