package game;

import java.util.Objects;

public class Point2F {
    public float x;
    public float y;

    public Point2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2F)) return false;
        Point2F point2F = (Point2F) o;
        return Float.compare(point2F.x, x) == 0 && Float.compare(point2F.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
