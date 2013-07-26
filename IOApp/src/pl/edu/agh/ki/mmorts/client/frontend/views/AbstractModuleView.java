package pl.edu.agh.ki.mmorts.client.frontend.views;

import java.util.List;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ConcreteModulesBroker;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.google.inject.Inject;

public abstract class AbstractModuleView extends View {
	
	@Inject(optional = true)
	protected ConcreteModulesBroker modulesBroker;
	
	protected String moduleName;
	public boolean isButton;

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
	
	/**
	 * used to force view to update on information it's logic has.
	 */
	public abstract void refresh();
	
	public abstract void iWasClicked(float x, float y);
	
	/**
	 * 
	 * @param moduleName name of the module the view will call for data
	 * @param view facade to separate modules implementation and UI classes. responsible for
	 * directing calls from UI to impl.
	 */
	public abstract void init(List<String> moduleNames, ConcreteModulesBroker view);
	

}
