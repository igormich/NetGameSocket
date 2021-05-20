import java.util.LinkedHashSet;

public class Player extends GameObject {

    volatile private float speed = 100f;
    volatile private float shotDelay = 0;
    volatile private float shotSpeed = 0.5f;
    transient private LinkedHashSet<MoveDirections> lastMoveKeys = new LinkedHashSet<>();
    transient private boolean shot = false;
    transient private final Game game;
    volatile private MoveDirections direction = MoveDirections.UP;

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
        lastMoveKeys.stream().skip(Math.max(0,lastMoveKeys.size() - 1)).findFirst().ifPresent(dir -> {
            direction = dir;
            switch (dir) {
                case UP -> y -= delhaTime * speed;
                case DOWN -> y += delhaTime * speed;
                case LEFT -> x -= delhaTime * speed;
                case RIGHT -> x += delhaTime * speed;
            }
        });
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
}
