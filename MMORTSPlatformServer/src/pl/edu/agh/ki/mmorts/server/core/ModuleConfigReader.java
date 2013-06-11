package pl.edu.agh.ki.mmorts.server.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * Class for loading and parsing module configuration data from a JSON file.
 * 
 * TODO: Specify the format
 */
public class ModuleConfigReader {

    private static final Logger logger = Logger
            .getLogger(ModuleConfigReader.class);

    /** Json parser */
    private Gson gson = new Gson();

    /** Map of module descriptors */
    private Map<String, ModuleDescriptor> descriptors;

    public ModuleConfigReader() {
        descriptors = new HashMap<String, ModuleDescriptor>();
    }

    /**
     * Loads configuration from the specified input stream.
     * 
     * @param stream
     *            Input stream to load configuration from
     * 
     * @see #load(File)
     * @see #load(String)
     */
    public void load(InputStream stream) {
        throw new NotImplementedException();
    }

    /**
     * Reads the configuration from the specified file.
     * 
     * @param file
     *            File to load configuration from
     * 
     * @throws ModuleConfigException
     *             In case of an error
     * 
     * @see #load(String)
     * @see #load(InputStream)
     */
    public void load(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            load(input);
        } catch (IOException e) {
            logger.error("Error reading module configuration file " + file, e);
            throw new ModuleConfigException("IO error", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e1) {
                    logger.error("Error while closing module config file"
                            + file, e1);
                }
            }
        }
    }

    /**
     * Reads configuration from the file denoted by the specified path.
     * 
     * @param path
     *            Path of the configuration file to load
     * 
     * @see #load(InputStream)
     * @see #load(File)
     */
    public void load(String path) {
        load(new File(path));
    }

    /**
     * @return Map of loaded descriptors
     */
    public Map<String, ModuleDescriptor> getModules() {
        return Collections.unmodifiableMap(descriptors);
    }

}
