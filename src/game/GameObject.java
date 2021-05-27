package game;

import java.awt.*;
import java.io.Serializable;

abstract public class GameObject implements Serializable {
    protected final long id = (long) (Math.random() * Long.MAX_VALUE);
    protected float x = 0;
    protected float y = 0;
    protected int z = 0;
    protected float sx = 0;
    protected float sy = 0;
    protected float size = 32;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void step(float delhaTime) {
        x += sx;
        y += sy;
    }

    public Rectangle asRectangle() {
        return new Rectangle((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);
    }

    public boolean checkCollision(GameObject other) {

        var r1 = asRectangle();
        var r2 = other.asRectangle();
        return r1.intersects(r2);

    }

    public long getID() {
        return id;
    }


    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void onCollision(GameObject other, Game game) {

    }

    public float getSize() {
        return size;
    }
}
