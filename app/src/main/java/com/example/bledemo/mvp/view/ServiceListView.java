package com.example.bledemo.mvp.view;

import java.util.ArrayList;

import com.example.bledemo.model.BLEService;

public interface ServiceListView extends MVPView  {
    public void clearUI();
    public void finish();
    public void showBluetoothGattServices(ArrayList<BLEService> gattServiceData);
    public void showDiscoveringIndicator();
    public void showDiscoveredIndicator();
}
