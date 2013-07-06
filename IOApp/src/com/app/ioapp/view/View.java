package com.app.ioapp.view;

import com.app.ioapp.customDroidViews.AbstractModuleView;

/**
 * Represents a facade between android module views and modules 
 *
 */
public interface View {
	
	/**
	 * Registers a module view to a module with given name. Registered module views will be informed about changes in module.
	 * @param moduleView
	 * @param moduleName
	 */
	void register(Class<? extends AbstractModuleView> moduleView, String moduleName);

}
