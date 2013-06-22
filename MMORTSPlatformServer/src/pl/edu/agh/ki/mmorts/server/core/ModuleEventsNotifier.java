package pl.edu.agh.ki.mmorts.server.core;

/**
 * Global module events notifier, allows adding/removing module events listener.
 * Designed to be injected to other components.
 * 
 * @author los
 * 
 */
public interface ModuleEventsNotifier {

    /**
     * Registers new {@linkplain ModuleEventsListener}
     * 
     * @param listener
     *            Listener to register
     */
    void addListener(ModuleEventsListener listener);

    /**
     * Removes previously registered {@linkplain ModuleEventsListener} from the
     * list of notified listeners. Does nothing if the listener has not been
     * registered.
     * 
     * @param listener Listener to remove
     */
    void removeListener(ModuleEventsListener listener);

}
