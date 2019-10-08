package akoamay.cell.socket;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataReceiver extends Thread {
    private Socket sc;
    private boolean isTerminate = false;
    private DataReceivedListener dataReceivedListener;
    private DisconnectedListener disconnectedListener;

    public DataReceiver(@NonNull Socket sc) {
        this.sc = sc;
    }

    public void addDataReceivedListener(@NonNull DataReceivedListener listener) {
        this.dataReceivedListener = listener;
    }

    public void addDisconnectedListener(@NonNull DisconnectedListener listener) {
        this.disconnectedListener = listener;
    }

    public void run() {
        while (!isTerminate) {
            try {
                ObjectInputStream ois = new ObjectInputStream(sc.getInputStream());
                if (dataReceivedListener != null)
                    dataReceivedListener.onDataReceived(ois.readObject());
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                if (disconnectedListener != null) {
                    disconnectedListener.onDisconnected(new InetSocketAddress(sc.getInetAddress(), sc.getPort()));
                }
                terminate();
            }
        }
    }

    public void terminate() {
        isTerminate = true;
        log.debug("thread will be terminated");
    }

}