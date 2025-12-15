package main;

import main.model.*;
import main.graph.*;
import main.algorithm.PathFinder;

// My main test file
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting the PERDS system test");

        // Test 1
        testLocation();

        // Test 2
        testIncidents();

        // Test 3
        testGraphPathfinding();

        // Test 4
        testFullDispatch();

        System.out.println("All of the tests are done and passed!!");
    }

    static void testLocation() {
        System.out.println("\n[Test 1] Location class");
        Location l1 = new Location(50.0, -1.0);
        Location l2 = new Location(51.0, -1.5);

        double d = l1.distanceTo(l2);
        System.out.println("Distance calc: " + d);

        // Check if equals works
        Location l3 = new Location(50.0, -1.0);
        System.out.println("l1 equals l3? " + l1.equals(l3));
    }

    static void testIncidents() {
        System.out.println("\n[Test 2] Incident handling");

        Incident inc = new Incident(new Location(51.5, -0.1), "fire", 5);
        System.out.println("New incident: " + inc.getId());
        System.out.println("Type: " + inc.getType());

        ResponseUnit ru = new ResponseUnit("fire1", "fire", new Location(51.5, -0.2));
        ru.assignToIncident(inc.getId());
        System.out.println("Unit assigned: " + ru.isAvailable());

        ru.markAvailable();
        System.out.println("Unit freed up: " + ru.isAvailable());
    }

    static void testGraphPathfinding() {
        System.out.println("\n[Test 3] Graph and pathfinding");

        EmergencyNetwork net = new EmergencyNetwork();

        net.addNode(new GraphNode("a", "Point A", new Location(0, 0), GraphNode.NodeType.CITY));
        net.addNode(new GraphNode("b", "Point B", new Location(0, 10), GraphNode.NodeType.CITY));
        net.addNode(new GraphNode("c", "Point C", new Location(10, 10), GraphNode.NodeType.CITY));

        net.addEdge(new GraphEdge("a", "b", 10.0));
        net.addEdge(new GraphEdge("b", "c", 15.0));
        net.addEdge(new GraphEdge("a", "c", 30.0));

        // Tries ti find a path
        PathFinder pf = new PathFinder(net);
        PathFinder.PathResult res = pf.findShortestPath("a", "c");

        if (res != null && res.hasPath()) {
            System.out.println("Path found: " + res.path);
            System.out.println("Cost: " + res.totalDistance);
        } else {
            System.out.println("No path or error in pathfinder");
        }
    }

    static void testFullDispatch() {
        System.out.println("\n[Test 4] Dispatch system");

        DispatchCenter dc = new DispatchCenter();

        // Units are added
        dc.registerUnit(new ResponseUnit("u1", "ambulance", new Location(51.5, -0.1)));
        dc.registerUnit(new ResponseUnit("u2", "fire", new Location(51.6, -0.2)));

        // Some incidents are reported
        String id1 = dc.reportIncident(new Location(51.55, -0.15), "medical", 8);
        String id2 = dc.reportIncident(new Location(51.65, -0.25), "fire", 9);

        System.out.println("After reporting: " + dc.getActiveIncidentCount() + " active incidents");
        System.out.println("Available units: " + dc.getAvailableUnitCount());

        // One resolved
        dc.resolveIncident(id1);
        System.out.println("After resolve: " + dc.getActiveIncidentCount() + " active");
    }
}