public class Atm implements Runnable {
 private Cloud cloud;
 private boolean received;
 private Action action;

 public void doAction(Action action) {
  this.action = action;
  synchronized (this) {
   notify();
  }
 }

 public Action getAction() {
  return action;
 }

 @Override
 public void run() {
  cloud = Cloud.allocateCloud(this);
  while (true) {
   try {
    synchronized (this) {
     while (action != null) {
      wait(); // for an action
     }
     action = null;
    }
    while (!received) {
     synchronized (cloud) {
      cloud.notify();
     }
     synchronized (this) {
      wait(Channel.timeout); // for cloud
     }
    }
    
    System.out.println("Completed transaction");
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
  }
 }
}
