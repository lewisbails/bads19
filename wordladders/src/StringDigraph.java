
import edu.princeton.cs.algs4.*;
import java.util.NoSuchElementException;

public class StringDigraph {
	private final int V;
	private int E;
	private RedBlackBST<String,SET<Integer>> graph;
	private RedBlackBST<String,SET<Integer>> combinations;
 	public StringDigraph(In in) {
 		combinations = new RedBlackBST<>();
 		graph = new RedBlackBST<>();

        try {
            while (in.hasNextLine()){
                String v = in.readLine();
                graph.put(v,new SET<>());
            }
            for (String v : graph.keys()){
                String[] s = v.substring(1).split("");
                Insertion.sort(s);
                String lastfour = s[0]+s[1]+s[2]+s[3];
                // System.out.println(v);
                // System.out.println(lastfour);
                for (String combination : fourcombos(v)){
                    // System.out.println(combination);
                    if (!combinations.contains(combination)){
                        combinations.put(combination,new SET<>());
                    }
                    if (!combinations.get(combination).contains(graph.rank(v))){
                        combinations.get(combination).add(graph.rank(v));
                    }
                }
                if (combinations.contains(lastfour)){
                    graph.put(v,combinations.get(lastfour));
                }
            }
            V = graph.size();
            // ranks();
            // combos();
            // joins();
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

    public Iterable<Integer> adj(String v){
        return graph.get(v);
    }
 
    public int E(){
    	return E;
    }

    public int outdegree(int v){
    	String key = graph.select(v);
    	return graph.get(key).size();
    }

    public int outdegree(String v){
        return graph.get(v).size();
    }

    public int V(){
    	return V;
    }

    public int rank(String key){
        return graph.rank(key);
    }

    public String key(int v){
        return graph.select(v);
    }

    // public void ranks(){
    //     for (String key : graph.keys()){
    //         System.out.println(key+" "+graph.rank(key));
    //     }
    // }

    // public void joins(){
    //     for (String key : graph.keys()){
    //         System.out.println(key+": ");
    //         for (Integer item : graph.get(key)){
    //             System.out.print(item+" ");
    //         }
    //         System.out.println("");
    //     }
    // }

    // public void combos(){
    //     for (String key : combinations.keys()){
    //         System.out.println(key+": ");
    //         for (Integer item : combinations.get(key)){
    //             System.out.print(item+" ");
    //         }
    //         System.out.println("");
    //     }
    // }

}