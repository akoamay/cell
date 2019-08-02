package akoamay.cell.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;

public class SocketServer {
    private int port;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private SocketEventListener listener;
    private HashMap<InetSocketAddress, ServerTask> clientsMap = new HashMap<InetSocketAddress, ServerTask>();

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            Socket sc = ss.accept();
            ServerTask task = new ServerTask(sc);
            task.addDataReceivedListener(new DataReceivedListener() {
                @Override
                public void onDataReceived(Object data) {
                    listener.onDataReceived(task.getAddress(), data);
                }
            });

            clientsMap.put(new InetSocketAddress(sc.getInetAddress(), sc.getPort()), task);
            executor.submit(new ServerTask(sc));
        } catch (IOException e) {

        } finally {
            if (ss != null && !ss.isClosed()) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void send(InetSocketAddress address, Object object) {
        ServerTask task = clientsMap.get(address);
        task.send(object);
    }

    public void addSocketEventListener(SocketEventListener listener) {
        this.listener = listener;
    }

}