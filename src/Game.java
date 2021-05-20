import java.awt.event.KeyEvent;
import java.util.*;

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


    volatile private List<GameObject> allObjects = new ArrayList();
    volatile private List<GameObject> addObjects = new ArrayList();
    volatile private List<GameObject> removeObjects = new ArrayList();

    public Game() {
        allObjects.add(new Tree(100f,100f));
        allObjects.add(new Tree(-100f,-100f));
        allObjects.add(new Tree(100f,-100f));
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
        allObjects.addAll(addObjects);

        List<GameObject> bullets = allObjects.stream().filter(o -> o instanceof Bullet).toList();
        List<GameObject> notBullets = new ArrayList(allObjects);
        notBullets.removeAll(bullets);
        bullets.forEach( b->
                notBullets.forEach( o-> {
                    if(o.getID() == ((Bullet)b).getOwnerID())
                        return;
                    if(b.checkCollision(o)) {
                        remove(o);
                        remove(b);
                    }
                })
        );

        addObjects.clear();
        allObjects.removeAll(removeObjects);
        removeObjects.clear();
    }

    public Player getById(long id) {
        return (Player) allObjects.stream().filter(o->o.getID()==id).findFirst().get();
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
}
