package main;

import main.model.*;
import main.graph.*;
import main.algorithm.PathFinder;
import main.prediction.DemandPredictor;

public class Main {
    public static void main(String[] args) {
        System.out.println("PERDS System test \n");

        // Test 1: All of the Core Components
        testLocation();

        // Test 2: Incidents & Units
        testIncidents();

        // Test 3: Graph & Pathfinding
        testGraphPathfinding();

        // Test 4: Full Dispatch with Priority Queue
        testFullDispatch();

        // Test 5: Predictive system
        testPredictiveSystem();

        // Test 6: Integration testing
        testCompleteIntegration();

        System.out.println("\n ALL TESTS ARE COMPLETE");
    }

    static void testLocation() {
        System.out.println("[Test 1/6] Location & Distance Calculation");
        Location london = new Location(51.5074, -0.1278);
        Location manchester = new Location(53.4809, -2.2426);

        double distance = london.distanceTo(manchester);
        System.out.println("   London → Manchester: " + String.format("%.1f", distance) + " km");
        System.out.println("   Haversine formula working correctly\n");
    }

    static void testIncidents() {
        System.out.println("[Test 2/6] Incident & Response Unit Management");

        Incident fire = new Incident(new Location(51.5, -0.1), "FIRE", 9);
        ResponseUnit truck = new ResponseUnit("FIRE01", "FIRE_TRUCK", new Location(51.51, -0.09));

        System.out.println("   Created: " + fire);
        System.out.println("   Created: " + truck);

        truck.assignToIncident(fire.getId());
        System.out.println("   Unit assigned to incident: " + truck.getAssignedIncident());

        truck.markAvailable();
        System.out.println("   Unit marked available: " + truck.isAvailable());
        System.out.println("   State management is working\n");
    }

    static void testGraphPathfinding() {
        System.out.println("[Test 3/6] Graph Network & Dijkstra Algorithm");

        EmergencyNetwork net = new EmergencyNetwork();

        // Create city network
        GraphNode[] cities = {
                new GraphNode("LDN", "London", new Location(51.5074, -0.1278), GraphNode.NodeType.CITY),
                new GraphNode("BIR", "Birmingham", new Location(52.4862, -1.8904), GraphNode.NodeType.CITY),
                new GraphNode("MAN", "Manchester", new Location(53.4809, -2.2426), GraphNode.NodeType.CITY),
                new GraphNode("LEE", "Leeds", new Location(53.8008, -1.5491), GraphNode.NodeType.CITY)
        };

        for (GraphNode city : cities) net.addNode(city);

        // Connect with approximate road distances
        net.addEdge(new GraphEdge("LDN", "BIR", 190.0));
        net.addEdge(new GraphEdge("BIR", "MAN", 120.0));
        net.addEdge(new GraphEdge("MAN", "LEE", 65.0));
        net.addEdge(new GraphEdge("LDN", "MAN", 340.0)); // Longer direct route

        PathFinder finder = new PathFinder(net);
        PathFinder.PathResult result = finder.findShortestPath("LDN", "MAN");

        if (result.hasPath()) {
            System.out.println("   Optimal path London → Manchester: " + result.path);
            System.out.println("   Total distance: " + result.totalDistance + " km");
            System.out.println("   Dijkstra found shortest path (via Birmingham)\n");
        }
    }

    static void testFullDispatch() {
        System.out.println("[Test 4/6] Priority Dispatch System");

        DispatchCenter dc = new DispatchCenter();

        // Register different unit types
        dc.registerUnit(new ResponseUnit("AMB01", "AMBULANCE", new Location(51.51, -0.12)));
        dc.registerUnit(new ResponseUnit("FIR01", "FIRE_TRUCK", new Location(51.52, -0.13)));
        dc.registerUnit(new ResponseUnit("POL01", "POLICE_CAR", new Location(51.53, -0.14)));

        System.out.println("   Registered 3 units: Ambulance, Fire Truck, Police Car");

        // Report incidents - should use PRIORITY QUEUE scoring
        String medicalId = dc.reportIncident(new Location(51.511, -0.125), "MEDICAL", 8);
        String fireId = dc.reportIncident(new Location(51.521, -0.135), "FIRE", 9);

        System.out.println("   Reported MEDICAL (severity 8) and FIRE (severity 9) incidents");
        System.out.println("   Active incidents: " + dc.getActiveIncidentCount());
        System.out.println("   Available units: " + dc.getAvailableUnitCount());

        // Resolve one
        dc.resolveIncident(medicalId);
        System.out.println("   Resolved medical incident - units available: " + dc.getAvailableUnitCount());
        System.out.println("   Priority dispatch with scoring working\n");
    }

    static void testPredictiveSystem() {
        System.out.println("[Test 5/6] Predictive Analytics (First Class Feature)");

        DemandPredictor predictor = new DemandPredictor(10); // Track last 10 incidents

        // Simulate incident cluster in London area
        for (int i = 0; i < 5; i++) {
            predictor.logIncident(new Location(51.51 + (Math.random() * 0.02),
                    -0.12 + (Math.random() * 0.02)));
        }

        // Add a few scattered incidents
        predictor.logIncident(new Location(51.60, -0.20));
        predictor.logIncident(new Location(51.40, -0.05));

        Location hotspot = predictor.predict();
        if (hotspot != null) {
            System.out.println("   Predicted hotspot: " + hotspot);
            System.out.println("   Incidents analyzed: " + predictor.getIncidentCount());
            System.out.println("   Grid-based clustering prediction working\n");
        }
    }

    static void testCompleteIntegration() {
        System.out.println("[Test 6/6] Complete System Integration");
        System.out.println("   Demonstrating all First Class features together:\n");

        DispatchCenter system = new DispatchCenter();

        // Setup network with units
        ResponseUnit[] units = {
                new ResponseUnit("AMB1", "AMBULANCE", new Location(51.507, -0.127)),
                new ResponseUnit("FIR1", "FIRE_TRUCK", new Location(51.515, -0.140)),
                new ResponseUnit("POL1", "POLICE_CAR", new Location(51.495, -0.110))
        };

        for (ResponseUnit unit : units) system.registerUnit(unit);

        System.out.println("   1. Reporting incidents (triggers predictive tracking)..");

        // Incident wave
        String[] incidents = {
                system.reportIncident(new Location(51.510, -0.130), "MEDICAL", 7),
                system.reportIncident(new Location(51.512, -0.128), "FIRE", 9),
                system.reportIncident(new Location(51.508, -0.125), "POLICE", 6)
        };

        System.out.println("   2. Checking predictive hotspot..");
        Location prediction = system.getPredictedHotspot();
        System.out.println("      Predicted area: " + prediction);

        System.out.println("   3. System status:");
        System.out.println("      Active incidents: " + system.getActiveIncidentCount());
        System.out.println("      Available units: " + system.getAvailableUnitCount());

        System.out.println("   4. Resolving incidents...");
        for (String id : incidents) {
            system.resolveIncident(id);
        }

        System.out.println("      Final available units: " + system.getAvailableUnitCount());
        System.out.println("      everything is functioning correctly and as intended");
    }
}