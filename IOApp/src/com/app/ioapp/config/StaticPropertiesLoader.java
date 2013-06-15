package com.app.ioapp.config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StaticPropertiesLoader {

	
	public static Properties load(InputStream inputStream) throws IOException {
        try {
        	Properties p = new Properties();
           BufferedInputStream input = new BufferedInputStream(inputStream);
            p.load(input);
            return p;
        } finally {

        }
    }
}
