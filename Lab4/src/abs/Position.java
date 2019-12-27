package abs;

public class Position {

    private final int level;

    private final double RADIUS;

    private final double ANGLE;

    /**
     * constructor for abs.Position
     * @param radius    radius from center object
     * @param angle     θ angle from 0º
     */
    public Position(int level, double radius, double angle) {
        this.level = level;
        this.RADIUS = radius;
        this.ANGLE = angle;
    }

    /**
     * for copy the object
     * @param old   old object
     */
    public Position(Position old) {
        this.level = old.level;
        this.RADIUS = old.RADIUS;
        this.ANGLE = old.ANGLE;
    }

    /**
     *
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * get radius
     * @return  radius of this pos
     */
    public double getRadius() {
        return RADIUS;
    }

    /**
     * get angle
     * @return  angle of this pos
     */
    public double getAngle() {
        return ANGLE;
    }


}
