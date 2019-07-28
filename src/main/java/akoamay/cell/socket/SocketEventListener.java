package akoamay.cell.socket;

import java.net.InetSocketAddress;;

public interface SocketEventListener {
    public void onConnected(InetSocketAddress address);

    public void onDisconnected(InetSocketAddress address);

    public void onDataReceived(InetSocketAddress address, Object data);
}