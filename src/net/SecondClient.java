package net;

import java.io.IOException;
import java.rmi.NotBoundException;

public class SecondClient {
    public static void main(String[] args) throws IOException, NotBoundException {
        Client.main();
    }
}
