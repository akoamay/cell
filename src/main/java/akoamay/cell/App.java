package akoamay.cell;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

import akoamay.cell.message.Message;
import akoamay.cell.socket.SocketClient;
import akoamay.cell.socket.SocketServer;
import akoamay.cell.socket.SocketServerEventListener;
import akoamay.cell.socket.SocketClientEventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        new App(args[0]);
    }

    public App(String mode) {
        if (mode.equals("s")) {
            SocketServer server = new SocketServer(1234);

            server.addEventListener(new SocketServerEventListener() {

                @Override
                public void onError(Exception e) {

                }

                @Override
                public void onDisconnected(InetSocketAddress address) {
                    log.info("onDisconnected");
                    log.info(String.valueOf(server.getClientsMap().size()));

                }

                @Override
                public void onDataReceived(InetSocketAddress address, Object data) {
                    Message msg = (Message)data;
                    //log.info("onDataReceived from " + address.toString() + " data=" + data.toString());
                    log.info("onDataReceived from " + address.toString() + " data=" + msg.getData().length);
                }

                @Override
                public void onConnected(InetSocketAddress address) {
                    log.info("onConnected from " + address);
                    log.info(String.valueOf(server.getClientsMap().size()));
                }
            });

            server.start();

        } else {
            InetSocketAddress address = new InetSocketAddress("localhost", 1234);

            CountDownLatch latch = new CountDownLatch(1);
            SocketClient client = new SocketClient(address, new MySocketClientEventListener(latch));
            try{
                latch.await();
            }catch(Exception e){
                e.printStackTrace();
            }

            System.out.println( "let:s wait" );
            System.out.println( "here" );

            int len = 8192;
            int size = len * len * 2;
            byte[] map = new byte[size];

            for (int i = 0; i < size; i++ ){
                map[i] = (byte)i;
            }

            Message msg = new Message();
            msg.setData(map);
            client.send(msg);
            
            /*
            new SocketClientEventListener() {

                @Override
                public void onConnected(InetSocketAddress address) {
                    log.info("connected");
                }

                @Override
                public void onDisconnected(InetSocketAddress address) {
                    log.info("discon");
                }

                @Override
                public void onDataReceived(InetSocketAddress address, Object data) {
                    log.info("received:" + data.toString());
                }

                @Override
                public void onError(Exception e) {
                    log.info("error:" + e.toString());

                }

            });
            */

        }
    }

    class MySocketClientEventListener implements SocketClientEventListener{
        private CountDownLatch latch;
        public MySocketClientEventListener(CountDownLatch latch){
            this.latch = latch;
        }

        @Override
        public void onConnected(InetSocketAddress address) {
            latch.countDown();
        }

        @Override
        public void onDisconnected(InetSocketAddress address) {
        }

        @Override
        public void onDataReceived(InetSocketAddress address, Object data) {
        }

        @Override
        public void onError(Exception e) {
        }
        
    }
}
