package com.app.ioapp.modules;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This interface represents logic of a module without communication. Communication is in 
 * {@code CommunicatingModule}
 *
 */
public interface Module {
    /**
     * Called in the first phase of moduleClass initialization, before the
     * communication environment is fully operational.
     * @param properties 
     */
    void init(Properties properties);
    
    /**
     * Called by {@code Synchronizer after getting state from server
     * @param properties 
     */
    void setSynchronizedState(Properties properties);
    
    /**
     * Called by (@link #MainActivity}. Used to get views connected with this module
     * to dynamically create menu.
     * @return text to place on the button as key, and name of the view class connected with said
     * button as value
     */
    Map<String,String> getMenus();

}
