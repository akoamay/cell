package akoamay.cell;

import java.net.InetSocketAddress;

import akoamay.cell.socket.SocketClient;
import akoamay.cell.socket.SocketServer;
import akoamay.cell.socket.SocketClientEventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        new App(args[0]);
    }

    public App(String mode) {
        if (mode.equals("s")) {
            SocketServer server = new SocketServer(1234);
            server.start();
        } else {
            InetSocketAddress address = new InetSocketAddress("localhost", 1234);

            SocketClient client = new SocketClient(address, new SocketClientEventListener() {

                @Override
                public void onConnected(InetSocketAddress address) {
                    log.info("connected");
                }

                @Override
                public void onDisconnected(InetSocketAddress address) {
                    log.info("discon");
                }

                @Override
                public void onDataReceived(InetSocketAddress address, Object data) {
                    log.info("received:" + data.toString());
                }

                @Override
                public void onError(Exception e) {

                }

            });

        }
    }
}
