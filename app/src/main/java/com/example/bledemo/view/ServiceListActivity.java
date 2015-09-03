package com.example.bledemo.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.bledemo.R;
import com.example.bledemo.adapter.DeviceListAdapter;
import com.example.bledemo.adapter.ServiceListAdapter;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.mvp.presenter.DeviceListPresenter;
import com.example.bledemo.mvp.presenter.ServiceListPresenter;
import com.example.bledemo.mvp.view.ServiceListView;
import com.example.bledemo.service.BluetoothLeService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ServiceListActivity extends Activity implements ServiceListView {
    public static final String TAG = "ServiceListActivity";

    public static final String SAVE_STATE_DO_NOT_DISCORVER = "SAVE_STATE_DO_NOT_DISCORVER";
    
    private ServiceListPresenter mServiceListPresenter;
    private ServiceListAdapter mServiceListAdapter;

    private ProgressDialog mProgressDialog;

    private Bundle mSaveInstanceState;
    
    public void clearUI() {
        mServiceListAdapter.clear();
        mServiceListAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_list_activity);
        
        Intent intent = getIntent();
        BLEDevice bleDevice = intent.getParcelableExtra(DeviceListPresenter.EXTRA_SELECTED_DEVICE);
        if (bleDevice == null) {
            Log.e(TAG, "null ble device.");
            finish();
            return;
        }
        
        setupViews(findViewById(R.id.service_activity_root));
        
        mServiceListPresenter = new ServiceListPresenter(this, bleDevice);
        mSaveInstanceState = savedInstanceState;
        mServiceListPresenter.setSaveStateBundle(savedInstanceState);
        mServiceListPresenter.onActivityCreate();
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mServiceListPresenter.setSaveStateBundle(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mServiceListPresenter.onActivityResume();
    }
    
    @Override
    protected void onPause() {
        mServiceListPresenter.onActivityPause();
        super.onPause();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mServiceListPresenter.setSaveStateBundle(mSaveInstanceState);
        mServiceListPresenter.onActivityStart();
    }
    
    @Override
    protected void onStop() {
        mServiceListPresenter.onActivityStop();
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
        mServiceListPresenter.onActivityDestroy();
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mSaveInstanceState = outState;
        outState.putBoolean(SAVE_STATE_DO_NOT_DISCORVER, true);
        super.onSaveInstanceState(outState);
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.service_list_activity, menu);
//        Log.d(TAG, "onCreateOptionsMenu:" + BLEManager.getInstance().getState());
//        if (BLEManager.getInstance().getState() == BLEManager.STATE_CONNECTING) {
//            menu.findItem(R.id.action_reconnect).setVisible(false);
//        }else {
//            menu.findItem(R.id.action_reconnect).setVisible(true);
//        }
//        return true;
//    }
//    
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        Log.d(TAG, "onCreateOptionsMenu:" + BLEManager.getInstance().getState());
//        if (BLEManager.getInstance().getState() == BLEManager.STATE_CONNECTING) {
//            menu.findItem(R.id.action_reconnect).setVisible(false);
//        }else {
//            menu.findItem(R.id.action_reconnect).setVisible(true);
//        }
//        return true;
//    }
//    
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.action_reconnect:
//                mServiceListPresenter.reconnect();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void setupViews(View rootView) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface arg0) {
                finish();
            }
        });
        
        mServiceListAdapter = new ServiceListAdapter(getContext());
        ListView serviceListView = (ListView) rootView.findViewById(R.id.service_listview);
        serviceListView.setAdapter(mServiceListAdapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showBluetoothGattServices(ArrayList<BLEService> gattServiceData) {
        mServiceListAdapter.clear();
        mServiceListAdapter.addAll(gattServiceData);
        mServiceListAdapter.notifyDataSetChanged();
//        this.invalidateOptionsMenu();
    }

    @Override
    public void showDiscoveringIndicator() {        
        mProgressDialog.setMessage("discovering");
        mProgressDialog.show();
//        this.invalidateOptionsMenu();
    }

    @Override
    public void showDiscoveredIndicator() {
        mProgressDialog.dismiss();
    }
}
