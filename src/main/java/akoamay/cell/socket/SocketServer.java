package akoamay.cell.socket;

import java.net.InetSocketAddress;

public class SocketServer implements SocketEventListener {
    private SocketEventListener listener;

    public SocketServer(int port, SocketEventListener listener) {
        this.listener = listener;
    }

    public void send(InetSocketAddress address, Object object) {
    }

    @Override
    public void onDataReceived(InetSocketAddress address, Object data) {
        listener.onDataReceived(address, data);
    }

    @Override
    public void onConnected(InetSocketAddress address) {
        listener.onConnected(address);
    }

    @Override
    public void onDisconnected(InetSocketAddress address) {
        listener.onDisconnected(address);
    }

}