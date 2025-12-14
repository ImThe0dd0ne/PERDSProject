package main.graph;

// Edge between nodes with weight (travel time)
public class GraphEdge {
    public String fromId;
    public String toId;
    public double weight; // travel time in minutes

    public GraphEdge(String from, String to, double weight) {
        this.fromId = from;
        this.toId = to;
        this.weight = weight;
    }

    // Check if this edge connects two nodes
    public boolean connects(String node1, String node2) {
        return (fromId.equals(node1) && toId.equals(node2)) ||
                (fromId.equals(node2) && toId.equals(node1));
    }

    @Override
    public String toString() {
        return fromId + " -> " + toId + " (" + weight + " min)";
    }
}