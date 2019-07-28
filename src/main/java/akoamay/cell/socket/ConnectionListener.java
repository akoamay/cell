package akoamay.cell.socket;

import java.net.InetSocketAddress;

public interface ConnectionListener {
    public void onConnected(InetSocketAddress address);

    public void onDisconnected(InetSocketAddress address);
}