package akoamay.cell.socket;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerTask implements Callable<Void> {
    private Socket sc = null;
    private DataReceivedListener dataReceivedListener;
    private DisconnectedListener disconnectedListener;

    public ServerTask(Socket sc) {
        this.sc = sc;
    }

    public InetSocketAddress getAddress() {
        return new InetSocketAddress(sc.getInetAddress(), sc.getPort());
    }

    public void addDisconnectedListener(DisconnectedListener disconnectedListener) {
        this.disconnectedListener = disconnectedListener;
    }

    public void addDataReceivedListener(DataReceivedListener dataReceivedListener) {
        this.dataReceivedListener = dataReceivedListener;
    }

    @Override
    public Void call() {

        DataReceiver receiver = new DataReceiver(sc);
        receiver.addDataReceivedListener(new DataReceivedListener() {
            @Override
            public void onDataReceived(Object data) {
                dataReceivedListener.onDataReceived(data);
            }
        });

        receiver.addDisconnectedListener(new DisconnectedListener() {
            @Override
            public void onDisconnected(InetSocketAddress address) {
                disconnectedListener.onDisconnected(address);
            }
        });
        receiver.start();

        return null;
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