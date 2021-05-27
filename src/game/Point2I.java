package game;

import java.util.Objects;

public class Point2I {
    public int x;
    public int y;

    public Point2I(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2I)) return false;
        Point2I point2I = (Point2I) o;
        return x == point2I.x && y == point2I.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
