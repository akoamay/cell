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
            } catch (Exception e) {
                log.error("ece", e);
                if (disconnectedListener != null)
                    // disconnectedListener.onDisconnected(new InetSocketAddress(sc.get, port));
                    terminate();
            }
        }
    }

    public void terminate() {
        isTerminate = true;
        log.info("thread will be terminated");
    }

}