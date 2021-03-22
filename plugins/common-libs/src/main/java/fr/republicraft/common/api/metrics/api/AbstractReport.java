package fr.republicraft.common.api.metrics.api;

public abstract class AbstractReport implements Report<Reporter> {

    protected transient Reporter reporter;

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public void send() {
        reporter.getReportToSend().add(this);
    }
}
