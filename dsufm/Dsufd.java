import edu.princeton.cs.algs4.*;
import java.util.Scanner;

public class Dsufd {

    private Node[] nodes;
    private Node[] vacantNodes;
    // private byte[] rank;   // rank[i] = rank of subtree rooted at i (never more than 31)
    private int count;     // number of components
    public int nextVacant; // next slot of vacant node

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own 
     * component.
     *
     * @param  n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */

    private class Node {
        public Node parent;
        public Node[] C;
        public Node[] G;
        public int C_size;
        public int G_size;
        public int rank;
        public int element;
        public int id;


        public Node(int s){
            C = new Node[1];
            G = new Node[1];
            this.element = s;
            this.id = s;
        }

        public Node(int s, int id){
            C = new Node[1];
            G = new Node[1];
            this.element = s;
            this.id = id;
        }

        private Node[] grow(Node[] a){
            // System.out.println("growing");
            Node[] b = new Node[2*a.length];
            for (int i = 0; i< a.length ; i++){
                b[i] = a[i];
            }
            return b;
        }

        private Node[] shrink(Node[] a){
            // System.out.println("shrinking");
            Node[] b = new Node[a.length/2];
            for (int i = 0; i< a.length ; i++){
                b[i] = a[i];
            }
            return b;
        }

        public void addChild(Node child){
            C[C_size++] = child;
            // System.out.println("child added, csize is "+C_size);

            if (child.C_size>0){
                // System.out.println("new child had children");
                G[G_size++] = child;
            }
            if (C_size == C.length) C = grow(C);
            if (G_size == G.length) G = grow(G);
        }

        public void replaceChild(Node oldChild, Node newChild){
            // replace in C array
            // System.out.println("looking through "+C_size+" children");
            for (int i = 0; i<C_size; i++){
                if (C[i]==oldChild){
                    // System.out.println("found it");
                    C[i] = newChild;
                    break;
                }
            }
            // replace in G , or add newChild to G array if necessary
            // System.out.println("old child had "+oldChild.C_size+" children");
            if (oldChild.C_size>0){ //oldChild has children so should be in the G array
                for (int i = 0; i<G_size; i++){
                    if (G[i]==oldChild){ //found the oldChild in G
                        if (newChild.C_size>0) G[i] = newChild; //can switch out for newChild
                        else {
                            G[i] = null;
                            for (int j = i; j<G_size; j++){ //shift children to cover empty spot
                                G[j] = G[j+1];
                            }
                            G_size--;
                            if (G_size< (int) G.length/4) G = shrink(G);
                        }
                        return;
                    }
                }
                if (newChild.G_size>0){ //didnt find oldChild in G array despite the fact it had children itself, WTF?
                    G[G_size++] = newChild; //set newChild in G array
                    if (G_size==G.length) G = grow(G);
                    return;
                }
                return;
            }
        }

        public void deleteChild(Node oldChild){
            for (int i = 0 ;i<C_size ; i++){
                if (C[i] == oldChild){ //found the child
                    C[i] = null;
                    for (int j = i; j<C_size; j++){ //shift children to cover empty spot
                        C[j] = C[j+1];
                    }
                    C_size--;
                    if (C_size == 0) rank--;
                    if (C_size< (int) C.length/4) C = shrink(C);
                }
            }
        }

    }


    public Dsufd(int n) {
        if (n < 0) throw new IllegalArgumentException();
        count = n;
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
            nodes[i].parent = nodes[i];
        }
        vacantNodes = new Node[n]; //at most n vacant nodes in all trees
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param  p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public Node find(int p) {
        // validate(p);
        Node currentNode = nodes[p];
        while (currentNode != currentNode.parent) {
            currentNode.parent = currentNode.parent.parent;    // path compression by halving
            if (currentNode.parent != currentNode.parent.parent){ //if we're more than immediately below the root
                currentNode.parent.parent.addChild(currentNode);
                currentNode.parent.deleteChild(currentNode);
            }
            currentNode = currentNode.parent;
        }
        return currentNode; //root node
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int count() {
        return count;
    }
  
    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }
  
