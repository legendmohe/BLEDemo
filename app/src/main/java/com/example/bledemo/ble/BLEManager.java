package com.example.bledemo.ble;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.bledemo.R;
import com.example.bledemo.constant.BLEConstant;
import com.example.bledemo.model.BLECharacteristic;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.service.BluetoothLeService;
import com.example.bledemo.util.StringUtil;

public class BLEManager {
    
    public static final String TAG = "BLEManager";
    
    private ArrayList<BLEService> gBleServices;
    
    private WeakReference<Context> mContextReference;
    private BluetoothAdapter mBluetoothAdapter;
    private HashSet<BLEManagerListener> mListeners;
    private boolean mScanning;
    private Handler mHandler;
    
    public static final int STATE_CONNECTING = 0;
    public static final int STATE_CONNECTED = 1;
    public static final int STATE_DISCOVERING = 2;
    public static final int STATE_DISCOVERED = 3;
    public static final int STATE_DISCONNECTING = 4;
    public static final int STATE_DISCONNECTED = 5;
    
    private BluetoothLeService mBluetoothLeService;
    private BLEDevice mBleDevice;
    private int mState = STATE_DISCONNECTED;
    
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                notifyListenersOnInitializingConnection(false);
                return;
            }
            notifyListenersOnInitializingConnection(true);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive:" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                notifyListenersOnStateChanged(STATE_CONNECTED, mBleDevice);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                notifyListenersOnStateChanged(STATE_DISCONNECTED, mBleDevice);
                mBleDevice = null;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_CHARACTERISTIC_READ.equals(action)) {
                displayCharacteriticRead(
                        intent.getSerializableExtra(BluetoothLeService.EXTRA_UUID),
                        intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_FAILURE)
                        );
            } else if (BluetoothLeService.ACTION_CHARACTERISTIC_WRITE.equals(action)) {
                displayCharacteriticWrite(
                        intent.getSerializableExtra(BluetoothLeService.EXTRA_UUID),
                        intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_FAILURE)
                        );
            } else if (BluetoothLeService.ACTION_GOT_NOTIFICATION.equals(action)) {
                notifyCharacteriticValue(intent.getByteArrayExtra(BluetoothLeService.EXTRA_VALUE));
            } else if (BluetoothLeService.ACTION_DESCRIPTOR_READ.equals(action)) {
                notifyChangeDescriptorValueCfm(
                        intent.getSerializableExtra(BluetoothLeService.EXTRA_UUID),
                        intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_FAILURE)
                        );
            } else if (BluetoothLeService.ACTION_DESCRIPTOR_WRITE.equals(action)) {
                notifyChangeDescriptorValueCfm(
                        intent.getSerializableExtra(BluetoothLeService.EXTRA_UUID),
                        intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_FAILURE)
                        );
            }
        }
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                byte[] scanRecord) {
            final int RSSI = rssi;
            final byte[] ScanRecord = scanRecord;
            mHandler.post(new Runnable() {
               @Override
               public void run() {
                   Log.d(TAG, "scan:" + device.getAddress());
                   notifyListenersOnLeScan(device, RSSI, ScanRecord);
               }
           });
       }
    };

    public static BLEManager getInstance(){
        return SingletonHolder.INSTANCE;
    }


    private BLEManager() {
        mHandler = new Handler();
        mListeners = new HashSet<>();
    }
    
    public boolean isBtEnable(){
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "BluetoothAdapter is invaild.");
            return false;
        }
        return mBluetoothAdapter.isEnabled();
    }
    
    public void btEnable(boolean enable) {
        Log.i(TAG, "set btEnable:" + enable);
        if (mBluetoothAdapter == null) {
            return;
        }
        if (enable) {
            mBluetoothAdapter.enable();            
        }else {
            mBluetoothAdapter.disable();
        }
    }
    
    public boolean isScanning() {
        return mScanning;
    }

    // Stops scanning after 10 seconds.
    public void scanLeDevice(final boolean enable) {
        Log.d(TAG, "start scan:" + enable);
        
        if (mBluetoothAdapter == null) {
            return;
        }
        // Stops scanning after a pre-defined scan period.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "stop scan.");
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                notifyListenersOnScanLeDevice(mScanning);
            }
        };
        if (enable) {
            mScanning = true;
            mHandler.postDelayed(runnable, BLEConstant.SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            notifyListenersOnScanLeDevice(mScanning);
        } else {
            mScanning = false;
            mHandler.removeCallbacks(runnable);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            notifyListenersOnScanLeDevice(mScanning);
        }
    }
    
    public void connect(BLEDevice bleDevice) {
        mBleDevice = bleDevice;
        if (mBluetoothLeService != null) {
            notifyListenersOnStateChanged(STATE_CONNECTING, mBleDevice);
            final boolean result = mBluetoothLeService.connect(mBleDevice.getMac());
            Log.d(TAG, "ServiceConnection Connect request result=" + result);
        }
    }
    
    public void disconnect() {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
        }
    }
    
    public void reconnect() {
//        BLEManager.getInstance().setBleServices(null);
        if (mBluetoothLeService == null) {
            bindLeService();
        }else {
            mBluetoothLeService.disconnect();
            notifyListenersOnStateChanged(STATE_CONNECTING);
            final boolean result = mBluetoothLeService.connect(mBleDevice.getMac());
            Log.d(TAG, "Connect request result=" + result);
        }
    }
    
    public void discoverServices() {
        notifyListenersOnStateChanged(STATE_DISCOVERING, mBleDevice);
        mBluetoothLeService.discoverServices();
    }
    
    public int getState() {
        return mState;
    }
    
    public int getInternalLeServiceState() {
        return mBluetoothLeService.getConnectionState();
    }
    
    public BLEDevice getConnectedDevice() {
        return mBleDevice;
    }
    
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        Context context = mContextReference.get();
        String unknownServiceString = context.getResources().getString(R.string.unknown_service);
        String undefineCharNname = context.getString(R.string.unknown_characteristic);
        String undefineDescNname = context.getString(R.string.unknown_descriptor);
        ArrayList<BLEService> gattServiceData = new ArrayList<BLEService>();
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            BLEService bleService = new BLEService(
                    gattService,
                    unknownServiceString,
                    undefineCharNname,
                    undefineDescNname);
            gattServiceData.add(bleService);
        }
        
        Log.d(TAG, "displayGattServices. size=" + gattServiceData.size());
        BLEManager.getInstance().setBleServices(gattServiceData);
        
