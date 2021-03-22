package fr.republicraft.common.api.metrics.api;

public interface Report<Reporter> {

    void setReporter(Reporter reporter);

    @Override
    String toString();
}
