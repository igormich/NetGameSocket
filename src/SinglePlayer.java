import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SinglePlayer {


    public static void main(String[] args) throws IOException {
        var frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        var game = new Game();
        Player activePlayer = new Player(game);
        game.add(activePlayer);
        var gamePainter = new GamePainter(game::getAllObject, activePlayer.getID());
        frame.add(new JComponent() {
            @Override
            public void paint(Graphics g) {
                gamePainter.paint((Graphics2D) g);
                frame.invalidate();
                frame.repaint();
            }
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                game.keyPressed(e.getKeyCode(), activePlayer.getID());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                game.keyReleased(e.getKeyCode(), activePlayer.getID());
            }
        });


        var ref = new Object() {
            long time = System.currentTimeMillis();
        };
        var logic = new Thread(() -> {
            while (true) {
                var time2 = System.currentTimeMillis();
                game.step((time2 - ref.time) / 1000f);
                ref.time = time2;
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        logic.setDaemon(true);
        logic.start();
        frame.setVisible(true);
    }


}
