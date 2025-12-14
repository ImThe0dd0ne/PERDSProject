package main.model;

import java.time.LocalDateTime;

public class Incident {
    private String id;
    private Location location;
    private String type; // "fire", "medical", "police"
    private int severity; // 1-5, 5 = most severe
    private LocalDateTime reportedTime;
    private boolean active;
    private String assignedUnitId;

    public Incident(Location location, String type, int severity) {
        this.id = "INC-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
        this.location = location;
        this.type = type;
        this.severity = Math.max(1, Math.min(5, severity)); // Ensure 1-5 range
        this.reportedTime = LocalDateTime.now();
        this.active = true;
        this.assignedUnitId = null;
    }

    // Getters
    public String getId() { return id; }
    public Location getLocation() { return location; }
    public String getType() { return type; }
    public int getSeverity() { return severity; }
    public boolean isActive() { return active; }
    public String getAssignedUnitId() { return assignedUnitId; }
    public LocalDateTime getReportedTime() { return reportedTime; }

    // Setters
    public void setActive(boolean active) { this.active = active; }
    public void setAssignedUnitId(String unitId) { this.assignedUnitId = unitId; }

    public long getAgeInMinutes() {
        return java.time.Duration.between(reportedTime, LocalDateTime.now()).toMinutes();
    }

    @Override
    public String toString() {
        return String.format("Incident[%s: %s at %s, severity=%d, active=%s]",
                id, type, location, severity, active);
    }
}