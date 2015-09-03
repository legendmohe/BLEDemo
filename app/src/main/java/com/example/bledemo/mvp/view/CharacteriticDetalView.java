package com.example.bledemo.mvp.view;

import java.util.Map;

import com.example.bledemo.model.BLECharacteristic;


public interface CharacteriticDetalView extends MVPView {
    public void updateValueUI(Map<String, String> newData);

    public void showReadCharacteristicIndicatorCompleted(
            BLECharacteristic mBleCharacteristic, boolean status);

    public void showWriteCharacteristicIndicatorCompleted(
            BLECharacteristic mBleCharacteristic, boolean status);
    
    public void showReadingCharacteristicIndicator(
            BLECharacteristic mBleCharacteristic);
    
    public void showWritingCharacteristicIndicator(
            BLECharacteristic mBleCharacteristic, String content);

    public void showWriteCharacteristicValueDialog(
            BLECharacteristic mBleCharacteristic);

    public void showEnablingNotifitionIndicator(BLECharacteristic mBleCharacteristic, boolean enabled);

    public void showGotNotifitionIndicator(byte[] value);

    public void showNotifyDescriptorValueIndicator(int intExtra);

    public void finish();
}
