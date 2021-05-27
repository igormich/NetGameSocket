package net;

import game.Game;
import game.GameImpl;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GameServer {

    public static void main(String[] args) throws IOException {

        GameImpl game = new GameImpl();
        var ref = new Object() {
            long time = System.currentTimeMillis();
        };
        Registry registry = LocateRegistry.createRegistry(1099);
        Game messenger = (Game) UnicastRemoteObject.exportObject( game, 0);
        registry.rebind(Common.SERVICE_NAME, messenger);

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


        }

    }
}
