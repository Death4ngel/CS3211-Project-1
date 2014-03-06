public class Action {
 private Command command;
 private int srcAccountId;
 private int amount;
 private int destAccountId;
 private int pinNumber; 
 
 //for check balance
 public Action(int srcAccountId, Command command) {
  this.srcAccountId = srcAccountId;
  this.command = command;
 }
 //for authentication/transfer/withdraw/deposit 3rd argument can represent pinNumber or withdraw/deposit amt
 public Action(int srcAccountId, Command command, int argument) {
   this(srcAccountId, command);
   this.pinNumber = (command == Command.authenticate) ? argument : -1;
   this.amount = (command == Command.withdraw || command == Command.deposit) ? argument : 0;
 }
 //for transfer
 public Action(int srcAccountId, Command command, int destAccountId, int amount) {
   this(srcAccountId, command);
   this.destAccountId = destAccountId;
   this.amount = amount;
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
