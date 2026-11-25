# Rectangles

A Java implementation for determining the geometric relationships
between two rectangles: intersection, containment, and adjacency.

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

Determines whether the boundaries of two rectangles intersect
and returns their intersection points.

### Containment

Determines whether one rectangle is completely contained within
another by comparing their coordinate boundaries.

### Adjacency

Determines whether two rectangles share a side and classifies
the relationship as:

- **Proper:** Both sides match completely.
- **Sub-line:** One side is completely contained within a longer side.
- **Partial:** Two sides partially overlap, but neither fully
  contains the other.
- **None:** No shared line segment of positive length.

## Testing

Unit tests will cover the following scenarios:

- Valid and invalid rectangle coordinates
- Negative coordinates
- Intersecting and non-intersecting rectangles
- Containment
- Proper, sub-line, and partial adjacency
- Identical rectangles
- Corner contact
- Shared boundaries

## Build and Run

TBD.

## AI Usage

ChatGPT was used as a collaborative tool during the development
of this exercise, primarily to discuss design alternatives,
identify potential edge cases, and assist with documentation.

One example is the decision to represent a rectangle using two
diagonally opposite vertices instead of explicitly storing all
four vertices, as described in the Design section.