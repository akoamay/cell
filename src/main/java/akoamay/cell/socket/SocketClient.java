package akoamay.cell.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import lombok.NonNull;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketClient {
    private SocketClientEventListener listener;
    private Socket sc;
    private InetSocketAddress address;
    private DataReceiver receiver;

    public SocketClient(@NonNull InetSocketAddress address, @NonNull SocketClientEventListener listener) {
        this.listener = listener;
        try {
            sc = new Socket(address.getHostName(), address.getPort());
            receiver = new DataReceiver(sc);
            receiver.addDataReceivedListener(new DataReceivedListener() {
                @Override
                public void onDataReceived(Object data) {
                    if (listener != null)
                        listener.onDataReceived(address, data);
                }
            });
            receiver.start();

            if (listener != null)
                listener.onConnected(address);
        } catch (UnknownHostException e) {
            if (listener != null)
                listener.onConnected(address);
        } catch (IOException e) {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException ie) {
                }
            }
            if (receiver != null)
                receiver.terminate();
        }
    }

    public void send(Object object) {
        if (sc.isConnected()) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(sc.getOutputStream());
                oos.writeObject(object);
                oos.flush();
            } catch (Exception e) {
                log.info("send fail", e);
            }
        }
    }

}