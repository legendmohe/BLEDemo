package com.example.bledemo.mvp.presenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bledemo.R;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.ble.BLEManagerListener;
import com.example.bledemo.helper.GattAttributesHelper;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.mvp.view.ServiceListView;
import com.example.bledemo.service.BluetoothLeService;
import com.example.bledemo.view.ServiceListActivity;

public class ServiceListPresenter extends MVPActivityPresenter implements
        BLEManagerListener {

    protected static final String TAG = "ServiceListPresenter";

    private WeakReference<ServiceListView> mServiceListView;

    private BLEDevice mBleDevice;

    private Bundle mSaveStateBundle;

    public ServiceListPresenter(ServiceListView serviceListView,
            BLEDevice device) {
        mServiceListView = new WeakReference<ServiceListView>(serviceListView);
        mBleDevice = device;
    }
    
    @Override
    public void onActivityCreate() {
        
    }

    @Override
    public void onActivityStart() {
        if (BLEManager.getInstance().getState() == BLEManager.STATE_DISCONNECTED) {
            mServiceListView.get().finish();
            return;
        }

        BLEManager.getInstance().addListener(this);
        if (mSaveStateBundle == null || !mSaveStateBundle.getBoolean(ServiceListActivity.SAVE_STATE_DO_NOT_DISCORVER, false)) {
            BLEManager.getInstance().discoverServices();
        }
    }

    @Override
    public void onActivityResume() {

    }

    @Override
    public void onActivityPause() {

    }

    @Override
    public void onActivityStop() {
        BLEManager.getInstance().removeListener(this);
    }

    @Override
    public void onActivityDestroy() {
        BLEManager.getInstance().disconnect();
    }

    public void reconnect() {
        BLEManager.getInstance().reconnect();
    }

    @Override
    public void onEnableLeScan(boolean enable) {}
    
    public void onBackPressed() {
        BLEManager.getInstance().disconnect();
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {}

    @Override
    public void onInitializingConnection(boolean success) {
        if (!success && mServiceListView.get() != null) {
            Toast.makeText(mServiceListView.get().getContext(), "BLEManager init faild!", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStateChanged(int oldState, int newState, Object data) {
        
        switch (newState) {
            case BLEManager.STATE_CONNECTING:

                break;
            case BLEManager.STATE_CONNECTED:

                break;
            case BLEManager.STATE_DISCONNECTING:

                break;
            case BLEManager.STATE_DISCONNECTED:
                mServiceListView.get().finish();
                break;
            case BLEManager.STATE_DISCOVERING:
                mServiceListView.get().showDiscoveringIndicator();
                break;
            case BLEManager.STATE_DISCOVERED:
                mServiceListView.get().showDiscoveredIndicator();
                mServiceListView.get().showBluetoothGattServices((ArrayList<BLEService>)data);
                break;

            default:
                break;
        }
    }

    /**
     * @param mSaveStateBundle the mSaveStateBundle to set
     */
    public void setSaveStateBundle(Bundle mSaveStateBundle) {
        this.mSaveStateBundle = mSaveStateBundle;
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
