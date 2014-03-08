import java.util.Scanner;


public class Test implements Runnable {
	private int i;

	public static void main(String[] args) {
		int N = 4;
		for (int i = 0; i < N; i++) {
			new Thread(new Test(i)).start();
		}
	}
	
	public Test(int i) {
		this.i = i;
	}

	@Override
	public void run() {
		try (Scanner input = new Scanner(System.in)) {
			System.out.println("This is test machine " + i);
			String s = input.nextLine();
			System.out.println("You typed " + s + "in machine " + i);
		}
	}
}
