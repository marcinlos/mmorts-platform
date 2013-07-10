package com.app.ioapp.customDroidViews;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;

import com.app.ioapp.view.ModulesBroker;

/**
 * Used to show additional choice after pressing menu button. Each module might have it's own
 * or it might be implemented more generically. It should be self-contained i.e. not require
 * additional touches on the normal MainActivity. If you want to interact with Board you can
 * pass it here through setter and display it.
 * 
 * It would be nice if after getting from user what it needs it cleared itself and gave incentive
 * to player to click on clearly visible "exit" button present in this views parent.
 * @author Michal
 *
 */
public class AdditionalViewA extends AbstractModuleView {

	
	
	public AdditionalViewA(Context context) {
		super(context);
	}
	public AdditionalViewA(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public AdditionalViewA(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void iWasClicked(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void init(List<String> moduleNames, ModulesBroker view) {
		// TODO Auto-generated method stub
		
	}

}
