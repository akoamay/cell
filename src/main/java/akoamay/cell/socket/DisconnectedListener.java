package akoamay.cell.socket;

import java.net.InetSocketAddress;

public interface DisconnectedListener {
    public void onDisconnected(InetSocketAddress address);
}