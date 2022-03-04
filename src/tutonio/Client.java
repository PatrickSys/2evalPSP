package tutonio;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/************************************************************************
 Made by        PatrickSys
 Date           03/03/2022
 Package        tutonio
 Description:
 ************************************************************************/
public class Client {


    private static final int BUFFER_SIZE = 128;
    SelectionKey selectionKey;
    SocketChannel socketChannel;
    ByteBuffer buffer;

    public Client(SelectionKey selectionKey, SocketChannel socketChannel) throws Throwable {
        this.selectionKey = selectionKey;
        this.socketChannel = (SocketChannel) socketChannel.configureBlocking(false);
        this.buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    protected void disconnect() {
        Server.clientMap.remove(this.selectionKey);
        try {
            if(this.selectionKey != null) this.selectionKey.cancel();
            if (socketChannel == null) return;
            System.out.println("See you!" + socketChannel.getRemoteAddress());
            socketChannel.close();
        }catch (Throwable ignored) {}
    }

    protected void read() {
        try{
            int amountRead = -1;
            try {
                amountRead = this.socketChannel.read(this.buffer.clear());
            }catch(Throwable t){}

            if(amountRead == -1) disconnect();
            if(amountRead < 1 ) return;
            System.out.println("Sending back " + this.buffer.position() + " bytes");

            this.buffer.flip();
            this.socketChannel.write(this.buffer);

        }catch (Throwable t) {
            this.disconnect();
            t.printStackTrace();
        }
    }

    protected void write(String message) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        this.socketChannel.write(bb);
    }
}
