public class Atm implements Runnable {
 private Cloud cloud;
 private boolean received;
 private Action action;
 private boolean authenticated = false;
 private Main main;
 
 Atm(Main main) {
   this.main = main;
 }

 public void doAction(Action action) {
  this.action = action;
  synchronized (this) {
   this.notify();
  }
 }

 public Action getAction() {
  return action;
 }
 
 public boolean getAuthStatus() {
   return this.authenticated;
 }

 @Override
 public void run() {
  cloud = Cloud.allocateCloud(this);
  System.out.println("Cloud allocated: " + cloud.getCloudId());
  while (true) {
   try {
    synchronized (this) {
     while (action == null) {
      System.out.println("ATM waiting for action.. ");
      wait(10000); // for an action
     }
    }
    System.out.println("Action received: " + this.getAction().getCommand().toString());
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
      case authenticate:
        System.out.println("Auth status received");
        
        break;
      default:
        System.out.println("Lol");
        break;
    }
    main.notify();
    action = null;
    System.out.println("finished loop");
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
  }
 }
}