    /**
     * Merges the component containing site {@code p} with the 
     * the component containing site {@code q}.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IllegalArgumentException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        Node rootP = find(p);
        Node rootQ = find(q);

        if (rootP == rootQ) return;

        // make root of smaller rank point to root of larger rank
        if (rootP.rank < rootQ.rank){
            rootP.parent = rootQ;
            rootQ.addChild(rootP);
            // System.out.println("parent of "+rootP.element+" is now "+rootQ.element);
        }
        else if (rootP.rank > rootQ.rank){
            rootQ.parent = rootP;
            rootP.addChild(rootQ);
        }
        else {
            rootQ.parent = rootP;
            rootP.addChild(rootQ);
            rootP.rank++;
        }
        count--;
    }

    private void deleteElement(int s){
        // System.out.println(nodes[s].element+" "+nodes[s].parent.element);
        if (nodes[s].parent == nodes[s] && nodes[s].rank == 0){ //singleton, dont need to do anything
            // System.out.println(s+" is singleton");
            return;
        }
        Node oldNode = nodes[s];
        Node oldParent = oldNode.parent;
        Node newNode = new Node(-1,nextVacant);
        vacantNodes[nextVacant++] = newNode;
        newNode.rank = oldNode.rank;
        newNode.C_size = oldNode.C_size;
        newNode.G_size = oldNode.G_size;
        newNode.C = oldNode.C;
        newNode.G = oldNode.G;
        // System.out.println("ready to get C and G arrays");
        // System.out.println("C len of new node"+newNode.C.length+" C_size is "+newNode.C_size);
        // System.out.println("G len of new node"+newNode.G.length+" G_size is "+newNode.G_size);
        if (oldNode.parent != oldNode){ //if oldNode wasnt the root
            // System.out.println(oldNode.element+" wasn't root.");
            oldNode.parent.replaceChild(oldNode,newNode); 
            newNode.parent = oldNode.parent;
        }
        else { // it was the root
            newNode.parent = newNode;
        }

        for (int j = 0 ; j < newNode.C_size; j++){ //change parent Node of oldChildren to newNode
            // System.out.println(newNode.C_size);
            // System.out.println(j);
            newNode.C[j].parent = newNode;
        }
        oldNode = new Node(s);
        oldNode.parent = oldNode;
        nodes[s] = oldNode;
        // System.out.println(oldNode.element+" "+oldNode.C_size+" "+oldNode.parent.element);
        // System.out.println(newNode.element+" "+newNode.C_size+" "+newNode.parent.element);
        tidy(newNode);
    }

    private void tidy(Node s){
        if (s.C_size == 0 && s.element == -1){ //vacant leaf, delete it
            Node parent = s.parent;
            parent.deleteChild(s);
            int oldID = s.id;
            vacantNodes[s.id] = null;
            for (int i = oldID; i<nextVacant; i++){ //shift vacant nodes down
                try{
                    vacantNodes[i] = vacantNodes[i+1];
                    vacantNodes[i].id--;
                }
                catch (Exception e){
                    // System.out.println("probably null pointer");
                }
            }
            nextVacant--;
            if (parent.C_size == 0){ //parent is now leaf!
                // System.out.println(parent.element+" is now leaf: "+"rank is "+parent.rank);
                parent.rank = 0; //this should have already happened
            }
            tidy(parent);
        }
        else if (s.C_size == 1 && s.element == -1){ //only 1 child for this vacant node, let child bypass
            int oldID = s.id;
            Node parent = s.parent;
            if (parent != s){ //if s is not the root, detach s from its parent and let s's child bypass
                parent.replaceChild(s,s.C[0]);
                parent.deleteChild(s);
                s.C[0].parent = parent;
            }
            else{ //set only child as its own because s was the root
                s.C[0].parent = s.C[0];
                parent = s.C[0];
                // System.out.println(parent.element+" is now root");
            }
            // null out now deleted vacant node
            vacantNodes[oldID] = null;
            s = null;
            for (int i = oldID; i<nextVacant; i++){
                try{
                    vacantNodes[i] = vacantNodes[i+1];
                    vacantNodes[i].id--;
                }
                catch (Exception e){
                    // System.out.println("probably null pointer");
                }
            }
            nextVacant--;
            tidy(parent);
        }
    }

    private void move(int s, int t) {
        if (!connected(s, t)) {
            // System.out.println("not connected, proceed");
            deleteElement(s);
            union(s, t);
        }
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = nodes.length/2; //we reserve N elements for vacant nodes
        if (nodes[p].element < 0 || nodes[p].element >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));  
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        sc.nextLine();
        Dsufd ufm = new Dsufd(n);
            for(int i = 0; i < m; i++) {
                String[] line = sc.nextLine().split(" ");
                String q = line[0];
                int s = Integer.parseInt(line[1]);
                int t = Integer.parseInt(line[2]);
                if (q.equals("u")) ufm.union(s,t);
                else if (q.equals("q")) System.out.println(ufm.connected(s,t) ? "yes":"no");
                else ufm.move(s, t);
        }
    }
}