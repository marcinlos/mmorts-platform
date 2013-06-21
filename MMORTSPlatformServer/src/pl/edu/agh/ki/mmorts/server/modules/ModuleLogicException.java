package pl.edu.agh.ki.mmorts.server.modules;

/**
 * Base class for exceptions denoting module processing errors caused by the
 * legitimate logic obstacles to commit, like unmet requirements or violated
 * constraints.
 * 
 * @author los
 * 
 */
public class ModuleLogicException extends RuntimeException {

    public ModuleLogicException() {
        // empty
    }

    public ModuleLogicException(String message) {
        super(message);
    }

    public ModuleLogicException(Throwable cause) {
        super(cause);
    }

    public ModuleLogicException(String message, Throwable cause) {
        super(message, cause);
    }

}
