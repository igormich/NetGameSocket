package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    private static final int UP = KeyEvent.VK_W;
    private static final int DOWN = KeyEvent.VK_S;
    private static final int LEFT = KeyEvent.VK_A;
    private static final int RIGHT = KeyEvent.VK_D;
    private static final int SHOT = KeyEvent.VK_SPACE;
    private static final Map<Integer, MoveDirections> moveKeys = Map.of(
            UP, MoveDirections.UP,
            DOWN, MoveDirections.DOWN,
            LEFT, MoveDirections.LEFT,
            RIGHT, MoveDirections.RIGHT
    );


    private final List<GameObject> allObjects = new ArrayList();
    private List<GameObject> addObjects = new ArrayList();
    private List<GameObject> removeObjects = new ArrayList();

    public Game() {
        var r = new Random();
        for (var i = 0; i < 100; i++)
            allObjects.add(new Tree(r.nextFloat() * 1000f, r.nextFloat() * 1000f));
    }

    public void keyPressed(int keyCode, long id) {
        if (moveKeys.containsKey(keyCode)) {
            getById(id).addMoveKey(moveKeys.get(keyCode));
        }
        if (SHOT == keyCode) {
            getById(id).setShot(true);
        }
    }

    public void keyReleased(int keyCode, long id) {
        if (moveKeys.containsKey(keyCode)) {
            getById(id).removeMoveKey(moveKeys.get(keyCode));
        }
        if (SHOT == keyCode) {
            getById(id).setShot(false);
        }
    }

    public void step(float deltaTime) {
        allObjects.forEach(o -> o.step(deltaTime));
        bulletsCheck();
        synchronized (allObjects) {
            allObjects.addAll(addObjects);
            allObjects.removeAll(removeObjects);
        }
        addObjects.clear();
        removeObjects.clear();
    }

    private void bulletsCheck() {
        AtomicInteger n = new AtomicInteger();
        var maxSize = allObjects.stream().max(Comparator.comparing(GameObject::getSize)).map(GameObject::getSize).orElse(1f);
        var chunkSize = 200;
        var chunks = new HashMap<Point2I, Set<GameObject>>(0);
        for (var gameObject : allObjects) {
            for (int x = -1; x < 2; x++)
                for (int y = -1; y < 2; y++) {
                    int bx = (int) ((gameObject.getX() + x*maxSize) / chunkSize);
                    int by = (int) ((gameObject.getY() + y*maxSize)/ chunkSize);
                    var point = new Point2I(bx, by);
                    var data = chunks.computeIfAbsent(point, p->new HashSet<>());
                    data.add(gameObject);
                }
        }
        chunks.values().forEach(objs -> {
            objs.forEach(o1 -> {
                objs.forEach(o2 -> {
                    n.getAndIncrement();
                    if (o1 == o2)
                        return;
                    if (o1.checkCollision(o2)) {
                        o1.onCollision(o2, this);
                        o2.onCollision(o1, this);
                    }
                });
            });
        });
    }

    public Player getById(long id) {
        synchronized (allObjects) {
            return (Player) allObjects.stream().filter(o -> o.getID() == id).findFirst().get();
        }
    }

    public List<GameObject> getAllObject() {
        return new ArrayList(allObjects);
    }

    public void add(GameObject gameObject) {
        addObjects.add(gameObject);
    }

    public void remove(GameObject gameObject) {
        removeObjects.add(gameObject);
    }

    public boolean canMoveTo(GameObject gameObject, Point2F p) {
        var rectangle = new Rectangle((int) (p.x - gameObject.size / 2), (int) (p.y - gameObject.size / 2), (int) gameObject.size, (int) gameObject.size);
        synchronized (allObjects) {
            return allObjects.stream().filter(o -> o.getID() != gameObject.getID()).map(GameObject::asRectangle).allMatch(r -> !r.intersects(rectangle));
        }
    }
}
