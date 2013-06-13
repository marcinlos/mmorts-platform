package com.app.ioapp.customDroidViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

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
public class AdditionalViewA extends View {

	
	
	public AdditionalViewA(Context context) {
		super(context);
	}
	public AdditionalViewA(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public AdditionalViewA(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
