package main.model;

import main.graph.EmergencyNetwork;
import main.graph.GraphNode;
import main.prediction.DemandPredictor;
import java.util.*;
import java.util.PriorityQueue;
import java.util.Comparator;

// DispatchCenter - manages all the emergency responses
public class DispatchCenter {
    // Store all registered units
    private HashMap<String, ResponseUnit> unitMap;

    // Track current incidents
    private HashMap<String, Incident> incidentMap;

    // Incidents waiting for assignment
    private ArrayList<Incident> pendingQueue;

    // The road network for routing
    private EmergencyNetwork network;

    // Predictive analysis for hotspots
    private DemandPredictor predictor;

    // Constructor
    public DispatchCenter() {
        unitMap = new HashMap<>();
        incidentMap = new HashMap<>();
        pendingQueue = new ArrayList<>();
        network = new EmergencyNetwork();
        predictor = new DemandPredictor(50);  // track last 50 incidents
    }

    // Adds a unit to the system
    public void registerUnit(ResponseUnit unit) {
        String id = unit.getId();
        unitMap.put(id, unit);

        // Adds to network for path calculations
        GraphNode node = new GraphNode(
                "unit-" + id,
                "Unit " + id,
                unit.getPosition(),
                GraphNode.NodeType.FIRE_STATION
        );
        network.addNode(node);

        System.out.println("Registered unit: " + id);
    }

    // Called when someone reports an emergency
    public String reportIncident(Location loc, String type, int urgency) {
        // here an incident object is created
        Incident inc = new Incident(loc, type, urgency);
        String id = inc.getId();

        incidentMap.put(id, inc);
        pendingQueue.add(inc);

        // log this incident for prediction analysis
        predictor.logIncident(loc);

        System.out.println("Reported: " + type + " incident at " + loc);

        // Tries to send someone straight away
        dispatchIfPossible();

        return id;
    }

    // My dispatch algorithm
    private void dispatchIfPossible() {
        // A check for free units
        ArrayList<ResponseUnit> freeUnits = new ArrayList<>();
        for (ResponseUnit unit : unitMap.values()) {
            if (unit.isAvailable()) {
                freeUnits.add(unit);
            }
        }

        if (freeUnits.isEmpty()) {
            System.out.println("Warning: All units are busy");
            return;
        }

        // Process pending incidents
        Iterator<Incident> it = pendingQueue.iterator();
        while (it.hasNext()) {
            Incident inc = it.next();

            // Finds a unit to send
            ResponseUnit chosen = pickUnitForIncident(inc, freeUnits);

            if (chosen != null) {
                // Assigns them
                chosen.assignToIncident(inc.getId());
                inc.setAssignedUnitId(chosen.getId());
                it.remove();
                freeUnits.remove(chosen);

                System.out.println("Assigned " + chosen.getId() + " to incident " + inc.getId());

                if (freeUnits.isEmpty()) {
                    break; // No more free units available
                }
            }
        }
    }

    // Pick which unit to send
    private ResponseUnit pickUnitForIncident(Incident inc, List<ResponseUnit> available) {
        // gets the closest available unit
        ResponseUnit best = null;
        double minDist = 999999.0;

        for (ResponseUnit unit : available) {
            double dist = unit.getPosition().distanceTo(inc.getLocation());
            if (dist < minDist) {
                minDist = dist;
                best = unit;
            }
        }

        return best;
    }

    // When an incident is resolved its closed
    public void resolveIncident(String incidentId) {
        Incident inc = incidentMap.get(incidentId);
        if (inc == null) {
            System.out.println("Error: Incident " + incidentId + " not found");
            return;
        }

        // Marked as resolved
        inc.setActive(false);

        // Frees up the unit
        String unitId = inc.getAssignedUnitId();
        if (unitId != null) {
            ResponseUnit unit = unitMap.get(unitId);
            if (unit != null) {
                unit.markAvailable();
            }
        }

        // Clean up
        incidentMap.remove(incidentId);
        System.out.println("Closed incident: " + incidentId);
    }

    // Getters for testing
    public int getActiveIncidentCount() {
        return incidentMap.size();
    }

    public int getAvailableUnitCount() {
        int count = 0;
        for (ResponseUnit unit : unitMap.values()) {
            if (unit.isAvailable()) count++;
        }
        return count;
    }

    public int countTotalUnits() {
        return unitMap.size();
    }

    // Get predicted hotspot area
    public Location getPredictedHotspot() {
        return predictor.predict();
    }

    // Get how many incidents are being tracked for predictions
    public int getPredictionDataSize() {
        return predictor.getIncidentCount();
    }

    // Helper to print status
    public void printStats() {
        System.out.println("\n--- Dispatch Center Stats ---");
        System.out.println("Units: " + countTotalUnits() + " total, " +
                getAvailableUnitCount() + " available");
        System.out.println("Incidents: " + getActiveIncidentCount() + " active");
        System.out.println("Pending assignments: " + pendingQueue.size());

        // Show prediction info
        Location hotspot = getPredictedHotspot();
        if (hotspot != null) {
            System.out.println("Predicted hotspot: " + hotspot.getLatitude() + ", " + hotspot.getLongitude());
        } else {
            System.out.println("Predicted hotspot: Not enough data yet");
        }
        System.out.println("Tracking " + getPredictionDataSize() + " incidents for predictions");
    }
}