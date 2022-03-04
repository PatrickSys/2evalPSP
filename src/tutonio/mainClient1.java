package tutonio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import static tutonio.Server.HOST;
import static tutonio.Server.PORT;

/************************************************************************
 Made by        PatrickSys
 Date           03/03/2022
 Package        tutonio
 Description:
 ************************************************************************/
public class mainClient1 {

    public static void main(String[] args) throws Throwable {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);


        Selector selector = Selector.open();
        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);

        Client client = new Client(key, socketChannel);

        client.read();

    }
}
