package fr.republicraft.common.api.metrics.api;

import java.util.Iterator;

public class RunnableReporter implements Runnable {

    private final Reporter reporter;

    public RunnableReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void run() {
        Iterator<Report> i = reporter.getReportToSend().iterator();
        if(reporter.getLogger().isDebugEnabled()){
            reporter.getLogger().debug("send metrics size={}", reporter.getReportToSend().size());
        }
        while (i.hasNext()) {
            reporter.send(i.next());
            i.remove();
        }
    }
}
