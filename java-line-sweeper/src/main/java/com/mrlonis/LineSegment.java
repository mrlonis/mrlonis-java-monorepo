package com.mrlonis;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serial;

/**
 * [read-only]
 * <p>
 * This class represents a single LineSegment, composed from two endpoints. This class is used by the GUI as the user is
 * drawing the line. When that's happening, the LeftEndpoint is fixed, but the RightEndpoint is still changing. Methods
 * are provided to draw the line segment using a graphics context, with or without details and highlighting. Methods are
 * provided to test for intersection with another line segment or a point.
 * <p>
 * Importantly, the lessThan() predicate allows us to determine the relative order of this line segment and some other
 * line segment according to the y-intercepts with the sweep line. This is the main computation performed by the
 * relation on which Sweeper maintains its tree.
 */

public class LineSegment {
    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final Color HIGHLIGHT_COLOR = new Color(0xee7600);
    public static final Color LEFT_COLOR = new Color(0x339900);
    public static final Color RIGHT_COLOR = Color.RED;
    public static final int EPSILON = 3;
    public static final int LDELTA = 80;
    public static final int RDELTA = 10;

    protected Point2D p1, p2;
    protected Color color;
    private boolean fixed;

    /**
     * Constructs a zero-length line segment from p1 to p1.
     */
    public LineSegment(Point2D p1) {
        this.p1 = p1;
        this.color = DEFAULT_COLOR;
        fixed = false;
    }

    /**
     * Constructs a line segments with endpoints p1 and p2.
     */
    public LineSegment(Point2D p1, Point2D p2) {
        this(p1);
        setP2(p2);
    }

    /**
     * Sets the second endpoint of this line segment to p2.
     */
    public void setP2(Point2D p2) {
        if ((int) p1.getX() == (int) p2.getX())
        // nudge vertical lines
        {
            p2 = new Point2D.Double(p2.getX() + 1, p2.getY());
        }
        this.p2 = p2;
    }

    /**
     * Marks this line segment as fully constructed.
     */
    public void fix() {
        fixed = true;
    }

    /**
     * Returns true iff this line segment is specified by a single endpoint.
     */
    public boolean isZeroLength() {
        return p2 == null;
    }

    /**
     * Returns the left endpoint associated with this line segment.
     */
    public Endpoint getLeftEndpoint() {
        if (isZeroLength() || p1.getX() < p2.getX()) {
            return new LeftEndpoint(p1);
        }
        return new LeftEndpoint(p2);
    }

    /**
     * Returns the right endpoint associated with this line segment.
     */
    public Endpoint getRightEndpoint() {
        if (isZeroLength() || p1.getX() > p2.getX()) {
            return new RightEndpoint(p1);
        }
        return new RightEndpoint(p2);
    }

    /**
     * Turns on the highlight for this line segment. Causes the line to appear in a distinctive color when drawn by the
     * GUI.
     */
    public void highlight() {
        color = HIGHLIGHT_COLOR;
    }

    /**
     * Turns off the highlight for this line segment.
     */
    public void unhighlight() {
        color = DEFAULT_COLOR;
    }

    /**
     * Returns the color this line should be drawn in.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns true iff this line segment intersects with that one.
     */
    public boolean intersects(LineSegment that) {
        return new Line2D.Double(p1, p2).intersectsLine(new Line2D.Double(that.p1, that.p2));
    }

    /**
     * Returns true iff this line segments intersects with that point.
     */
    public boolean intersects(Point2D that) {
        return new Line2D.Double(p1, p2).intersects(
                new Rectangle2D.Double(that.getX() - EPSILON, that.getY() - EPSILON, 2 * EPSILON, 2 * EPSILON));
    }

    /**
     * Returns true iff this line segment is "less than" that line segment along the sweep line. That happens when this
     * segment's y-intercept along the sweep line is less than the corresponding y-intercept for that segment.
     */
    public boolean lessThan(LineSegment that, int sweepX) {
        Point2D left1 = this.getLeftEndpoint();
        Point2D right1 = this.getRightEndpoint();
        Point2D left2 = that.getLeftEndpoint();
        Point2D right2 = that.getRightEndpoint();
        double m1 = (right1.getY() - left1.getY()) / (right1.getX() - left1.getX());
        double m2 = (right2.getY() - left2.getY()) / (right2.getX() - left2.getX());
        double y1 = left1.getY() + m1 * (sweepX - left1.getX());
        double y2 = left2.getY() + m2 * (sweepX - left2.getX());
        return y1 < y2;
    }

    /**
     * Returns a textual representation of this line segment.
     */
    public String toString() {
        return "[" + getLeftEndpoint() + ", " + getRightEndpoint() + "]";
    }

