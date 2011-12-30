import org.bukkit.Location;

public class Waypoint {
    private boolean powered;
    private String alignment;
    private Location teleportLocation;
    private Location registrationLocation;
    private static final double DEFAULT_WAYPOINT_RADIUS = 10d;

    /**
     * A waypoint can be powered or unpowered.
     * Typically (probably always) all waypoints will be unpowered or powered at the same time.
     * The waypoints will be powered in a cycle.
     * At the start of each cycle the player’s exhausted souls will be replenished.
     * The typical power cycle will be:
     *   unpowered during the day -> powered at night
     * An unpowered waypoint cannot be activated, and it cannot be used for basic teleportation.
     * If a waypoint is bound to a soul then it can always be soul teleported to, regardless of its power state.
     * @return
     */
    public boolean isPowered() {
        return powered;
    }

    public String getAlignment(){
        return alignment;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public boolean contains(Location location) {
        return this.registrationLocation.distance(location) < DEFAULT_WAYPOINT_RADIUS;
    }
}
