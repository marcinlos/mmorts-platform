package com.example;

import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.annotations.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.data.Database;

/**
 * Example implementation of {@linkplain CustomPersistor}, as a part of quick
 * proof-of-concept during the system initial developement.
 */
public class MathPersistorImpl implements MathPersistor {

    private static final Logger logger = Logger.getLogger(MathPersistorImpl.class);
    
    /** Field injected after constructor call */
    @Inject
    private Config config;
    
    /** Field injected by the constructor */
    private Database database;
    
    @Inject
    public MathPersistorImpl(Database database) {
        logger.debug("Initializing exemplary custom persistor");
        this.database = database;
    }
    
    /**
     * Special initialization method
     */
    @OnInit
    private void init() {
        logger.debug("Fun, we have the config: ");
        for (Entry<String, String> e: config.getProperties().entrySet()) {
            System.out.printf("%s = %s\n", e.getKey(), e.getValue());
        }
    }
    
    @OnShutdown
    private void shut() {
        logger.debug("Shutting down");
    }

}
