package com.example.bledemo.mvp.view;

import java.util.List;

import com.example.bledemo.model.BLECharacteristic;

public interface CharacteristicListView extends MVPView {
    void showBluetoothGattCharacteristics(List<BLECharacteristic> characteristics);

    void finish();
}
