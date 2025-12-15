package main.algorithm;

import main.graph.EmergencyNetwork;
import main.graph.GraphEdge;
import main.graph.GraphNode;
import java.util.*;

// Dijkstra's algorithm
public class PathFinder {
    private EmergencyNetwork network;

    public PathFinder(EmergencyNetwork network) {
        this.network = network;
    }

    public PathResult findShortestPath(String startId, String endId) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>();

        // Initialize
        for (String nodeId : getAllNodeIds()) {
            distances.put(nodeId, Double.MAX_VALUE);
        }
        distances.put(startId, 0.0);
        queue.offer(new NodeDistance(startId, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            String currentId = current.nodeId;

            if (visited.contains(currentId)) continue;
            visited.add(currentId);

            // Stop if a destination is reached
            if (currentId.equals(endId)) break;

            // a check on all neighbours
            for (GraphEdge edge : network.getEdgesFrom(currentId)) {
                String neighborId = edge.toId;
                if (visited.contains(neighborId)) continue;

                double newDist = distances.get(currentId) + edge.weight;
                if (newDist < distances.get(neighborId)) {
                    distances.put(neighborId, newDist);
                    previous.put(neighborId, currentId);
                    queue.offer(new NodeDistance(neighborId, newDist));
                }
            }
        }

        // path is buolt
        List<String> path = new ArrayList<>();
        String current = endId;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        // a check to see if the path was found
        if (path.size() == 1 && !path.get(0).equals(startId)) {
            return new PathResult(null, Double.MAX_VALUE); // No path
        }

        return new PathResult(path, distances.get(endId));
    }

    private Set<String> getAllNodeIds() {
        Set<String> nodeIds = new HashSet<>();

        for (main.graph.GraphNode node : network.getAllNodes()) {
            nodeIds.add(node.id);
        }

        return nodeIds;
    }

    // priority queue class
    private static class NodeDistance implements Comparable<NodeDistance> {
        String nodeId;
        double distance;

        NodeDistance(String id, double dist) {
            this.nodeId = id;
            this.distance = dist;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    // the result class
    public static class PathResult {
        public final List<String> path;
        public final double totalDistance;

        public PathResult(List<String> path, double distance) {
            this.path = path;
            this.totalDistance = distance;
        }

        public boolean hasPath() {
            return path != null && !path.isEmpty();
        }
    }
}