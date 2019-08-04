package akoamay.cell.socket;

import java.net.InetSocketAddress;;

public interface SocketServerEventListener {
    public void onConnected(InetSocketAddress address);

    public void onDisconnected(InetSocketAddress address);

    public void onDataReceived(InetSocketAddress address, Object data);

    public void onError(Exception e);
}