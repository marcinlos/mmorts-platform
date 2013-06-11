package com.example;

import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.data.Database;

/**
 * Example implementation of {@linkplain CustomPersistor}, as a part of quick
 * proof-of-concept during the system initial developement.
 */
public class MathPersistor implements CustomPersistor {

    private static final Logger logger = Logger.getLogger(MathPersistor.class);
    
    private Config config;
    private Database database;

    @Inject
    public MathPersistor(Config config, Database database) {
        logger.debug("Initializing exemplary custom persistor");
        this.config = config;
        this.database = database;
        logger.debug("Fun, we have the config: ");
        for (Entry<String, String> e: config.getProperties().entrySet()) {
            System.out.printf("%s = %s\n", e.getKey(), e.getValue());
        }
    }
    
    
    

}
