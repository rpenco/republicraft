package fr.republicraft.common.api.managers;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Managers {
    Map<Class<? extends Manager>, Manager> managers = new HashMap<>();
    private final Logger logger;

    public Managers(Logger logger) {
        this.logger = logger;
    }

    public Manager get(Class<? extends Manager> className) {
        return managers.get(className);

    }

    public Managers add(Manager manager) {
        managers.put(manager.getClass(), manager);
        return this;
    }

    public Managers addAll(Managers remoteManager) {
        for (Map.Entry<Class<? extends Manager>, Manager> classManagerEntry : remoteManager.managers.entrySet()) {
            managers.put(classManagerEntry.getKey(), classManagerEntry.getValue());
        }
        return this;
    }

    public void start() {
        logger.info("Starting managers...");
        managers.forEach((s, manager) -> {
            if (!manager.isStarted()) {
                logger.info("starting manager={}...", manager.getClass().getName());
                manager.start();
                manager.setStarted(true);
            } else {
                logger.info("manager already started manager={}...", manager.getClass().getName());
            }
        });
    }

    public void stop() {
        logger.info("Stopping managers...");
        managers.forEach((s, manager) -> {
            if (manager.isStarted()) {
                logger.info("stopping manager={}...", manager.getClass().getName());
                manager.stop();
                manager.setStarted(false);
            } else {
                logger.info("manager already stopped manager={}...", manager.getClass().getName());
            }
        });
    }
}