    /**
     * Return true iff this line segment is the same as the other one.
     */
    public boolean equals(Object other) {
        if (other instanceof LineSegment that) {
            return this.p1.equals(that.p1) && this.p2.equals(that.p2);
        }
        return false;
    }

    /**
     * Uses the provided graphics context to draw this line segment, with or without the details (as determined by the
     * argument).
     */
    public void draw(Graphics2D g2d, boolean showDetails) {
        if (p2 != null) {
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.setColor(getColor());
            g2d.draw(new Line2D.Double(p1, p2));
            Point2D left = getLeftEndpoint(), right = getRightEndpoint();
            if (showDetails) {
                if (!fixed) {
                    g2d.setColor(LEFT_COLOR);
                    g2d.drawString(left.toString(), Math.max((int) left.getX() - LDELTA, 0), (int) left.getY());
                    g2d.setColor(RIGHT_COLOR);
                    g2d.drawString(right.toString(), (int) right.getX() + RDELTA, (int) right.getY());
                } else {
                    final int RADIUS = 5;
                    int diameter = 2 * RADIUS;
                    g2d.setColor(LEFT_COLOR);
                    g2d.fillOval((int) left.getX() - RADIUS, (int) left.getY() - RADIUS, diameter, diameter);
                    g2d.setColor(RIGHT_COLOR);
                    g2d.fillOval((int) right.getX() - RADIUS, (int) right.getY() - RADIUS, diameter, diameter);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval((int) left.getX() - RADIUS, (int) left.getY() - RADIUS, diameter, diameter);
                    g2d.drawOval((int) right.getX() - RADIUS, (int) right.getY() - RADIUS, diameter, diameter);
                }
            }
        }
    }
}

/**
 * SweepLine represents a special kind of line segment, namely the segment describing the sweep line. The endpoints
 * extend from the top border of the window to the bottom border, and it's completely vertical. It is drawn as a dashed
 * gray line.
 */

class SweepLine extends LineSegment {
    private boolean error = false;

    public SweepLine(int x) {
        super(new Point2D.Double(x, 0), new Point2D.Double(x, Constants.SURFACE_HEIGHT));
        color = Color.LIGHT_GRAY;
    }

    public void highlight() {
        color = Color.PINK;
        error = true;
    }

    public void draw(Graphics2D g2d, boolean showDetails) {
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.setColor(getColor());
        g2d.draw(new Line2D.Double(p1, p2));
        if (error) {
            int x = (int) p2.getX() + 5, y = (int) p2.getY() - 10;
            while (y > 10) {
                g2d.drawString("Error", x, y);
                y -= 20;
            }
        }
    }
}

/**
 * Endpoint is parent to LeftEndpoint and RightEndpoint. It provides sensible definitions of equals(), toString(), and
 * compareTo().
 */
abstract class Endpoint extends Point2D.Double implements Comparable<Endpoint> {
    /**
     * Added default serial ID to remove Eclipse warning.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public Endpoint(Point2D point) {
        super(point.getX(), point.getY());
    }

    public int compareTo(Endpoint p2) {
        Endpoint p1 = this;
        if (p1.getX() < p2.getX()) {
            return -1;
        } else if (p1.getX() == p2.getX()) {
            if (p1.onLeft() && p2.onRight()) {
                return -1;
            } else if (p1.onRight() && p2.onLeft()) {
                return 1;
            } else // on the same side
                return java.lang.Double.compare(p1.getY(), p2.getY());
        } else {
            return 1;
        }
    }

    public abstract boolean onLeft();

    public boolean onRight() {
        return !onLeft();
    }

    public boolean equal(Object other) {
        if (other instanceof Endpoint that) {
            return (int) getX() == (int) that.getX() && (int) getY() == (int) that.getY();
        }
        return false;
    }

    public String toString() {
        return (onLeft() ? "L(" : "R(") + (int) getX() + ", " + (int) getY() + ")";
    }
}

/**
 * LeftEndpoint is an Endpoint that is known to be on the left side of a line segment.
 */
class LeftEndpoint extends Endpoint {
    /**
     * Added default serial ID to remove Eclipse warning.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public LeftEndpoint(Point2D point) {
        super(point);
    }

    public boolean onLeft() {
        return true;
    }
}

/**
 * RightEndpoint is an Endpoint that is known to be on the right side of a line segment.
 */
class RightEndpoint extends Endpoint {
    /**
     * Added default serial ID to remove Eclipse warning.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public RightEndpoint(Point2D point) {
        super(point);
    }

    public boolean onLeft() {
        return false;
    }
}
