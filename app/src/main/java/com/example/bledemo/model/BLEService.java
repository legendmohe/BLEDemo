package com.example.bledemo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.bledemo.R;
import com.example.bledemo.helper.GattAttributesHelper;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

public class BLEService extends BluetoothGattService {
    
    private BluetoothGattService mBluetoothGattService;
    private List<BLECharacteristic> mBleCharacteristics;
    private static final String TAG = "BLEService";
    
    public BLEService(BluetoothGattService service, String unknownServiceName, String unknownCharName, String unknownDescName) {
        this(service.getUuid(), service.getType());
        this.setName(GattAttributesHelper.lookup(service.getUuid().toString(), unknownServiceName));
        mBluetoothGattService = service;
        
        mBleCharacteristics = new ArrayList<>();
        for (BluetoothGattCharacteristic bleCharacteristic : service.getCharacteristics()) {
            BLECharacteristic newCharacteristic = new BLECharacteristic(bleCharacteristic, unknownCharName, unknownDescName);
            mBleCharacteristics.add(newCharacteristic);
        }
    }
    
    public BLEService(UUID uuid, int serviceType) {
        super(uuid, serviceType);
    }
    
    @Override
    public BLECharacteristic getCharacteristic(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (BLECharacteristic bleCharacteristic : mBleCharacteristics) {
            if (bleCharacteristic.getUuid().compareTo(uuid) == 0) {
                return bleCharacteristic;
            }
        }
        return null;
    }

    public List<BLECharacteristic> getBLECharacteristics() {
        return mBleCharacteristics;
    }
    
    public String getTypeName() {
        switch (getType()) {
            case 0:
                return "Primary";
            case 1:
                return "Secondary";
            default:
                break;
        }
        return "Unknown";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mBluetoothGattService
     */
    private BluetoothGattService getBluetoothGattService() {
        return mBluetoothGattService;
    }

    private String name;
}
