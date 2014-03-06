public class BankAccount {
 private int accountId;
 private int pinNumber;
 private int balance;

 public BankAccount(int accountId, int pinNumber, int balance) {
  this.accountId = accountId;
  this.pinNumber = pinNumber;
  this.balance = balance;
 }
 
 public int getAccountId() {
  return accountId;
 }
 
 public int getPassword() {
  return pinNumber;
 }

 public int getBalance() {
  return balance;
 }
 
}