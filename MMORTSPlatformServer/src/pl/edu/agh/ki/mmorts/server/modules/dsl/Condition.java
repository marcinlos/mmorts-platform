package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * Most general condition interface, used by the control instructions of the
 * DSL.
 */
public interface Condition {

    boolean holds();

}
