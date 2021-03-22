package fr.republicraft.common.api.metrics.api;

import org.slf4j.Logger;

import java.util.List;

/**
 * Public interface for reporters
 *
 * @param <T extends Report> BasicReport implementation
 */
public interface Reporter<T extends Report> {
    void send(T report);

    List<T> getReportToSend();

    Logger getLogger();
}
