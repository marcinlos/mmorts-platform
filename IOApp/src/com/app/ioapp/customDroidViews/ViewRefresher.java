package com.app.ioapp.customDroidViews;

import android.view.View;

import java.util.TimerTask;

public class ViewRefresher extends TimerTask {

    private final View view;

    public ViewRefresher(View view) {
        this.view = view;
    }

    @Override
    public void run() {
        view.postInvalidate();
    }
}
