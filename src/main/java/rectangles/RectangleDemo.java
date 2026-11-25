package rectangles;

public final class RectangleDemo {
    public static void main(String[] args) {
        Rectangle first = new Rectangle(0, 0, 4, 4);
        Rectangle second = new Rectangle(4, 1, 6, 3);

        System.out.println("Boundary intersection points: " + first.intersectionPoints(second));
        System.out.println("First contains second: " + first.contains(second));
        System.out.println("Adjacency: " + first.adjacency(second));
    }
}
