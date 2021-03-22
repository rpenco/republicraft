package fr.republicraft.common.api.metrics.api.reporter;

import fr.republicraft.common.api.metrics.api.BaseReporter;
import fr.republicraft.common.api.metrics.api.Report;
import fr.republicraft.common.api.metrics.api.RunnableReporter;
import fr.republicraft.common.api.metrics.api.connectors.tcp.NIOClient;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TcpReporter<T extends Report> extends BaseReporter<T> {
    private final NIOClient reporter;
    private final Logger logger;
    private final InetSocketAddress address;
    private ScheduledExecutorService executor;

    public TcpReporter(Logger logger, Class<T> reportClass, InetSocketAddress address) {
        super(reportClass);
        this.logger = logger;
        this.address = address;
        reporter = NIOClient.getInstance(logger);
    }

    public void open() {
        reporter.open(address);
        executor = Executors.newSingleThreadScheduledExecutor();
        logger.info("schedule metric runnable.");
        executor.scheduleAtFixedRate(new RunnableReporter(this), 0, 5, TimeUnit.SECONDS);
    }

    public void send(String report) {
        try {
            reporter.message(report);
            reporter.flush();
        } catch (IOException e) {
            logger.error("failed to send metrics error={}", e.getMessage());
        }
    }

    public void send(Report report) {
        try {
            if(logger.isDebugEnabled()){
                logger.info("send metric={}", report.toString());
            }
            reporter.message(report.toString());
            reporter.flush();
        } catch (Exception e) {
            logger.error("failed to send metrics", e);
        }
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public void close() {
        if (reporter != null) {
            reporter.close();
        }

        if (executor != null) {
            executor.shutdown();
        }
    }


}
