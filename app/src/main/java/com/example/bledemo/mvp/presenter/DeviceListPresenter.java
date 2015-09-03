package com.example.bledemo.mvp.presenter;

import java.lang.ref.WeakReference;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.ble.BLEManagerListener;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.mvp.view.DeviceListView;
import com.example.bledemo.util.StringUtil;
import com.example.bledemo.view.ServiceListActivity;

public class DeviceListPresenter extends MVPPresenter implements BLEManagerListener{
    
    public static final String TAG = "DeviceListPresenter";

    public static final String EXTRA_SELECTED_DEVICE = "EXTRA_SELECTED_DEVICE";
    
    private WeakReference<DeviceListView> mDeviceListviewReference;
    
    public DeviceListPresenter(DeviceListView deviceListView) {
        this.mDeviceListviewReference = new WeakReference<DeviceListView>(deviceListView);
    }

    @Override
    public void start() { 
//        BLEManager.getInstance().setBleServices(null);
        BLEManager.getInstance().addListener(this);
        BLEManager.getInstance().scanLeDevice(true); 
        mDeviceListviewReference.get().clearUI();
        
        if (BLEManager.getInstance().getState() == BLEManager.STATE_CONNECTED) {
            Context context = mDeviceListviewReference.get().getContext();
            Intent intent = new Intent(context, ServiceListActivity.class);
            intent.putExtra(EXTRA_SELECTED_DEVICE, BLEManager.getInstance().getConnectedDevice());
            context.startActivity(intent);
        }
    }

    @Override
    public void stop() {
        BLEManager.getInstance().scanLeDevice(false);
        BLEManager.getInstance().removeListener(this);
        if (mDeviceListviewReference.get() == null) {
            return;
        }
    }
    
    @Override
    public void onEnableLeScan(boolean enable) {
        mDeviceListviewReference.get().changeScanMenuState(enable);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d(TAG, "find:" + device);
        if (mDeviceListviewReference.get() == null) {
            return;
        }
        BLEDevice newDevice = new BLEDevice(device.getName(), rssi, device.getAddress());
        newDevice.setDump(StringUtil.ByteArrayToHexString(scanRecord));

        DeviceListView deviceListView = mDeviceListviewReference.get();
        int pos = deviceListView.devicePosInList(newDevice);
        if (pos == -1) {
            deviceListView.addDevice(newDevice);
        }else {
            deviceListView.updateDevice(pos, newDevice);
        }
    }

    public void onScanMenuPress() {
        if (BLEManager.getInstance().isScanning()) {
            BLEManager.getInstance().scanLeDevice(false);
        }else {
            BLEManager.getInstance().scanLeDevice(true);
        }
    }
    
    public void onConnectingDialogCancel() {
        if (BLEManager.getInstance().getState() == BLEManager.STATE_CONNECTED
                || BLEManager.getInstance().getState() == BLEManager.STATE_CONNECTING) {
            BLEManager.getInstance().disconnect();
        }
    }

    @Override
    public void onInitializingConnection(boolean success) {
        if (!success && mDeviceListviewReference.get() != null) {
            Toast.makeText(mDeviceListviewReference.get().getContext(), "BLEManager init faild!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStateChanged(int oldState, int newState, Object data) {
        
        Context context = mDeviceListviewReference.get().getContext();
        switch (newState) {
            case BLEManager.STATE_CONNECTING:
                mDeviceListviewReference.get().showConnectingDeviceIndicator((BLEDevice)data);
                break;
            case BLEManager.STATE_CONNECTED:
                Intent intent = new Intent(context, ServiceListActivity.class);
                intent.putExtra(EXTRA_SELECTED_DEVICE, (BLEDevice)data);
                context.startActivity(intent);
                break;
            case BLEManager.STATE_DISCONNECTED:
                mDeviceListviewReference.get().showDisconnectedIndicator((BLEDevice)data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotifyCharacteriticValue(byte[] value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onChangeDescriptorValueCfm(UUID uuid, int intExtra) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onChangeCharacteristicValueCfm(boolean isRead, UUID uuid, int status) {
        // TODO Auto-generated method stub
        
    }
}
