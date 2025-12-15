package main.graph;

import main.model.Location;
import java.util.*;

// My first graph implementation with an adjacency list
public class EmergencyNetwork {
    private Map<String, GraphNode> nodes;
    private Map<String, List<GraphEdge>> edges;

    public EmergencyNetwork() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    public void addNode(GraphNode node) {
        nodes.put(node.id, node);
        edges.put(node.id, new ArrayList<>());
    }

    public Set<String> getAllNodeIds() {
        return new HashSet<>(nodes.keySet());
    }

    public void addEdge(GraphEdge edge) {
        edges.get(edge.fromId).add(edge);
        edges.get(edge.toId).add(new GraphEdge(edge.toId, edge.fromId, edge.weight));
    }

    public Collection<GraphNode> getAllNodes() {
        return nodes.values();
    }

    public GraphNode getNode(String id) {
        return nodes.get(id);
    }

    public List<GraphEdge> getEdgesFrom(String nodeId) {
        return edges.getOrDefault(nodeId, new ArrayList<>());
    }

    public GraphNode findNearestNode(Location location) {
        GraphNode nearest = null;
        double minDist = Double.MAX_VALUE;

        for (GraphNode node : nodes.values()) {
            double dist = node.location.distanceTo(location);
            if (dist < minDist) {
                minDist = dist;
                nearest = node;
            }
        }
        return nearest;
    }

    public List<String> findPath(String startId, String endId) {
        return new ArrayList<>();
    }
}