package akoamay.cell.socket;

import java.net.InetSocketAddress;

public class SocketClient {
    private SocketEventListener listener;

    public SocketClient(InetSocketAddress address, SocketEventListener listener) {
        this.listener = listener;
    }

    public void send(Object object) {

    }

}