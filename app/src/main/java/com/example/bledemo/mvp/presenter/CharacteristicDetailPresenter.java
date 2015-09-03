package com.example.bledemo.mvp.presenter;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.ble.BLEManagerListener;
import com.example.bledemo.helper.GattAttributesHelper;
import com.example.bledemo.model.BLECharacteristic;
import com.example.bledemo.model.BLEDescriptor;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.mvp.view.CharacteriticDetalView;
import com.example.bledemo.util.StringUtil;

public class CharacteristicDetailPresenter extends MVPActivityPresenter implements BLEManagerListener {
    
    private WeakReference<CharacteriticDetalView> mCharacteriticDetalView;
    private BLECharacteristic mBleCharacteristic;

    public CharacteristicDetailPresenter(
            CharacteriticDetalView characteriticDetalView,
            BLECharacteristic bleCharacteristic) {
        mCharacteriticDetalView = new WeakReference<CharacteriticDetalView>(characteriticDetalView);
        mBleCharacteristic = bleCharacteristic;
    }

    public List<? extends Map<String, ?>> setupDescriptorData() {
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        if (mBleCharacteristic != null) {
            for (BLEDescriptor descriptor : mBleCharacteristic.getBLEDescriptors()) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", descriptor.getName());
                map.put("uuid", descriptor.getUuid().toString());
                map.put("value", StringUtil.ByteArrayToHexString(descriptor.getValue()));
                fillMaps.add(map);
            }
        }
        return fillMaps;
    }

    public void updateValue() {
        Map<String, String> data = new HashMap<>();
        byte[] value = mBleCharacteristic.getValue();
        if (value != null && value.length != 0) {
            data.put("hex", StringUtil.ByteArrayToHexString(value));
            //
            data.put("string", mBleCharacteristic.getStringValue(0));
            //
            if (value.length == 1) {
                data.put("uint", mBleCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0).toString());
                data.put("sint", mBleCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 0).toString());
            }else if (value.length == 2) {
                data.put("uint", mBleCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0).toString());
                data.put("sint", mBleCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, 0).toString());
            }else if (value.length == 4) {
                data.put("uint", mBleCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0).toString());
                data.put("sint", mBleCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT32, 0).toString());
            }else {
                data.put("uint", "unknown");
                data.put("sint", "unknown");
            }
//            data.put("float", mBleCharacteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT, 0).toString());
//            data.put("sfloat", mBleCharacteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT, 0).toString());
            //
//            Date dateData = new Date();
//            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mCharacteriticDetalView.get().getContext());
//            data.put("stmp", dateFormat.format(dateData));
            data.put("stmp", Calendar.getInstance().getTime().toLocaleString());
        }
        mCharacteriticDetalView.get().updateValueUI(data);
    }
    
    public void readCharacteristic() {
        BLEManager.getInstance().readCharacteristicValue(mBleCharacteristic);
        mCharacteriticDetalView.get().showReadingCharacteristicIndicator(mBleCharacteristic);
    }
    
    public void writeCharacteristic(String content) {
        BLEManager.getInstance().writeCharacteristicValue(mBleCharacteristic, content);
        mCharacteriticDetalView.get().showWritingCharacteristicIndicator(mBleCharacteristic, content);
    }
    
    @Override
    public void onActivityCreate() {
        BLEManager.getInstance().addListener(this);
    }
    
    @Override
    public void onActivityDestroy() {
        BLEManager.getInstance().removeListener(this);
    }
    
    @Override
    public void onActivityStart() {
        if (BLEManager.getInstance().getState() == BLEManager.STATE_DISCONNECTED) {
            mCharacteriticDetalView.get().finish();
            return;
        }
        readCharacteristic();
    }

    @Override
    public void onEnableLeScan(boolean enable) {}

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {}

    @Override
    public void onInitializingConnection(boolean success) {}

    @Override
    public void onStateChanged(int oldState, int newState, Object data) {
        switch (newState) {
            case BLEManager.STATE_DISCONNECTING:
            case BLEManager.STATE_DISCONNECTED:
                mCharacteriticDetalView.get().finish();
                break;

            default:
                break;
        }
    }

    public void onConnectingDialogCancel() {
    }

    public void onReadBtnPressed() {
        BLEManager.getInstance().readCharacteristicValue(mBleCharacteristic);
    }

    public void onWriteBtnPressed() {
        mCharacteriticDetalView.get().showWriteCharacteristicValueDialog(mBleCharacteristic);
    }

    public void onNotifyBtnPressed(boolean enabled) {
        mCharacteriticDetalView.get().showEnablingNotifitionIndicator(mBleCharacteristic, enabled);
        BLEManager.getInstance().setCharacteristicNotify(mBleCharacteristic, enabled);
    }

    public void onIndicateBtnPressed() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onNotifyCharacteriticValue(byte[] value) {
//        mBleCharacteristic.setValue(value);
        updateValue();
        mCharacteriticDetalView.get().showGotNotifitionIndicator(value);
    }

    @Override
    public void onChangeDescriptorValueCfm(UUID uuid, int intExtra) {
        if (uuid.compareTo(UUID.fromString(GattAttributesHelper.CLIENT_CONFIG_DESCRIPTOR_UUID)) == 0) {
            mCharacteriticDetalView.get().showNotifyDescriptorValueIndicator(intExtra);
        }
    }

    @Override
    public void onChangeCharacteristicValueCfm(boolean isRead, UUID uuid, int status) {
//        mBleCharacteristic.setValue(value);
        updateValue();
        if (isRead) {
            mCharacteriticDetalView.get().showReadCharacteristicIndicatorCompleted(mBleCharacteristic, status == BluetoothGatt.GATT_SUCCESS);
        }else {
            mCharacteriticDetalView.get().showWriteCharacteristicIndicatorCompleted(mBleCharacteristic, status == BluetoothGatt.GATT_SUCCESS);
        }
    }

}
