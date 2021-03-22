package fr.republicraft.papermc.world.api.metrics;

import fr.republicraft.common.api.metrics.api.reporter.TcpReporter;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

public class PaperMetricReporter extends TcpReporter<PaperMetricReport> {

    private static PaperMetricReporter instance;
    private final RepublicraftPlugin plugin;

    public PaperMetricReporter(Logger logger, RepublicraftPlugin plugin, Class<PaperMetricReport> reportClass, InetSocketAddress address) {
        super(logger, reportClass, address);
        this.plugin = plugin;
    }

    public static PaperMetricReporter getReporter(RepublicraftPlugin plugin) {
        if (instance == null) {
            // TODO throw error host/port null
            instance = new PaperMetricReporter(plugin.getSLF4JLogger(), plugin,
                    PaperMetricReport.class, new InetSocketAddress(plugin.getWorldConfig().getMetrics().getHost(),
                    plugin.getWorldConfig().getMetrics().getPort()));
            if (plugin.getWorldConfig().getMetrics().isEnabled()) {
                instance.open();
            }
        }
        return instance;
    }

    public PaperMetricReport create() {
        return create(plugin);
    }

    public PaperMetricReport create(JavaPlugin plugin) {
        PaperMetricReport report = super.create();
        report.setPluginName(plugin.getName());
        report.setPluginVersion("1.0.0");
        report.setServer(plugin.getServer());
        return report;
    }

    public void create(JavaPlugin plugin, ReportConsumer<PaperMetricReport> report) {
        PaperMetricReport rep = super.create();
        rep.setPluginName(plugin.getName());
        rep.setPluginVersion("1.0.0");
        rep.setServer(plugin.getServer());
        if (this.plugin.getWorldConfig().getMetrics().isEnabled()) {
            toSendReport.add(report.report(rep));
        }
    }

}
