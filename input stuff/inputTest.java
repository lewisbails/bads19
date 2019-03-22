import java.util.*;

public class inTest{
	private ArrayList<TreeSet<Integer>> sets;
	
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        sc.nextLine();
        inTest it = new inTest(n);
            for(int i = 0; i < m; i++) {
                String[] line = sc.nextLine().split(" ");
                String q = line[0];
                int s = Integer.parseInt(line[1]);
                int t = Integer.parseInt(line[2]);
                if (q.equals("u")) it.union(s,t);
                else if (q.equals("q")) System.out.println(it.connected(s,t) ? "yes":"no");
                else it.move(s, t);
        }
    }
	
	public inTest(int n) {
		sets = new ArrayList<TreeSet<Integer>>();
		for (int i = 0;i<n;i++) {
			TreeSet<Integer> temp = new TreeSet<Integer>();
			temp.add(i);
			sets.add(temp);
		}
	}
	
	private void union(int s, int t) {
		TreeSet temp = sets.get(t);
		temp.addAll(sets.get(s));
		for (int i : sets.get(s)) {
			sets.set(i,temp);
		}
		for (int i : sets.get(t)) {
			sets.set(i,temp);
		}
	}
	
	private boolean connected(int s, int t) {
		return sets.get(s) == sets.get(t);
	}
	
	private void move(int s, int t) {
	TreeSet tempR = sets.get(s);
	tempR.remove(s);
	for (int i : sets.get(s)) {
		sets.set(i,tempR);
	}
	TreeSet tempA = sets.get(t);
	tempA.add(s);
	for (int i : sets.get(t)) {
		sets.set(i, tempA);
	}
	}
}