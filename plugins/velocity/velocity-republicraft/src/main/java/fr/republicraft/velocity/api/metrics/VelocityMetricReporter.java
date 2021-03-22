package fr.republicraft.velocity.api.metrics;

import fr.republicraft.common.api.metrics.api.reporter.TcpReporter;
import fr.republicraft.velocity.RepublicraftPlugin;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

public class VelocityMetricReporter extends TcpReporter<VelocityMetricReport> {

    private static VelocityMetricReporter instance;
    private final RepublicraftPlugin plugin;

    public VelocityMetricReporter(Logger logger, RepublicraftPlugin plugin, Class<VelocityMetricReport> reportClass, InetSocketAddress address) throws IOException {
        super(logger, reportClass, address);
        this.plugin = plugin;
    }

    public static VelocityMetricReporter getReporter(RepublicraftPlugin plugin) throws IOException {
        if (instance == null) {
            // TODO throw error host/port null
            instance = new VelocityMetricReporter(plugin.getLogger(), plugin, VelocityMetricReport.class,
                    new InetSocketAddress(plugin.getConfig().getMetrics().getHost(), plugin.getConfig().getMetrics().getPort()));
            if (plugin.getConfig().getMetrics().isEnabled()) {
                instance.plugin.getLogger().info("metrics reporter is enable.");
                instance.open();
            } else {
                instance.plugin.getLogger().warn("metrics reporter is disabled.");
            }
        }
        return instance;
    }

    public VelocityMetricReport create() {
        return create(plugin);
    }

    public VelocityMetricReport create(RepublicraftPlugin plugin) {
        VelocityMetricReport report = super.create();
        report.setPluginName("republicraft");
        report.setPluginVersion("1.0.0");
        report.setProxy(plugin.getProxy());
        return report;
    }

    public void create(RepublicraftPlugin plugin, ReportConsumer<VelocityMetricReport> report) {
        VelocityMetricReport rep = super.create();
        rep.setPluginName("republicraft");
        rep.setPluginVersion("1.0.0");
        rep.setProxy(plugin.getProxy());
        if (plugin.getConfig().getMetrics().isEnabled()) {
            toSendReport.add(report.report(rep));
        }
    }

//    @Override
//    public void send(Report report) {
//        plugin.getProxy().getScheduler().buildTask(plugin, () -> {
//            String rep = report.toString();
//            if(plugin.getLogger().isDebugEnabled()) {
//                plugin.getLogger().debug("send metrics={}", rep);
//            }
//            if(plugin.getConfig().getMetrics().isEnabled()){
//                VelocityMetricReporter.super.send(rep);
//            }
//        }).schedule();
//    }
}
