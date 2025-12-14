package main.model;

public class Location {
    private double latitude;
    private double longitude;

    // i included validation after consideration of coodination ranges
    public Location(double latitude, double longitude) {

        if (latitude < -90 || latitude > 90) {
            throw new RuntimeException("Invalid latitude: " + latitude);
        }
        if (longitude < -180 || longitude > 180) {
            throw new RuntimeException("Invalid longitude: " + longitude);
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // the haversine formula
    public double haversineDistance(Location other) {
        // radians
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lon2 = Math.toRadians(other.longitude);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.sin(dlat/2) * Math.sin(dlat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dlon/2) * Math.sin(dlon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        // the earths radius in km
        return 6371 * c;
    }

    public double distanceTo(Location other) {
        return haversineDistance(other);
    }

    @Override
    public String toString() {
        return String.format("Location(%.4f, %.4f)", latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        return Math.abs(latitude - other.latitude) < 0.000001 &&
                Math.abs(longitude - other.longitude) < 0.000001;
    }

    @Override
    public int hashCode() {
        return (int)(latitude * 1000000) ^ (int)(longitude * 1000000);
    }
}