//        mServiceListView.get().showBluetoothGattServices(gattServiceData);
        notifyListenersOnStateChanged(STATE_DISCOVERED, gattServiceData);
    }

    protected void displayCharacteriticWrite(Serializable object, int status) {
        notifyListenersOnChangeCharacteristicValueCfm(false, (UUID)object, status);
    }

    protected void displayCharacteriticRead(Serializable object, int status) {
        notifyListenersOnChangeCharacteristicValueCfm(true, (UUID)object, status);
    }

    private void notifyCharacteriticValue(byte[] value) {
        notifyListenersOnNotifyCharacteriticValue(value);
    }
    
    
    protected void notifyChangeDescriptorValueCfm(Serializable serializable, int intExtra) {
        notifyListenersOnChangeDescriptorValueCfm((UUID) serializable, intExtra);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_CHARACTERISTIC_READ);
        intentFilter.addAction(BluetoothLeService.ACTION_CHARACTERISTIC_WRITE);
        intentFilter.addAction(BluetoothLeService.ACTION_GOT_NOTIFICATION);
        intentFilter.addAction(BluetoothLeService.ACTION_DESCRIPTOR_READ);
        intentFilter.addAction(BluetoothLeService.ACTION_DESCRIPTOR_WRITE);
        return intentFilter;
    }
    
    /* -----------------------------------------------------------------*/

    /* -----------------------------------------------------------------*/
    
    public static void initManager(Context context) {
        if (context != null) {
            BLEManager.getInstance().setContext(context);
            BLEManager.getInstance().init();
        }
    }
    
    public static BluetoothAdapter getAdapterInstance(Context context) {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        return bluetoothManager.getAdapter();
    }
    
    private void setContext(Context context) {
        if (context == null) {
            return;
        }
        mContextReference = new WeakReference<Context>(context);
    }
    
    private void init() {
        if (mContextReference.get() == null) {
            return;
        }
        mBluetoothAdapter = getAdapterInstance(mContextReference.get());
        bindLeService();
    }
    
    public void bindLeService() {
        Context context = mContextReference.get();
        if (context == null) {
            return;
        }
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }
    
    public void unbindLeService() {
        Context context = mContextReference.get();
        if (context == null) {
            return;
        }
        if (mBluetoothLeService != null) {
            context.unbindService(mServiceConnection);
            mBluetoothLeService = null;
        }
        context.unregisterReceiver(mGattUpdateReceiver);
    }
    
    public void addListener(BLEManagerListener listener) {
        if (listener != null) {
            mListeners.add(listener);
        }
    }
    
    public void removeListener(BLEManagerListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }
    
    private void notifyListenersOnScanLeDevice(final boolean enable) {
        for (BLEManagerListener listener : mListeners) {
            listener.onEnableLeScan(enable);
        }
    }
    
    private void notifyListenersOnLeScan(final BluetoothDevice device, int rssi,
            byte[] scanRecord) {
        for (BLEManagerListener listener : mListeners) {
            listener.onLeScan(device, rssi, scanRecord);
        }
    }
    
    private void notifyListenersOnInitializingConnection(boolean success) {
        for (BLEManagerListener listener : mListeners) {
            listener.onInitializingConnection(success);
        }
    }
    
    private void notifyListenersOnStateChanged(int state) {
        notifyListenersOnStateChanged(state, null);
    }
    
    private void notifyListenersOnStateChanged(int state, Object data) {
        if (mState == state) {
            Log.i(TAG, "state not change:" + state);
            return;
        }
        Log.d(TAG, String.format("state changed, old:%s, new:%s, data:%s", mState, state, data));
        
        int oldState = mState;
        mState = state;
        for (BLEManagerListener listener : mListeners) {
            listener.onStateChanged(oldState, mState, data);
        }
    }
    
    private void notifyListenersOnChangeCharacteristicValueCfm(boolean isRead,
            UUID uuid, int status) {
        for (BLEManagerListener listener : mListeners) {
            listener.onChangeCharacteristicValueCfm(isRead, uuid, status);
        }
    }
    
    private void notifyListenersOnNotifyCharacteriticValue(byte[] value) {
        for (BLEManagerListener listener : mListeners) {
            listener.onNotifyCharacteriticValue(value);
        }
    }

    private void notifyListenersOnChangeDescriptorValueCfm(UUID uuid,
            int intExtra) {
        for (BLEManagerListener listener : mListeners) {
            listener.onChangeDescriptorValueCfm(uuid, intExtra);
        }
    }
    
    /* -----------------------------------------------------------------*/
    
    public void readCharacteristicValue(BLECharacteristic characteristic) {
        mBluetoothLeService.readCharacteristic(characteristic);
    }

    public void writeCharacteristicValue(BLECharacteristic characteristic,
            String value) {
        characteristic.setValue(StringUtil.hexStringToByteArray(value));
        mBluetoothLeService.writeCharacteristicValue(characteristic);
    }
    
    public void setCharacteristicNotify(BLECharacteristic characteristic, boolean enabled) {
        mBluetoothLeService.setCharacteristicNotification(characteristic, enabled);
    }
    
    /* -----------------------------------------------------------------*/
    
    /**
     * @return the gBleServices
     */
    public ArrayList<BLEService> getBleServices() {
        return gBleServices;
    }

    /**
     * @param gBleServices the gBleServices to set
     */
    public void setBleServices(ArrayList<BLEService> gBleServices) {
        this.gBleServices = gBleServices;
    }

    private static class SingletonHolder{
        private final static  BLEManager INSTANCE=new BLEManager();
    }

}
