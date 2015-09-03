package com.example.bledemo.mvp.presenter;

public abstract class MVPPresenter {

    /**
     * Called when the presenter is initialized
     */
    public abstract void start();

    /**
     * Called when the presenter is stop, i.e when an activity
     * or a fragment finishes
     */
    public abstract void stop();
}
