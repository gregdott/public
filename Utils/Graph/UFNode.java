package Utils.Graph;

/*
 * Author: Gregory Dott
 * 27-10-2022
 * 
 * UFNode is a class representing a node for use in Union-Find applications (disjoint data set)
 * It contains a node index and a parent index
 * It could contain a size or a rank, but for the implementation it is being used for (Kruskal's Algorithm), 
 * we are not going to be worrying about that.
 * 
 */

public class UFNode {
    private int node, parent;

    public UFNode(int node, int parent) {
        this.node = node;
        this.parent = parent;
    }

    public void print() {
        System.out.println("NODE: " + node + ", Parent: " + parent);
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getNode() {
        return node;
    }

    public int getParent() {
        return parent;
    }

    public boolean isRoot() {
        if (node == parent) {
            return true;
        } else {
            return false;
        }
    }
}
