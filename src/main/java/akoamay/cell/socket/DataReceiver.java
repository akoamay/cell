package akoamay.cell.socket;

import java.net.InetSocketAddress;

public interface DataReceiver {
    public void onDataReceived(InetSocketAddress address, Object data);
}