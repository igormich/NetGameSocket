package swing;

import game.Bullet;
import game.GameObject;
import game.Player;
import game.Tree;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class GamePainter {
    private Supplier<List<GameObject>> dataSource;
    private long id;
    private BufferedImage tank;
    private BufferedImage tree;

    public GamePainter(Supplier<List<GameObject>> dataSource, long id) throws IOException {
        this.dataSource = dataSource;
        this.id = id;
        tank = ImageIO.read(new File("./img/tank1.png"));
        tree = ImageIO.read(new File("./img/tree.png"));
    }

    public void paint(Graphics2D g) {
        try {
            var allData = dataSource.get();
            var activePlayer = allData.stream().filter(o->o.getID()==id).findFirst().get();
            g.translate(-activePlayer.getX(), -activePlayer.getY());
            var screenSize = g.getClipBounds();
            g.translate(screenSize.width / 2, screenSize.height / 2);
            allData.sort(Comparator.comparingInt(GameObject::getZ));
            allData.forEach(o -> paint(g, o));
        }catch (NoSuchElementException e) {
            return;
        }
    }

    private void paint(Graphics2D g, GameObject gameObject) {
        var transform = g.getTransform();
        g.translate(gameObject.getX(), gameObject.getY());
        if (gameObject instanceof Player player) {
            switch(player.getDirection()) {
                case UP -> {}
                case DOWN -> g.rotate(Math.PI);
                case LEFT -> g.rotate(-Math.PI/2);
                case RIGHT -> g.rotate(Math.PI/2);
            }
            g.drawImage(tank, -tank.getWidth() / 2, -tank.getHeight() / 2, null);
        }
        if (gameObject instanceof Bullet) {
            g.drawOval(-5,-5,10,10);
        }
        if (gameObject instanceof Tree) {
            g.drawImage(tree, -tree.getWidth() / 2, -tree.getHeight() / 2, null);
        }
        g.setTransform(transform);
    }
}
