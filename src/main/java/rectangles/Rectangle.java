package rectangles;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;

/** An immutable axis-aligned rectangle with positive width and height. */
public record Rectangle(int minX, int minY, int maxX, int maxY) {
    public enum Adjacency {
        NONE, PROPER, SUB_LINE, PARTIAL
    }

    public Rectangle {
        if (minX >= maxX || minY >= maxY) {
            throw new IllegalArgumentException("Rectangle must have positive width and height");
        }
    }

    /** Returns true if this rectangle contains the other rectangle, including its boundary. */
    public boolean contains(Rectangle other) {
        return minX <= other.minX && maxX >= other.maxX
            && minY <= other.minY && maxY >= other.maxY;
    }

    /**
     * Returns distinct shared boundary points sorted by x and then y.
     * A shared segment is represented by its two endpoints.
     */
    public List<Point> intersectionPoints(Rectangle other) {
        Set<Point> points = new TreeSet<>(Comparator.comparingInt(Point::x)
            .thenComparingInt(Point::y));
        List<Edge> otherEdges = other.edges();
        for (Edge edge : edges()) {
            for (Edge otherEdge : otherEdges) {
                edge.addIntersection(otherEdge, points);
            }
        }
        return List.copyOf(points);
    }

    /** Classifies a shared side segment between rectangles whose interiors do not overlap. */
    public Adjacency adjacency(Rectangle other) {
        if (maxX == other.minX || minX == other.maxX) {
            return classifyOverlap(minY, maxY, other.minY, other.maxY);
        }
        if (maxY == other.minY || minY == other.maxY) {
            return classifyOverlap(minX, maxX, other.minX, other.maxX);
        }
        return Adjacency.NONE;
    }

    private static Adjacency classifyOverlap(int start, int end, int otherStart, int otherEnd) {
        if (Math.max(start, otherStart) >= Math.min(end, otherEnd)) {
            return Adjacency.NONE;
        }
        if (start == otherStart && end == otherEnd) {
            return Adjacency.PROPER;
        }
        if ((start <= otherStart && end >= otherEnd)
                || (otherStart <= start && otherEnd >= end)) {
            return Adjacency.SUB_LINE;
        }
        return Adjacency.PARTIAL;
    }

    private List<Edge> edges() {
        return List.of(
            new Edge(true, minY, minX, maxX),
            new Edge(true, maxY, minX, maxX),
            new Edge(false, minX, minY, maxY),
            new Edge(false, maxX, minY, maxY)
        );
    }

    private record Edge(boolean horizontal, int fixed, int start, int end) {
        private void addIntersection(Edge other, Set<Point> points) {
            if (horizontal != other.horizontal) {
                Edge horizontalEdge = horizontal ? this : other;
                Edge verticalEdge = horizontal ? other : this;
                if (horizontalEdge.spans(verticalEdge.fixed) && verticalEdge.spans(horizontalEdge.fixed)) {
                    points.add(new Point(verticalEdge.fixed, horizontalEdge.fixed));
                }
            } else if (fixed == other.fixed) {
                int overlapStart = Math.max(start, other.start);
                int overlapEnd = Math.min(end, other.end);
                if (overlapStart <= overlapEnd) {
                    points.add(pointAt(overlapStart));
                    points.add(pointAt(overlapEnd));
                }
            }
        }

        private boolean spans(int coordinate) {
            return start <= coordinate && coordinate <= end;
        }

        private Point pointAt(int coordinate) {
            return horizontal ? new Point(coordinate, fixed) : new Point(fixed, coordinate);
        }
    }
}
