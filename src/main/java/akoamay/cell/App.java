package akoamay.cell;

import java.net.InetSocketAddress;

import akoamay.cell.socket.SocketClient;
import akoamay.cell.socket.SocketServer;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        new App(args[0]);
    }

    public App(String mode) {
        if (mode.equals("s")) {
            SocketServer server = new SocketServer(1234);
            server.start();
        } else {
            InetSocketAddress address = new InetSocketAddress("localohst", 1234);
            SocketClient client = new SocketClient(address);
            client.connect();
        }
    }
}
