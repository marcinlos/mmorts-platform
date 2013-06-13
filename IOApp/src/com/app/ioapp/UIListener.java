package com.app.ioapp;

public interface UIListener {
	
	/**
	 * It should probably be a series of methods, each taking care of specific event
	 * that UI is interested in
	 * @param whathappend a way to describe what happened? could be more arguments , could not be Object
	 */
	public void stuffHappened(Object whathappend);
	
	/**
	 * called by module whose button was clicked to show additional menu
	 * should switch (name) and show proper additional menu based on who sent it
	 * @param name module name (one module == one menu item)
	 */
	public void showMenuForModule(String name);

}
