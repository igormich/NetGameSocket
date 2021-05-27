package net;

import game.Game;
import game.GameObject;
import swing.GamePainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Collections;
import java.util.List;

public class Client {
     private static List<GameObject> data= Collections.emptyList();
    public static void main() throws IOException, NotBoundException {
        var frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);

        String objectName = Common.SERVICE_PATH;
        Game game = (Game) Naming.lookup(objectName);
        var id = game.newUser();
        var gamePainter = new GamePainter(()->data, id);
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
                try {

                    game.keyPressed(e.getKeyCode(), id);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    game.keyReleased(e.getKeyCode(), id);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        var net = new Thread(() -> {
            while (true) {
                try {
                    data = game.getAllObject();
                    Thread.sleep(16);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        net.setDaemon(true);
        net.start();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        main();
    }
}
