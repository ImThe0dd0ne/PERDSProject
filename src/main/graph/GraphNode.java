package main.graph;

import main.model.Location;

// Node in the graph which shows a location in the network
public class GraphNode {
    public String id;
    public String name;
    public Location location;
    public NodeType type; // cities and stations etc

    public GraphNode(String id, String name, Location loc, NodeType type) {
        this.id = id;
        this.name = name;
        this.location = loc;
        this.type = type;
    }

    public enum NodeType {
        CITY, HOSPITAL, FIRE_STATION, POLICE_STATION, INCIDENT
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}