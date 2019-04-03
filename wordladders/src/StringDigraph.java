
import edu.princeton.cs.algs4.Insertion;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import java.util.NoSuchElementException;

public class StringDigraph {
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
                String[] s = v.substring(1).split("");
                Insertion.sort(s);
                String lastfour = s[0]+s[1]+s[2]+s[3];
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
    	String[] chars = word.split("");
    	Insertion.sort(chars);
    	String[] combos = new String[5];
    	combos[0] = chars[0]+chars[1]+chars[3]+chars[4];
    	combos[1] = chars[0]+chars[1]+chars[2]+chars[3];
    	combos[2] = chars[0]+chars[1]+chars[2]+chars[4];
		combos[3] = chars[0]+chars[2]+chars[3]+chars[4];
    	combos[4] = chars[1]+chars[2]+chars[3]+chars[4];
    	return combos;
    }

    public Iterable<Integer> adj(int v){
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

    public int V(){
    	return V;
    }

}