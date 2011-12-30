import com.sun.deploy.association.utility.WinAppAssociationReader;

public class Soul {
    private Waypoint boundWaypoint;
    //private Wayfarer wayfarer;
    private boolean exhausted;

    public Soul() {
        //this.wayfarer = wayfarer;
        this.boundWaypoint = null;
        this.exhausted = false;
    }

    public void bind(Waypoint waypoint) {
        if (waypoint == null) {
            throw new IllegalArgumentException("You cannot bind a Soul to a null Waypoint");
        }
        if (this.exhausted) {
            throw new IllegalStateException("You cannot bind an exhausted Soul.");
        }
        if (waypoint.equals(this.boundWaypoint)) {
            return;
        }
        this.boundWaypoint = waypoint;
    }

    void unbind() {
        this.boundWaypoint = null;
        this.exhausted = true;
    }

    public boolean isBound() {
        return this.boundWaypoint != null;
    }

    public boolean isUnbounded() {
        return !this.exhausted && (this.boundWaypoint == null);
    }

    public boolean isExhausted() {
        return this.exhausted;
    }

    public boolean isBoundTo(Waypoint waypoint) {
        return waypoint.equals(this.boundWaypoint);
    }

    public void exhaust() {
        this.exhausted = true;
    }

    public void regenerate() {
        this.exhausted = false;
    }
}
