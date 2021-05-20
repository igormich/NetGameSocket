import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class Client {
    volatile private static List<GameObject> data= Collections.emptyList();
    public static void main() throws IOException {
        var frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        Socket s = new Socket("localhost", 666);
        var oos = new ObjectOutputStream(s.getOutputStream());
        oos.flush();
        var ois = new ObjectInputStream(s.getInputStream());
        var id = ois.readLong();
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

                    oos.writeInt(e.getKeyCode());
                    oos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    oos.writeInt(-e.getKeyCode());
                    oos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        var net = new Thread(() -> {
            while (true) {
                try {
                    data = (List<GameObject>) ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        net.setDaemon(true);
        net.start();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        main();
    }
}
