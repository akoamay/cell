package akoamay.cell.socket;

import java.net.InetSocketAddress;

public interface ConnectedListener {
    public void onConnected(InetSocketAddress address);
}