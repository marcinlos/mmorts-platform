package com.example;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;

/**
 * Example implementation of {@linkplain CustomPersistor}, as a part of quick
 * proof-of-concept during the system initial developement.
 */
public class MathPersistor implements CustomPersistor {

    private static final Logger logger = Logger.getLogger(MathPersistor.class);
    
    
    
    public MathPersistor() {
        logger.debug("Initializing exemplary custom persistor");
    }

}
