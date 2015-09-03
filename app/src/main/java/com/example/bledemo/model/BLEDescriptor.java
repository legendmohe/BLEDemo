package com.example.bledemo.model;

import java.util.List;
import java.util.UUID;

import com.example.bledemo.helper.GattAttributesHelper;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

public class BLEDescriptor extends BluetoothGattDescriptor {

    private BluetoothGattDescriptor mDescriptor;

    public BLEDescriptor(BluetoothGattDescriptor descriptor, String unknownName) {
        this(descriptor.getUuid(), descriptor.getPermissions());
        this.setName(GattAttributesHelper.lookup(descriptor.getUuid().toString(), unknownName));
        this.mDescriptor = descriptor;
    }

    public BLEDescriptor(UUID uuid, int permissions) {
        super(uuid, permissions);
    }

    public String getPermissionsString() {
        return GattAttributesHelper.parsePermissions(getPermissions());
    }
    
    public BluetoothGattDescriptor getInternalDescriptor() {
        return mDescriptor;
    }
    
    @Override
    public byte[] getValue() {
        return mDescriptor.getValue();
    }
    
    @Override
    public BluetoothGattCharacteristic getCharacteristic() {
        return mDescriptor.getCharacteristic();
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
