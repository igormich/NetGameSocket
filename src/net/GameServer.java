package net;

import game.Game;
import game.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Random;

public class GameServer {

    public static void main(String[] args) throws IOException {

        Random r = new Random();
        Game game = new Game();
        ServerSocket ss = new ServerSocket(666);
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

        while (true) {
            var s = ss.accept();
            var t = new Thread(() -> {
                long id = 0;
                try {
                    var tank = new Player(game);
                    do {
                        tank.setX(r.nextInt(1000));
                        tank.setY(r.nextInt(1000));
                    } while (game.getAllObject().stream().allMatch(o -> !o.checkCollision(tank)));
                    id = tank.getID();
                    game.add(tank);
                    var oos = new ObjectOutputStream(s.getOutputStream());
                    oos.flush();
                    var ois = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
                    oos.writeLong(tank.getID());
                    oos.flush();
                    long finalId1 = id;
                    var reader = new Thread(() -> {
                        while (true) {
                            int keyCode = 0;
                            try {
                                keyCode = ois.readInt();
                                if (keyCode < 0)
                                    game.keyReleased(-keyCode, finalId1);
                                else
                                    game.keyPressed(keyCode, finalId1);
                            } catch (IOException e) {
                                game.getAllObject().stream().filter(o -> o.getID() == finalId1).findFirst().ifPresent(game::remove);
                                return;
                            }

                        }
                    });
                    reader.setDaemon(true);
                    reader.start();
                    while (true) {
                        var data = game.getAllObject();
                        oos.writeObject(data);
                        oos.flush();
                        oos.reset();
                        try {
                            Thread.sleep(16);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            } catch(Exception e){
                long finalId = id;
                game.getAllObject().stream().filter(o -> o.getID() == finalId).findFirst().ifPresent(game::remove);
                return;
            }
        });
        t.setDaemon(true);
        t.start();
    }

}
}
