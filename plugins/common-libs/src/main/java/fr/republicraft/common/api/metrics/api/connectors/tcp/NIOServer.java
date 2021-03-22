package fr.republicraft.common.api.metrics.api.connectors.tcp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Crunchify.com
 *
 */

public class NIOServer {

    public static void main(String[] args) throws IOException {

        // Selector: multiplexor of SelectableChannel objects
        Selector selector = Selector.open(); // selector is open here

        // ServerSocketChannel: selectable channel for stream-oriented listening sockets
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1111);

        // Binds the channel's socket to a local address and configures the socket to listen for connections
        socketChannel.bind(socketAddress);

        // Adjusts this channel's blocking mode.
        socketChannel.configureBlocking(false);

        int ops = socketChannel.validOps();
        SelectionKey selectKy = socketChannel.register(selector, ops, null);

        // Infinite loop..
        // Keep server running
        while (true) {

//            log("i'm a server and i'm waiting for new connection and buffer select...");
            // Selects a set of keys whose corresponding channels are ready for I/O operations
            selector.select();

            // token representing the registration of a SelectableChannel with a Selector
            Set<SelectionKey> crunchifyKeys = selector.selectedKeys();
            Iterator<SelectionKey> crunchifyIterator = crunchifyKeys.iterator();

            while (crunchifyIterator.hasNext()) {
                SelectionKey myKey = crunchifyIterator.next();

                System.out.println("mykey" +
                        " accepatble="+ myKey.isAcceptable()+
                        " readable="+ myKey.isReadable()+
                        " connectable="+ myKey.isConnectable()+
                        " valid="+ myKey.isValid()+
                        " writable="+ myKey.isWritable());
                // Tests whether this key's channel is ready to accept a new socket connection
                if (myKey.isAcceptable()) {
                    SocketChannel client = socketChannel.accept();
                    // Adjusts this channel's blocking mode to false
                    client.configureBlocking(false);

                    // Operation-set bit for read operations
                    client.register(selector, SelectionKey.OP_READ);
                    log("Connection Accepted: " + client.getLocalAddress() + "\n");
                    System.out.println("connected="+client.isConnected());

                    // Tests whether this key's channel is ready for reading
                } else if (myKey.isReadable()) {

                    SocketChannel client = (SocketChannel) myKey.channel();
                    ByteBuffer crunchifyBuffer = ByteBuffer.allocate(256);
                    client.read(crunchifyBuffer);
                    String result = new String(crunchifyBuffer.array()).trim();
                    System.out.println(client.isConnected());
                    log("Message received: " + result);

                    if (result.equals("")) {
                        client.close();
                        log("\nIt's time to close connection as we got last company name 'Crunchify'");
                        log("\nServer will keep running. Try running client again to establish new connection");
                    }
                }
                crunchifyIterator.remove();
            }
        }
    }

    private static void log(String str) {
        System.out.println(str);
    }
}
