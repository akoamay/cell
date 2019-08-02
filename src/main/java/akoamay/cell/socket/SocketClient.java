package akoamay.cell.socket;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
    private SocketEventListener listener;
    private Socket sc = null;
    private InetSocketAddress address;

    public SocketClient(InetSocketAddress address) {
        this.address = address;
    }

    public void connect() {
        try {
            sc = new Socket(address.getAddress(), address.getPort());
            DataReceiver receiver = new DataReceiver(sc);
            receiver.addDataReceivedListener(new DataReceivedListener() {
                @Override
                public void onDataReceived(Object data) {
                    listener.onDataReceived(address, data);
                }
            });
            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSocketEventListener(SocketEventListener listener) {
        this.listener = listener;
    }

    public void send(Object object) {
        if (sc.isConnected()) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(sc.getOutputStream());
                oos.writeObject(object);
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}