package abs;

public class Position {
    /**
     * level.
     */
    private final int level;

    /**
     * radius.
     */
    private final double radius;

    /**
     * angle.
     */
    private final double angle;

    /**
     * constructor for Position.
     *
     * @param level  level
     * @param radius radius from center object
     * @param angle  θ angle from 0º
     */
    public Position(final int level, final double radius, final double angle) {
        this.level = level;
        this.radius = radius;
        this.angle = angle;
    }

    /**
     * get level.
     *
     * @return level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * get radius.
     *
     * @return radius of this pos
     */
    public double getRadius() {
        return radius;
    }

    /**
     * get angle.
     *
     * @return angle of this pos
     */
    public double getAngle() {
        return angle;
    }


}
