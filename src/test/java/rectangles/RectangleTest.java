package rectangles;

import java.util.List;
import java.util.Objects;

import static rectangles.Rectangle.Adjacency.*;

public final class RectangleTest {
    private static final Rectangle BASE = new Rectangle(0, 0, 4, 4);

    public static void main(String[] args) {
        rejectsInvalidBounds();
        detectsBoundaryIntersections();
        detectsContainment();
        classifiesAdjacency();
        supportsFullIntegerRange();
        System.out.println("All 5 rectangle test groups passed.");
    }

    private static void rejectsInvalidBounds() {
        expectInvalid(() -> new Rectangle(0, 0, 0, 4));
        expectInvalid(() -> new Rectangle(0, 0, 4, 0));
        expectInvalid(() -> new Rectangle(4, 0, 0, 4));
        expectInvalid(() -> new Rectangle(0, 4, 4, 0));
    }

    private static void detectsBoundaryIntersections() {
        expectIntersection("disjoint", new Rectangle(5, 1, 7, 3));
        expectIntersection("strictly inside", new Rectangle(1, 1, 3, 3));
        expectIntersection("two crossings", new Rectangle(2, -1, 3, 2),
            new Point(2, 0), new Point(3, 0));
        expectIntersection("four crossings", new Rectangle(1, -1, 3, 5),
            new Point(1, 0), new Point(1, 4), new Point(3, 0), new Point(3, 4));
        expectIntersection("corner contact", new Rectangle(4, 4, 6, 6), new Point(4, 4));
        expectIntersection("fully shared shorter side", new Rectangle(4, 1, 6, 3),
            new Point(4, 1), new Point(4, 3));
        expectIntersection("partial shared side", new Rectangle(4, 2, 6, 6),
            new Point(4, 2), new Point(4, 4));
        expectIntersection("contained with shared side", new Rectangle(1, 0, 3, 2),
            new Point(1, 0), new Point(3, 0));
        expectIntersection("identical", new Rectangle(0, 0, 4, 4),
            new Point(0, 0), new Point(0, 4), new Point(4, 0), new Point(4, 4));
    }

    private static void detectsContainment() {
        Rectangle inner = new Rectangle(1, 1, 3, 3);
        expect("contains inner", true, BASE.contains(inner));
        expect("containment is directional", false, inner.contains(BASE));
        expect("contains with shared boundary", true, BASE.contains(new Rectangle(1, 0, 3, 2)));
        expect("contains equal rectangle", true, BASE.contains(new Rectangle(0, 0, 4, 4)));
        expect("overlap without containment", false, BASE.contains(new Rectangle(2, -1, 3, 2)));
        expect("disjoint containment", false, BASE.contains(new Rectangle(5, 1, 7, 3)));
    }

    private static void classifiesAdjacency() {
        expectAdjacency(PROPER, new Rectangle(4, 0, 6, 4));
        expectAdjacency(PROPER, new Rectangle(0, -2, 4, 0));
        expectAdjacency(SUB_LINE, new Rectangle(4, 1, 6, 3));
        expectAdjacency(SUB_LINE, new Rectangle(-2, -1, 0, 5));
        expectAdjacency(SUB_LINE, new Rectangle(0, 4, 2, 6));
        expectAdjacency(PARTIAL, new Rectangle(2, 4, 6, 6));
        expectAdjacency(NONE, new Rectangle(4, 4, 6, 6));
        expectAdjacency(NONE, new Rectangle(4, 5, 6, 7));
        expectAdjacency(NONE, new Rectangle(1, 0, 3, 2));
        expectAdjacency(NONE, new Rectangle(2, 0, 6, 4));
        expectAdjacency(NONE, new Rectangle(0, 0, 4, 4));
    }

    private static void supportsFullIntegerRange() {
        Rectangle left = new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE);
        Rectangle right = new Rectangle(0, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        expect("extreme coordinates adjacency", PROPER, left.adjacency(right));
        expect("extreme coordinates intersection",
            List.of(new Point(0, Integer.MIN_VALUE), new Point(0, Integer.MAX_VALUE)),
            left.intersectionPoints(right));
    }

    private static void expectIntersection(String scenario, Rectangle other, Point... points) {
        List<Point> expected = List.of(points);
        expect(scenario, expected, BASE.intersectionPoints(other));
        expect(scenario + " (reversed)", expected, other.intersectionPoints(BASE));
    }

    private static void expectAdjacency(Rectangle.Adjacency expected, Rectangle other) {
        expect("adjacency with " + other, expected, BASE.adjacency(other));
        expect("reversed adjacency with " + other, expected, other.adjacency(BASE));
    }

    private static void expectInvalid(Runnable construction) {
        try {
            construction.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Invalid rectangle was accepted");
    }

    private static void expect(String scenario, Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(scenario + ": expected " + expected + ", got " + actual);
        }
    }
}
