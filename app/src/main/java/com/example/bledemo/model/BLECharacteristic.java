package com.example.bledemo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.bledemo.helper.GattAttributesHelper;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

public class BLECharacteristic extends BluetoothGattCharacteristic {

    private BluetoothGattCharacteristic mCharacteristic;
    private ArrayList<BLEDescriptor> mBleDescriptors;

    public BLECharacteristic(UUID uuid, int properties, int permissions) {
        super(uuid, properties, permissions);
    }
    public BLECharacteristic(BluetoothGattCharacteristic characteristic, String unknownName, String unknownDescName) {
        this(characteristic.getUuid(), characteristic.getProperties(), characteristic.getPermissions());
        this.setName(GattAttributesHelper.lookup(characteristic.getUuid().toString(), unknownName));
        this.mCharacteristic = characteristic;
        
        mBleDescriptors = new ArrayList<>();
        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
            BLEDescriptor newDescriptor = new BLEDescriptor(descriptor, unknownDescName);
            mBleDescriptors.add(newDescriptor);
        }
    }
    
    @Override
    public int getInstanceId() {
        return mCharacteristic.getInstanceId();
    }
    
    public BluetoothGattCharacteristic getInternalCharacteristic() {
        return mCharacteristic;
    }
    
    public List<BluetoothGattDescriptor> getDescriptors() {
        return this.mCharacteristic.getDescriptors();
    }
    
    @Override
    public BluetoothGattDescriptor getDescriptor(UUID uuid) {
        return mCharacteristic.getDescriptor(uuid);
    }
    
    public List<BLEDescriptor> getBLEDescriptors() {
        return mBleDescriptors;
    }
    
    public BLEDescriptor getBleDescriptor(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (BLEDescriptor bleDescriptor : mBleDescriptors) {
            if (bleDescriptor.getUuid().compareTo(uuid) == 0) {
                return bleDescriptor;
            }
        }
        return null;
    }
    
    @Override
    public byte[] getValue() {
        return mCharacteristic.getValue();
    }
    
    @Override
    public String getStringValue(int offset) {
        return mCharacteristic.getStringValue(offset);
    }
    
    @Override
    public Float getFloatValue(int formatType, int offset) {
        return mCharacteristic.getFloatValue(formatType, offset);
    }
    
    @Override
    public Integer getIntValue(int formatType, int offset) {
        return mCharacteristic.getIntValue(formatType, offset);
    }
    
    @Override
    public boolean setValue(byte[] value) {
        return mCharacteristic.setValue(value);
    }
    
    @Override
    public boolean setValue(int mantissa, int exponent, int formatType,
            int offset) {
        return mCharacteristic.setValue(mantissa, exponent, formatType, offset);
    }
    
    @Override
    public boolean setValue(int value, int formatType, int offset) {
        return mCharacteristic.setValue(value, formatType, offset);
    }
    
    @Override
    public boolean setValue(String value) {
        return mCharacteristic.setValue(value);
    }
    
    @Override
    public void setWriteType(int writeType) {
        mCharacteristic.setWriteType(writeType);
    }
    
    @Override
    public BluetoothGattService getService() {
        return mCharacteristic.getService();
    }
    
    @Override
    public int getWriteType() {
        return mCharacteristic.getWriteType();
    }
    
    public String getPermissionsString() {
        return GattAttributesHelper.parsePermissions(getPermissions());
    }
    
    public String getPropertiesString() {
        return GattAttributesHelper.parseProperties(getProperties());
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

    private String name;
}
