package game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Game extends Remote {
    void keyPressed(int keyCode, long id) throws RemoteException;

    void keyReleased(int keyCode, long id) throws RemoteException;

    Player getById(long id) throws RemoteException;

    List<GameObject> getAllObject() throws RemoteException;

    long newUser() throws RemoteException;
}
