package com.example.bledemo.mvp.presenter;

import java.lang.ref.WeakReference;

import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.mvp.view.CharacteristicListView;

public class CharacteristicListPresenter extends MVPActivityPresenter {
    protected static final String TAG = "CharacteristicListPresenter";

    private WeakReference<CharacteristicListView> mCharacteristicListView;
    private BLEService mService;
    
    public CharacteristicListPresenter(CharacteristicListView characteristicListView, BLEService service) {
        mCharacteristicListView = new WeakReference<CharacteristicListView>(characteristicListView);
        mService = service;
    }
    
    @Override
    public void onActivityCreate() {
        mCharacteristicListView.get().showBluetoothGattCharacteristics(mService.getBLECharacteristics());
    }
    
    @Override
    public void onActivityStart() {
        if (BLEManager.getInstance().getState() == BLEManager.STATE_DISCONNECTED) {
            mCharacteristicListView.get().finish();
            return;
        }
    }
}
