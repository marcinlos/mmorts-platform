package pl.edu.agh.ki.mmorts.server.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.modules.Module;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;
import pl.edu.agh.ki.mmorts.server.util.GsonUtil;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

/**
 * Class for loading and parsing module configuration data from a JSON file.
 * 
 * @author los
 */
public class ModuleConfigReader {

    private static final Logger logger = Logger
            .getLogger(ModuleConfigReader.class);

    /** Very simple data structure for parsing json */
    class ModuleData {
        @SerializedName("class")
        String clazz;
        String name;
        String[] unicast;
        String[] groups;
        Map<String, String> properties;
    }

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
        Reader reader = new InputStreamReader(stream);
        try {
            GsonUtil.gson.fromJson("java.lang.String", Class.class);
            ModuleData[] data = GsonUtil.gson.fromJson(reader, ModuleData[].class);
            for (ModuleData item : data) {
                try {
                    logger.debug("Processing config of module " + item.name);
                    ModuleDescriptor desc = makeDescriptor(item);
                    addModule(desc);
                } catch (Exception e) {
                    logger.error("Error processing module " + item.name
                            + " configuration", e);
                }
            }
        } catch (JsonSyntaxException e) {
            logger.error("Error while parsing JSON module configuration");
            logger.error(e);
            throw new ModuleConfigException(e);
        }
    }

    /**
     * Creates {@code ModuleDescriptor} from {@code ModuleData}
     */
    private ModuleDescriptor makeDescriptor(ModuleData item) {
        Class<? extends Module> clazz = loadClass(item.clazz);
        ModuleDescriptor.Builder b = ModuleDescriptor.create(item.name, clazz);
        for (String address : item.unicast) {
            b.addUnicast(address);
        }
        for (String group : item.groups) {
            b.addGroup(group);
        }
        for (Entry<String, String> entry: item.properties.entrySet()) {
            b.addProperty(entry.getKey(), entry.getValue());
        }
        return b.build();
    }

    /**
     * Tries to load {@code Module} implementation of a given name.
     * 
     * @throws ModuleConfigException
     *             If there is a problem with the class
     */
    private Class<? extends Module> loadClass(String className) {
        try {
            Class<?> rawClass = Class.forName(className);
            return rawClass.asSubclass(Module.class);
        } catch (ClassNotFoundException e) {
            logger.error("Class not found: " + className);
            throw new ModuleConfigException(e);
        } catch (ClassCastException e) {
            logger.error("Class is not a Module: " + className);
            throw new ModuleConfigException(e);
        }
    }

    /**
     * Adds module to the table
     */
    private void addModule(ModuleDescriptor desc) {
        descriptors.put(desc.name, desc);
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
