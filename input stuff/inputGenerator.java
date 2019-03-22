import java.util.Random;

public class inputGenerator{
	private static final String[] operations = {"q","u","m"};
	
	public static void main(String[] args) {
		Random rd = new Random();
		int n = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);
		System.out.printf("%d %d%n",n,m);
		for (int i = 0; i < m; i++) {
			int s = rd.nextInt(n);
			int t = rd.nextInt(n);
			System.out.printf("%s %d %d%n", operations[rd.nextInt(3)],s,t);
		}
	}
}