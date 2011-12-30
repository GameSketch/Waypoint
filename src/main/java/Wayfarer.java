import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class Wayfarer {
    private Collection<Soul> souls;
    private Collection<Waypoint> waypointNetwork;
    int soulStrength;
    private static final int DEFAULT_SOUL_STRENGTH = 5;
    private String alignment;
    private Player player;

    public Wayfarer() {
        this.soulStrength = DEFAULT_SOUL_STRENGTH;
        this.souls = new ArrayList<Soul>();
        this.waypointNetwork = new HashSet<Waypoint>();
    }

    public boolean bindSoul(Waypoint waypoint) {
        if (!isActive(waypoint)) {
            throw new IllegalStateException("A Waypoint must be activated before a Soul can be bound to it.");
        }
        if (!waypoint.isPowered()) {
            throw new IllegalStateException("A Waypoint must be powered for a Soul to be bound to it.");
        }
        if (!waypoint.getAlignment().equals(this.alignment)) {
            /*this.clearNetwork();
            this.alignment = waypoint.getAlignment();*/
            throw new IllegalStateException("Only Waypoints of the same alignment can be bound at the same time.");
        }
        Soul unboundSoul = this.getNextUnboundSoul();
        if (unboundSoul != null) {
            unboundSoul.bind(waypoint);
            return true;
        } else {
            return false;
        }
    }

    public boolean unbindSoul(Waypoint waypoint) {
        Iterator<Soul> boundSouls = this.getBoundSouls(waypoint).iterator();
        if (!boundSouls.hasNext()) {
            boundSouls.next().unbind();
            return true;
        } else {
            return false;
        }
    }

    public void activate(Waypoint waypoint) throws IllegalStateException {
        if (!waypoint.isPowered()) {
            throw new IllegalStateException("A Waypoint must be powered to be activated.");
        }
        if (!this.isAlignedTo(waypoint)) {
            throw new IllegalStateException("Only Waypoints with the same alignment can be activated at the same time.");
        }
        this.waypointNetwork.add(waypoint);
    }

    public void forceActivate(Waypoint waypoint) {
        if (!waypoint.isPowered()) {
            throw new IllegalStateException("A Waypoint must be powered to be activated.");
        }
        if (!this.isAlignedTo(waypoint)) {
            this.clearNetwork();
        }
        this.waypointNetwork.add(waypoint);
    }

    public void waypointTransfer(Waypoint start, Waypoint end) throws IllegalStateException {
        if (!isActive(start) || !isActive(end)) {
            throw new IllegalStateException("A Waypoint transfer can only occur between two activated Waypoints");
        }
        if (!start.isPowered() || !end.isPowered()) {
            throw new IllegalStateException("A Waypoint transfer can only occur between two powered Waypoints");
        }
        if (!start.contains(this.player.getLocation())) {
            throw new IllegalStateException("The Player is too far from the start Waypoint to Waypoint transfer.");
        }
        Soul unboundSoul = this.getNextUnboundSoul();
        if (unboundSoul == null) {
            throw new IllegalStateException("An unbound Soul is required to complete a Waypoint transfer.");
        }
        this.player.teleport(end.getTeleportLocation());
        unboundSoul.exhaust();
    }

    public void soulTeleport(Waypoint waypoint) {
        Collection<Soul> boundSouls = this.getBoundSouls(waypoint);
        if (boundSouls.isEmpty()) {
            throw new IllegalStateException("There must be at least one bound Soul to a Waypoint in order to Soul Teleport to it.");
        }
        this.player.teleport(waypoint.getTeleportLocation());
        boundSouls.iterator().next().unbind();
    }

    public void regenerate(){
        for (Soul soul : this.souls) {
            if (soul.isExhausted()) {
                soul.regenerate();
                break;
            }
        }
    }

    private boolean isAlignedTo(Waypoint waypoint) {
        Iterator<Waypoint> iterator = this.waypointNetwork.iterator();
        return !iterator.hasNext() || waypoint.getAlignment().equals(iterator.next().getAlignment());
    }

    private boolean isActive(Waypoint waypoint) {
        return this.waypointNetwork.contains(waypoint);
    }

    public boolean isSoulBoundTo(Waypoint waypoint) {
        return !getBoundSouls(waypoint).isEmpty();
    }

    private Collection<Soul> getBoundSouls(Waypoint waypoint) {
        Collection<Soul> boundSouls = new ArrayList<Soul>();
        for (Soul soul : this.souls) {
            if (soul.isBoundTo(waypoint)) {
                boundSouls.add(soul);
            }
        }
        return boundSouls;
    }

    /**
     * Get the next available soul that is not exhausted, and is not already bound.
     * If there are no existing unbound souls and there are less souls than the
     * current soul stength, a new soul will be created.
     * Otherwise, null will be returned.
     * @return and unbound soul, or null if none exist or can be added.
     */
    private Soul getNextUnboundSoul() {
        for (Soul soul : this.souls) {
            if (soul.isUnbounded()) {
                return soul;
            }
        }
        if (this.souls.size() < this.soulStrength) {
            Soul soul = new Soul();
            souls.add(soul);
            return soul;
        } else {
            return null;
        }
    }

    private void clearNetwork() {
        this.waypointNetwork.clear();
    }
}
