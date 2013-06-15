package com.app.ioapp.customDroidViews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.app.ioapp.modules.InfrastructureModule;
import com.app.ioapp.modules.Module;

public abstract class AbstractModuleView extends View {

	public AbstractModuleView(Context context) {
		super(context);
	}

	public AbstractModuleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundColor(Color.GREEN);
	}

	public AbstractModuleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public abstract void setModuleImpl(Module m);
	
	/**
	 * used to force view to update on information it's logic has.
	 */
	public abstract void refresh();

}
