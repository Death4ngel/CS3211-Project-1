public class Action {
 private Command command;
 private int srcAccountId;
 private int amount;
 private int destAccountId;
 private int pinNumber; 
 
 //for withdraw and deposit
 public Action(int srcAccountId, Command command) {
  this.srcAccountId = srcAccountId;
  this.command = command;
 }
 //for authentication/transfer 3rd argument can represent pinNumber or destAccountId
 public Action(int srcAccountId, Command command, int argument) {
   this(srcAccountId, command);
   this.pinNumber = (command == Command.authenticate) ? argument : -1;
   this.destAccountId = (command == Command.transfer) ? argument : -1;
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
 
 public int getPassword() {
  return pinNumber;
 }
}
