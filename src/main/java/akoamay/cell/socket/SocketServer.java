package akoamay.cell.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketServer extends Thread {
    private int port;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private SocketServerEventListener listener;
    private HashMap<InetSocketAddress, ServerTask> clientsMap = new HashMap<InetSocketAddress, ServerTask>();
    private boolean isTerminate = false;

    public SocketServer(int port) {
        this.port = port;
    }

    public HashMap<InetSocketAddress, ServerTask> getClientsMap() {
        return clientsMap;
    }

    @Override
    public void run() {
        ServerSocket ss = null;
        try {

            ss = new ServerSocket(port);

            while (!isTerminate) {

                log.debug("waiting");
                Socket sc = ss.accept();
                log.debug(sc.toString() + " has connected");
                ServerTask task = new ServerTask(sc);
                task.addDataReceivedListener(new DataReceivedListener() {
                    @Override
                    public void onDataReceived(Object data) {
                        listener.onDataReceived(task.getAddress(), data);
                    }
                });
                task.addDisconnectedListener(new DisconnectedListener() {
                    @Override
                    public void onDisconnected(InetSocketAddress address) {
                        clientsMap.remove(address);
                        listener.onDisconnected(address);
                    }
                });

                InetSocketAddress address = new InetSocketAddress(sc.getInetAddress(), sc.getPort());
                clientsMap.put(address, task);
                executor.submit(task);
                if (listener != null)
                    listener.onConnected(address);

            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            listener.onError(e);
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

    public void addEventListener(@NonNull SocketServerEventListener listener) {
        this.listener = listener;
    }

    public void terminate() {
        isTerminate = true;
        log.debug("Thread will be terminated");
    }

}