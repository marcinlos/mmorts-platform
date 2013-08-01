package pl.edu.agh.ki.mmorts.client.frontend.views;

import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


/**
 * Example implementation of a button that goes to Main menu of the app on the left.
 * This implementation is completely free to be changed by implementators of Presenters that
 * need them. They can be used for module-specific activities or calling presenters methods
 * if it needs it, though note that once set on the menu they will stay there regardless of which
 * view is currently on top.
 *
 */
public class MenuButton extends Button {

	private View v;
	private MainSpaceManager msm;
	private String presentersId;
	
	
	public MenuButton(Context context) {
		super(context);
	}
	public MenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MenuButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setView(View view){
		v = view;
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iWasClicked();
				
			}
		});
	}
	
	public void setMSM(MainSpaceManager m){
		msm = m;
	}
	
	public void setId(String id){
		presentersId = id;
	}
	
	public View getView(){
		return v;
	}
	
	public void iWasClicked(){
		msm.toTop(presentersId);
	}

}
