
import edu.prineton.cs.algs4.Insertion;
import edu.prineton.cs.algs4.Digraph;
import edu.prineton.cs.algs4.Bag;
import edu.prineton.cs.algs4.In;
import edu.prineton.cs.algs4.RedBlackBST;

public class StringDigraph extends Digraph {
	private final int V;
	private int E;
	private RedBlackBST<String,Bag<Integer>> graph;
	private RedBlackBST<String,Bag<Integer>> combinations;


 	public StringDigraph(In in) {
 		combinations = new RedBlackBST<>();
 		graph = new RedBlackBST<>();

        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Digraph must be nonnegative");
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Digraph must be nonnegative");
            for (int i = 0; i < E; i++) {
                String v = in.readString();
                String lastfour = new String(Insertion.sort(v.substring(1).toCharArray()));
                if (!graph.contains(v)){
                	graph.put(v,new Bag<>());
                	for (String combination : fourcombos(v)){
                		if (!combinations.contains(combination)){
                			combinations.put(combination,new Bag<>());
            			}
            			combinations.get(combination).add(graph.rank(v));
                	}
                	if (combinations.contains(lastfour)){
                		graph.put(v,combinations.get(lastfour));
                	}
                }
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Digraph constructor", e);
        }
    }


    private String[] fourcombos(String word){
    	Char[] chars = Insertion.sort(v.toCharArray());
    	String[] combos = new String[5];
    	combos[0] = ""+chars[0]+chars[1]+chars[3]+chars[4];
    	combos[1] = ""+chars[0]+chars[1]+chars[2]+chars[3];
    	combos[2] = ""+chars[0]+chars[1]+chars[2]+chars[4];
		combos[3] = ""+chars[0]+chars[2]+chars[3]+chars[4];
    	combos[4] = ""+chars[1]+chars[2]+chars[3]+chars[4];
    	return combos;
    }

    private void addEdge(int v, int w){
    	return;
    }

    private Iterable<Integer> adj(int v){
		String key = graph.select(v);
		return graph.get(key); //exposes invarient, fix it
    }

    public int E(){
    	return E;
    }

    public int outdegree(int v){
    	String key = graph.select(v);
    	return graph.get(key).size();
    }

    public Digraph reverse(){
    	return;
    }

    public int V(){
    	return V;
    }

}