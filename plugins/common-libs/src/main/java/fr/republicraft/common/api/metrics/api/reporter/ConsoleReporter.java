package fr.republicraft.common.api.metrics.api.reporter;

import fr.republicraft.common.api.metrics.api.BaseReporter;
import fr.republicraft.common.api.metrics.api.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public class ConsoleReporter<T extends Report> extends BaseReporter<T> {
    private PrintStream out;

    public ConsoleReporter(Class<T> reportClass, PrintStream out) {
        super(reportClass);
        this.out = out;
    }

    public void send(Report report) {
        out.println(report.toString());
    }

    @Override
    public Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }


}
