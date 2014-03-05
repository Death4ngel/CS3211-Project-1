import java.util.*;

public class Tests {
  public boolean test1() {
    return false;
  }
  
  
  public void runTests() {
    System.out.println("Running Tests..");
    if(test1()) { 
      System.out.println("Test1 passed!"); 
    } else { 
      System.out.println("Test1 failed!"); 
    }
    
  }
  //test transfer
  //test withdraw
  //test deposit
  //test database integrity
  //test assertions
}