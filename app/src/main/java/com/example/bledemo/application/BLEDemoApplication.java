package com.example.bledemo.application;

import com.example.bledemo.ble.BLEManager;

import android.app.Application;

public class BLEDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        BLEManager.initManager(getApplicationContext());
    }
}
