package main.model;

public class ResponseUnit {
    private String unitId;
    private String unitType; // "AMBULANCE", "FIRE", "POLICE"
    private Location currentPosition;
    private boolean available;
    private String assignedIncidentId;

    public ResponseUnit(String id, String type, Location startPos) {
        this.unitId = id;
        this.unitType = type;
        this.currentPosition = startPos;
        this.available = true;
        this.assignedIncidentId = null;
    }

    // the accessors
    public String getId() { return unitId; }
    public String getType() { return unitType; }
    public Location getPosition() { return currentPosition; }
    public boolean isAvailable() { return available; }
    public String getAssignedIncident() { return assignedIncidentId; }

    public void updatePosition(Location newPos) {
        currentPosition = newPos;
    }

    // assigns to an incident
    public void assignToIncident(String incidentId) {
        if (!available) {
            throw new IllegalStateException("Unit already assigned");
        }
        assignedIncidentId = incidentId;
        available = false;
    }

    // this marks as available
    public void markAvailable() {
        assignedIncidentId = null;
        available = true;
    }

    // this calculates the time to reach the location
    public double estimateTravelTime(Location destination, double speedKmh) {
        double distance = currentPosition.distanceTo(destination);
        return distance / speedKmh; // hours
    }

    @Override
    public String toString() {
        return unitType + " Unit " + unitId + " at " + currentPosition +
                (available ? " (Available)" : " (Busy)");
    }
}