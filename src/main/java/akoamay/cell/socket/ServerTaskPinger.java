package akoamay.cell.socket;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import akoamay.cell.message.PingMessage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerTaskPinger extends Thread {

    private ServerTask task;
    private boolean isTerminate = false;

    public ServerTaskPinger(@NonNull ServerTask task) {
        this.task = task;
    }

    public void run() {
        while (!isTerminate) {
            try {
                task.send(new PingMessage());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        isTerminate = true;
        log.info("thread will be terminated");
    }

}