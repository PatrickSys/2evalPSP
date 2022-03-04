package tutonio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/************************************************************************
 Made by        PatrickSys
 Date           03/03/2022
 Package        tutonio
 Description:
 ************************************************************************/
public class Server {
    public static final String HOST = "localhost";
    public static final int PORT = 1337;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private SelectionKey serverKey;

    public static void main(String[] args) throws Throwable {
        new Server(new InetSocketAddress(HOST, PORT));
    }

    public Server(InetSocketAddress listenAddress) throws Throwable {
        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);
        this.serverKey = this.serverChannel.register(selector = Selector.open(), SelectionKey.OP_ACCEPT);
        this.serverChannel.bind(listenAddress);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                loop();
            }catch (Throwable t) {
                t.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }


    static Map<SelectionKey, Client> clientMap = new HashMap<SelectionKey, Client>();

    private void loop() throws Throwable {
        this.selector.selectNow();

        for (SelectionKey key : this.selector.selectedKeys()) {
            if(!key.isValid()) continue;

            if (key.isAcceptable()) {
                SocketChannel acceptedChannel = this.serverChannel.accept();
                if (acceptedChannel == null) continue;
                acceptedChannel.configureBlocking(false);
                SelectionKey readKey = acceptedChannel.register(selector, SelectionKey.OP_READ);
                clientMap.put(readKey, new Client(readKey, acceptedChannel));
                System.out.println("Client accepted from" + acceptedChannel.getRemoteAddress() + ", total clients: " + clientMap.size());
            }

            if(key.isReadable()) {
             clientMap.get(key).read();
            }
            this.selector.selectedKeys().clear();
        }


    }

}
