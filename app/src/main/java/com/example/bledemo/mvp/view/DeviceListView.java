package com.example.bledemo.mvp.view;

import java.util.ArrayList;

import com.example.bledemo.model.BLEDevice;

import android.widget.ListView;

public interface DeviceListView extends MVPView {
    
    public void addDeviceList(ArrayList<BLEDevice> devices);
    
    public void addDevice(BLEDevice device);
    
    public void updateDeviceList(ArrayList<BLEDevice> devices);
    
    public void updateDevice(int position, BLEDevice device);
    
    public void updateDevice(BLEDevice device);
    
    public void updateDeviceInfo();
    
    public int devicePosInList(BLEDevice device);
    
    public void changeScanMenuState(boolean scanning);
    
    public void showConnectingDeviceIndicator(BLEDevice device);
    
    public void showDisconnectedIndicator(BLEDevice device);

    public void clearUI();
}
