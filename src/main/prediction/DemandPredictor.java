package main.prediction;

import main.model.Location;
import java.util.*;

// this class is created to help try and determine where incidents may happen and occur the most
public class DemandPredictor {

    // recent incidents being tracked
    private List<Location> recentIncidents;
    private int maxSize;

    // this groups nearby locations
    private static final double GRID_SIZE = 0.05; // about 5km

    public DemandPredictor(int size) {
        recentIncidents = new ArrayList<>();
        maxSize = size;
    }

    // adds a new incident to the history
    public void logIncident(Location loc) {
        recentIncidents.add(loc);

        // this part keeps the list from getting too big
        if (recentIncidents.size() > maxSize) {
            recentIncidents.remove(0);  // remove oldest
        }
    }

    // predicts where next incident may happen
    public Location predict() {
        if (recentIncidents.isEmpty()) {
            System.out.println("[Predictor] No data yet");
            return null;
        }

        if (recentIncidents.size() < 3) {
            // there isnt enough data to base a prediction off of
            return recentIncidents.get(recentIncidents.size() - 1);
        }

        // groups incidents by the area
        HashMap<String, ArrayList<Location>> groups = new HashMap<>();

        for (int i = 0; i < recentIncidents.size(); i++) {
            Location l = recentIncidents.get(i);
            String key = getAreaKey(l);

            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<Location>());
            }
            groups.get(key).add(l);
        }

        // finds the area with most incidents
        ArrayList<Location> biggestGroup = null;
        int maxCount = 0;

        for (ArrayList<Location> group : groups.values()) {
            if (group.size() > maxCount) {
                maxCount = group.size();
                biggestGroup = group;
            }
        }

        if (biggestGroup == null || biggestGroup.isEmpty()) {
            return null;
        }

        // calculates the center
        double sumLat = 0.0;
        double sumLon = 0.0;

        for (Location loc : biggestGroup) {
            sumLat += loc.getLatitude();
            sumLon += loc.getLongitude();
        }

        double centerLat = sumLat / biggestGroup.size();
        double centerLon = sumLon / biggestGroup.size();

        System.out.println("[Predictor] Found a hotspot with " + maxCount + " incidents");

        return new Location(centerLat, centerLon);
    }

    // creates a key for grouping nearby locations
    private String getAreaKey(Location loc) {
        // grid-based grouping
        int latIdx = (int) Math.floor(loc.getLatitude() / GRID_SIZE);
        int lonIdx = (int) Math.floor(loc.getLongitude() / GRID_SIZE);
        return latIdx + "|" + lonIdx;
    }

    // how many incidents there are
    public int getIncidentCount() {
        return recentIncidents.size();
    }

    // clears all the data
    public void reset() {
        recentIncidents.clear();
        System.out.println("[Predictor] Data cleared");
    }

    // debugging
    public void printStats() {
        System.out.println("Tracking " + recentIncidents.size() + " incidents");
        if (!recentIncidents.isEmpty()) {
            Location last = recentIncidents.get(recentIncidents.size() - 1);
            System.out.println("Last incident was at: " + last.getLatitude() + ", " + last.getLongitude());
        }
    }
}