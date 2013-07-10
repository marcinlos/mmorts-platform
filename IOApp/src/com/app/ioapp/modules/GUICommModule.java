package com.app.ioapp.modules;

public interface GUICommModule extends Module {

	/**
     * Method needed for performance optimisation (DroidView should only pull info when there was a change).
     * @return whether the module has any changes that were not yet sent
     */
    public abstract boolean isStateChanged();
    /**
     * tells the module that pulling data is complete and view is up to date
     */
    public abstract void stateReceived();
    /**
     * User did something on DroidView that requires action. This method informs module what to do
     * @param data arbitrary data structure that identifies operation and arguments. Needs to be consistent
     * only in DroidView-Module pair, each pair can have independent implementation.
     */
    public abstract <T> void setData(T data, Class<T> clazz);
    /**
     * DroidView by calling this requests the state of data within the module to use it instead of it's
     * own outdated one.
     * @return arbitrary data structure (Object) holding all the relevant info. Needs to be consistent
     * only in DroidView-Module pair, each pair can have independent implementation.
     */
    public abstract <T> T getData(Class<T> clazz);
}
