/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.*;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        for (int i = 1; i <= 4; i++) {
            turtle.forward(sideLength);
            turtle.turn(90);
        }
//        throw new RuntimeException("implement me!");
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        // （n － 2）×180°
        return 180.0 * (sides - 2) / sides;

//        throw new RuntimeException("implement me!");
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        // angle * sides = 180 * (sides - 2) => angle * sides = 180sides - 360 => (180-angle)sides = 360 => sides = 360/(180-angle)
        return (int)Math.round(360 / (180 - angle));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        final double turnAngle = 180 - calculateRegularPolygonAngle(sides);
        for (int i = 0; i < sides; i++) {
            turtle.forward(sideLength);
            turtle.turn(turnAngle);
        }
//        throw new RuntimeException("implement me!");
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
        int deltaY = currentY - targetY, deltaX = currentX - targetX;
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

        double rot = (-angle + 90 - currentBearing + 180) % 360;

        if (rot < 0) rot = 360 + rot;

        return rot;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
        int size = xCoords.size();
        if (size == 0 || size == 1) return new ArrayList<>();

//        if (xCoords.size() != yCoords) gg;

        int currentX = xCoords.get(0);
        int currentY = yCoords.get(0);
        double currentBearing = 0;

        List<Double> results = new ArrayList<>();

        for (int i = 1; i < size; i++) {
            int targetX = xCoords.get(i);
            int targetY = yCoords.get(i);
            currentBearing = calculateBearingToPoint(currentBearing, currentX, currentY, targetX, targetY);
            results.add(currentBearing);
            currentX = targetX;
            currentY = targetY;
        }

        return results;

//        throw new RuntimeException("implement me!");
    }

    private static double calculateBearingToPoint(double currentBearing, Point fromPoint, Point toPoint) {
        return calculateBearingToPoint(currentBearing, (int)fromPoint.x(), (int)fromPoint.y(), (int)toPoint.x(), (int)toPoint.y());
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        drawRegularPolygon(turtle, 5, 10);
    }

    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and
     * there are other algorithms too.
     *
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {

        // 空或少于等于3个点时
        if (points == null || points.size() <= 3) {
            return points;
        }

        Set<Point> result = new HashSet<>();

        // 多余三个点时
//        Point[] pointsArray = (Point[]) points.toArray();   // 转换为数组
        int length = points.size();

        // 寻找最左下角的点
        Iterator<Point> it = points.iterator();

        Point minPoint = it.next();

        while (it.hasNext()) {
            Point c = it.next();
            if (minPoint.x() > c.x()) {   // 存在x更小的点，需要更新最小点
                minPoint = c;
            } else if (minPoint.x() == c.x()) {
                if (minPoint.y() > c.y()) {    // 存在x相同，y更小的点
                    minPoint = c;
                }
            }
        }

        // 加入最小点到result
        result.add(minPoint);

        Point lastInSet = null, currentInSet, nextInSet;  // 记录上个点和当前点和下个点
//        assert minPoint != null;
        nextInSet = currentInSet = minPoint;

        do {
            boolean hasRightPoint = false;
            double maxAngle = -1;

            for (Point tryingPoint : points) {
                if (tryingPoint != currentInSet) {
                    // 不和上一个点相同时才计算
                    boolean isRightPoint = tryingPoint.x() - currentInSet.x() >= 0;
                    double currentAngle;
                    if (hasRightPoint) {
                        // 有右边的点，不需要管左边的点
                        if (isRightPoint) {
                            currentAngle = calculateBearingToPoint(0, currentInSet, tryingPoint);
                            if (currentAngle >= maxAngle) {
                                maxAngle = currentAngle;
                                nextInSet = tryingPoint;    // 当前点可能成为下一个凸包的点
                            }
                        }
                    } else {
                        // 没有右边的点
                        if (isRightPoint) {
                            // 这个点是第一次出现在右边的点
                            maxAngle = currentAngle = calculateBearingToPoint(0, currentInSet, tryingPoint);
                            hasRightPoint = true;
                        } else {
                            currentAngle = calculateBearingToPoint(180, currentInSet, tryingPoint);
                        }
                        if (currentAngle >= maxAngle) {
                            maxAngle = currentAngle;
                            nextInSet = tryingPoint;    // 当前点可能成为下一个凸包的点
                        }
                    }
                }
            }
            // 通过最大角度将新的点加入凸包
            result.add(nextInSet);

            // 如果此时last不是null，并且last current next的横坐标或纵坐标是重合的，则这个current点应该被移除
            if (lastInSet != null) {    // last不是null
//                if ((lastInSet.x() == currentInSet.x() && currentInSet.x() == nextInSet.x()) ||
//                        (lastInSet.y() == currentInSet.y() && currentInSet.y() == nextInSet.y())) {
                if (calculateBearingToPoint(0, currentInSet, nextInSet) == calculateBearingToPoint(0, lastInSet, currentInSet)) {
                    // 如果三个点在一条直线上就删掉中间的点
                    result.remove(currentInSet);
                }
            }

            // 此时current变成last，next变成current
            lastInSet = currentInSet;
            currentInSet = nextInSet;
        } while (nextInSet != minPoint);

//        System.out.println("result: ");
//        printPointSet(result);
        return result;
    }

    public static void printPointSet(Set<Point> points) {
        for (Point point : points) {
            System.out.println("(" + point.x() + "," + point.y() +")");
        }
        System.out.println("---------------");
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        DrawableTurtle turtle = new DrawableTurtle();

//        drawSquare(turtle, 40);

//        drawPersonalArt(turtle);

        // draw the window
//        turtle.draw();

    }

}
