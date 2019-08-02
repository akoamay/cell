package akoamay.cell.socket;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class DataReceiver extends Thread {
    private Socket sc = null;
    private boolean isTerminate = false;
    private DataReceivedListener listener = null;

    public DataReceiver(Socket sc) {
        this.sc = sc;
    }

    public void addDataReceivedListener(DataReceivedListener listener) {
        this.listener = listener;
    }

    public void run() {
        while (!isTerminate) {
            try {
                ObjectInputStream ois = new ObjectInputStream(sc.getInputStream());
                listener.onDataReceived(ois.readObject());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        isTerminate = true;
    }

}