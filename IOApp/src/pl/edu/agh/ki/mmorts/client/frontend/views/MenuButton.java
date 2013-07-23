package pl.edu.agh.ki.mmorts.client.frontend.views;

import pl.edu.agh.ki.mmorts.client.frontend.activities.RunningActivity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;


public class MenuButton extends Button {

	private AbstractModuleView v;
	private RunningActivity context;
	
	public MenuButton(Context context) {
		super(context);
		if(context instanceof RunningActivity)
			this.context = (RunningActivity) context;
	}
	public MenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(context instanceof RunningActivity)
			this.context = (RunningActivity) context;
	}
	public MenuButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(context instanceof RunningActivity)
			this.context = (RunningActivity) context;
	}
	
	public void setView(AbstractModuleView view){
		v = view;
	}
	
	public AbstractModuleView getView(){
		return v;
	}
	
	public void iWasClicked(){
		System.out.println("Clicked!!!");
	}

}
