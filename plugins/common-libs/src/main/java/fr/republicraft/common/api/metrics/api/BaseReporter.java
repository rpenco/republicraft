package fr.republicraft.common.api.metrics.api;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseReporter<T extends Report> extends AbstractReporter {
    private final Class<T> reportClass;
    protected final List<T> toSendReport = new ArrayList<>();

    public BaseReporter(Class<T> reportClass) {
        this.reportClass = reportClass;
    }

    public T create() {
        try {
            Report e = reportClass.getDeclaredConstructor().newInstance();
            e.setReporter(this);
            return (T) e;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List getReportToSend() {
        return toSendReport;
    }

    @FunctionalInterface
    public interface ReportConsumer<T extends Report> {
        T report(T report);
    }
}
