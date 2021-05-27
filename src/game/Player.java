package game;

import java.util.LinkedHashSet;

public class Player extends GameObject {

    private float speed = 100f;
    private float shotDelay = 0;
    private float shotSpeed = 0.5f;
    transient private LinkedHashSet<MoveDirections> lastMoveKeys = new LinkedHashSet<>();
    transient private boolean shot = false;
    transient private final Game game;
    private MoveDirections direction = MoveDirections.UP;

    public Player(Game game) {
        this.game = game;
    }

    public void addMoveKey(MoveDirections moveDirections) {
        lastMoveKeys.add(moveDirections);
    }

    public void removeMoveKey(MoveDirections moveDirections) {
        lastMoveKeys.remove(moveDirections);
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    @Override
    public void step(float delhaTime) {
        shotDelay -= delhaTime;

        var p = new Point2F(x, y);
        lastMoveKeys.stream().skip(Math.max(0, lastMoveKeys.size() - 1)).findFirst().ifPresent(dir -> {
            direction = dir;
            switch (dir) {
                case UP -> p.y = y - delhaTime * speed;
                case DOWN -> p.y = y + delhaTime * speed;
                case LEFT -> p.x = x - delhaTime * speed;
                case RIGHT -> p.x = x + delhaTime * speed;
            }
        });
        if (game.canMoveTo(this, p)) {
            x = p.x;
            y = p.y;
        }
        if (shot && (shotDelay < 0)) {
            shotDelay = shotSpeed;
            var sx = 10 * direction.getX();
            var sy = 10 * direction.getY();
            game.add(new Bullet(x + sx, y + sy, sx, sy, id));
        }
    }

    public MoveDirections getDirection() {
        return direction;
    }

    public void onCollision(GameObject other, Game game) {
        if (other instanceof Bullet b && b.getOwnerID() != id) {
            game.remove(this);
        }
    }
}
