package fr.republicraft.velocity.api.helpers;

import java.util.logging.Logger;

public class LoggerAdapter extends Logger {

    private org.slf4j.Logger logger;

    protected LoggerAdapter(org.slf4j.Logger logger, String s, String s1) {
        super(s, s1);
        this.logger = logger;
    }

    public static LoggerAdapter getLogger(org.slf4j.Logger logger) {
        return new LoggerAdapter(logger, null, null);
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void severe(String s) {
        logger.error(s);
    }

    @Override
    public void fine(String s) {
        logger.debug(s);
    }
}
