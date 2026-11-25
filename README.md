# Rectangles

A Java implementation for determining the geometric relationships
between two rectangles: intersection, containment, and adjacency.

## Requirements

- JDK 17 or later with both `java` and `javac` on your PATH.
- A POSIX-compatible shell, such as Bash or Zsh, for the commands below.
- The demo and tests use only the Java standard library.

The demo and tests were verified on Linux with JDK 17.

Check your JDK before building.

```sh
java -version
javac -version
```

## Run the Demo

From the repository root, compile and run the demo.

```sh
mkdir -p build && \
javac --release 17 -d build src/main/java/rectangles/*.java && \
java -cp build rectangles.RectangleDemo
```

The demo uses two rectangles that share part of a side. The shared
segment runs from `(4, 1)` to `(4, 3)`, neither rectangle contains the
other, and the adjacency is `SUB_LINE`.

## Run the Tests

Run the unit tests from the repository root. This command also compiles
the main classes, so it works on a fresh checkout.

```sh
mkdir -p build && \
javac --release 17 -d build src/main/java/rectangles/*.java src/test/java/rectangles/*.java && \
java -cp build rectangles.RectangleTest
```

On success, the tests print `All 5 rectangle test groups passed.`

Unit tests cover these scenarios.

- Valid and invalid rectangle coordinates
- Negative coordinates
- Intersecting and non-intersecting rectangles
- Containment
- Proper, sub-line, and partial adjacency
- Identical rectangles
- Corner contact
- Shared boundaries

## Assumptions

- Rectangles are axis-aligned within a 2D Cartesian coordinate system.
- Coordinates are integers and can be negative.
- A rectangle must have a positive width and height.
- Rotated rectangles are not supported.
- Rectangle and Point objects are immutable.

## Design

### Rectangle

My initial approach was to represent a rectangle using four points:
upper left, upper right, lower left, and lower right.

During a design discussion with ChatGPT, an alternative approach
was suggested: representing an axis-aligned rectangle using only
two diagonally opposite vertices.

After considering both approaches, I decided to use the second
approach because storing all four vertices introduces redundant
data and requires additional validation to ensure that the four
points form a valid rectangle.

The Rectangle class stores the following four values:

- `minX`
- `minY`
- `maxX`
- `maxY`

All four vertices can be derived as needed:

| Vertex      | Coordinates  |
|-------------|--------------|
| Upper left  | (minX, maxY) |
| Upper right | (maxX, maxY) |
| Lower left  | (minX, minY) |
| Lower right | (maxX, minY) |

This approach simplifies the data model, input validation,
and the calculations required for the geometric operations.

The constructor validates that `minX < maxX` and `minY < maxY`
to prevent the creation of invalid rectangles.

### Point

A Point represents an individual (x, y) coordinate. This class
is primarily used to represent the intersection points between
two rectangles.

## Operations

### Intersection

`first.intersectionPoints(second)` returns the distinct points where
the boundaries meet, sorted by x and then y.
A shared segment is represented by its two endpoints. A corner touch
returns one point. Strict nesting returns an empty list.

### Containment

`first.contains(second)` checks whether the first rectangle wholly
contains the second. A rectangle may touch the containing rectangle’s
boundary, and equal rectangles contain each other.

### Adjacency

`first.adjacency(second)` checks for a shared side segment of positive
length with the rectangle interiors on opposite sides of that segment.
It returns one of four values.

- `PROPER` means both sides match completely.
- `SUB_LINE` means one side is completely contained within a longer side.
- `PARTIAL` means the sides overlap along a segment shorter than either side.
- `NONE` means there is no qualifying shared segment. Corner-only contact
  and rectangles with overlapping interiors fall into this category.

## AI Usage

ChatGPT was used as a collaborative tool during the development
of this exercise, primarily to discuss design alternatives,
identify potential edge cases, and assist with documentation.

One example is the decision to represent a rectangle using two
diagonally opposite vertices instead of explicitly storing all
four vertices, as described in the Design section.
