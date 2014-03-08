import java.util.ArrayList;
import java.util.List;

public class Atms {
  private final List<Atm> atms;
  
  public Atms(int N, Clouds clouds) {
    this.atms = new ArrayList<Atm>(N);
    for (int i = 0; i < N; i++) {
    	Atm atm =  new Atm(i, clouds);
    	atms.add(i, atm);
    	new Thread(atm).start();
    }
  }
  
  public Atm getAtm(int atmId) {
	  return atms.get(atmId);
  }
}
