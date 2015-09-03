package com.example.bledemo.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.bledemo.R;
import com.example.bledemo.adapter.CharacteristicListAdapter;
import com.example.bledemo.adapter.ServiceListAdapter;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.model.BLECharacteristic;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.mvp.presenter.CharacteristicListPresenter;
import com.example.bledemo.mvp.view.CharacteristicListView;

public class CharacteristicListActivity extends Activity implements CharacteristicListView {
    public static final String TAG = "CharacteristicListActivity";
    
    private CharacteristicListPresenter mCharacteristicListPresenter;
    private CharacteristicListAdapter mCharacteristicListAdapter;

    private int mServiceIndex;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.characteristic_list_activity);
        
        Intent intent = getIntent();
        mServiceIndex = intent.getIntExtra(ServiceListAdapter.EXTRA_SELECTED_SERVICE_INDEX, -1);
        if (mServiceIndex < 0) {
            Log.e(TAG, "invaild service index.");
            finish();
            return;
        }
        
        setupViews(findViewById(R.id.characteristic_activity_root));
        
        BLEService bleService = BLEManager.getInstance().getBleServices().get(mServiceIndex);
        mCharacteristicListPresenter = new CharacteristicListPresenter(this, bleService);
        mCharacteristicListPresenter.onActivityCreate();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mCharacteristicListPresenter.onActivityResume();
    }
    
    @Override
    protected void onPause() {
        mCharacteristicListPresenter.onActivityPause();
        super.onPause();
    }
    
    @Override
    protected void onStart() {
        mCharacteristicListPresenter.onActivityStart();
        super.onStart();
    }
    
    @Override
    protected void onStop() {
        mCharacteristicListPresenter.onActivityStop();
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        mCharacteristicListPresenter.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void setupViews(View rootView) {
        mCharacteristicListAdapter = new CharacteristicListAdapter(getContext(), mServiceIndex);
        ListView characteristicListView = (ListView) rootView.findViewById(R.id.characteristic_listview);
        characteristicListView.setAdapter(mCharacteristicListAdapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showBluetoothGattCharacteristics(
            List<BLECharacteristic> characteristics) {
        mCharacteristicListAdapter.addAll(characteristics);
        mCharacteristicListAdapter.notifyDataSetChanged();
    }
}
