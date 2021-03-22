package fr.republicraft.common.api.metrics.api.connectors.tcp;

import fr.republicraft.common.api.helper.Retry;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NIOClient {

    public static NIOClient instance;
    private static SocketChannel client;
    private final Logger logger;
    ArrayList<String> bulkMessages = new ArrayList<>();
    private InetSocketAddress address;

    public NIOClient(Logger logger) {
        this.logger = logger;
    }

    public static NIOClient getInstance(Logger logger) {
        if (instance == null) {
            instance = new NIOClient(logger);
        }
        return instance;
    }

    public void open(InetSocketAddress address) {
        this.address = address;
        logger.debug("open metric socket connection address={}, port={}", address.getHostName(), address.getPort());
        try {
            client = SocketChannel.open(address);
        } catch (IOException e) {
            logger.error(e.getMessage());
            //TODO retry interval
        }
    }

    public NIOClient message(String message) {
        bulkMessages.add(message);
        return this;
    }

    SocketChannel getOpenClient() {
        if (client == null) {
            open(address);
        }
        return client;
    }

    public NIOClient flush() throws IOException {
        //TODO using bluck flush, settings @timestamp
        List<String> clone = new ArrayList<>(bulkMessages);
        bulkMessages.clear();
        for (String message : clone) {
            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            try {
                Retry.execute(3, 1000, (maxTries, delay, currentTry) -> {
                    // TODO ensure connections... client pool?
                    SocketChannel c = getOpenClient();
                    if (c != null) {
                        c.write(buffer);
                        logger.debug("metric reporter flush {} messages", clone.size());
                        return true;
                    }
                    return false;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            buffer.clear();
        }
        close();
        return this;
    }

    public void close() {
        try {
            if (client != null) {
                logger.debug("close socket connection");
                client.close();
                client = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
