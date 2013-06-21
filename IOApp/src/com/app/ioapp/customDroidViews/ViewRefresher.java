package com.app.ioapp.customDroidViews;

import android.view.View;

import java.util.TimerTask;

/**
 * This class should be used within Views that need to update their contents from time to time.
 * @author Michal
 *
 */
public class ViewRefresher extends TimerTask {

    private final AbstractModuleView view;

    public ViewRefresher(AbstractModuleView view) {
        this.view = view;
    }

    @Override
    public void run() {
        view.postInvalidate();
    }
}
