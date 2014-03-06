public class Atm implements Runnable {
 private Cloud cloud;
 private boolean received;
 private Action action;

 public void doAction(Action action) {
  this.action = action;
  synchronized (this) {
   this.notify();
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
     while (action == null) {
      wait(); // for an action
     }
    }
    while (!received) {
     synchronized (cloud) {
      cloud.notify(); //notify cloud to get Action
     }
     synchronized (this) {
      wait(Channel.timeout); // for cloud to notify of result
     }
    }
    //output messages based on choice of action.
    switch(this.getAction().getCommand()) {
      default:
        System.out.println("Lol");
    }
    action = null;
    System.out.println("Completed transaction");
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
  }
 }
}
