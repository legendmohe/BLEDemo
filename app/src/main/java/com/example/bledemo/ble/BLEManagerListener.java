package com.example.bledemo.ble;

import java.io.Serializable;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;

public interface BLEManagerListener {
    public void onEnableLeScan(final boolean enable);
    public void onLeScan(final BluetoothDevice device, int rssi,
            byte[] scanRecord);
    public void onInitializingConnection(boolean success);
    public void onStateChanged(int oldState, int newState, Object data);
    public void onNotifyCharacteriticValue(byte[] value);
    public void onChangeDescriptorValueCfm(UUID uuid, int intExtra);
    public void onChangeCharacteristicValueCfm(boolean isRead, UUID uuid, int status);
}
