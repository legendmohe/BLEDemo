package com.example.bledemo.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.bledemo.R;
import com.example.bledemo.R.id;
import com.example.bledemo.adapter.CharacteristicListAdapter;
import com.example.bledemo.adapter.ServiceListAdapter;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.helper.DialogHelper;
import com.example.bledemo.helper.GattAttributesHelper;
import com.example.bledemo.model.BLECharacteristic;
import com.example.bledemo.model.BLEDescriptor;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.mvp.presenter.CharacteristicDetailPresenter;
import com.example.bledemo.mvp.view.CharacteriticDetalView;
import com.example.bledemo.util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CharacteristicDetalActivity extends Activity implements CharacteriticDetalView {
    
    private static final String TAG = "CharacteriticDetalActivity";
    private CharacteristicDetailPresenter mCharacteristicDetailPresenter;
    private BLECharacteristic mBleCharacteristic;
    private TextView mHexTextView;
    private TextView mStringTextView;
    private TextView mIntTextView;
    private TextView mStmpTextView;
    private ProgressDialog mProgressDialog;
    private Button mReadBtn;
    private Button mWriteBtn;
    private Button mNotifyBtn;
    private Button mIndicateBtn;
    
    private boolean mEnableNofity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.characteristic_detail_activity);
        Intent intent = getIntent();
        int spos = intent.getIntExtra(CharacteristicListAdapter.EXTRA_SELECTED_SERVICE_INDEX, -1);
        if (spos < 0) {
            Log.e(TAG , "invaild service index.");
            finish();
            return;
        }
        UUID uuid = (UUID) intent.getSerializableExtra(CharacteristicListAdapter.EXTRA_SELECTED_CHARACTERISTIC_UUID);
        if (uuid == null) {
            Log.e(TAG , "invaild characteristic uuid.");
            finish();
            return;
        }
        
        
        BLEService bleService = BLEManager.getInstance().getBleServices().get(spos);
        mBleCharacteristic = bleService.getCharacteristic(uuid);
        mCharacteristicDetailPresenter = new CharacteristicDetailPresenter(this, mBleCharacteristic);
        
        setupViews(findViewById(R.id.characteristic_detail_root));
        
        mCharacteristicDetailPresenter.onActivityCreate();
    }
    
    @Override
    protected void onDestroy() {
        mCharacteristicDetailPresenter.stop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (mEnableNofity) {
            mCharacteristicDetailPresenter.onNotifyBtnPressed(!mEnableNofity);
        }
        mCharacteristicDetailPresenter.onActivityDestroy();
        super.onDestroy();
    }
    
    @Override
    protected void onStart() {
        mCharacteristicDetailPresenter.onActivityStart();
        super.onStart();
    }
    
    @Override
    protected void onStop() {
        mCharacteristicDetailPresenter.onActivityStop();
        super.onStart();
    }

    @Override
    public void setupViews(View rootView) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface arg0) {
                mCharacteristicDetailPresenter.onConnectingDialogCancel();
            }
        });
        
        ListView descListView = (ListView) rootView.findViewById(R.id.characteristic_desc_listview);
        
        String[] from = new String[] {"name", "uuid", "value"};
        int[] to = new int[] { R.id.descriptor_name_textview, R.id.descriptor_uuid_textview, id.descriptor_value_textview};
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getContext(),
                mCharacteristicDetailPresenter.setupDescriptorData(),
                R.layout.descriptor_list_item_view,
                from,
                to);
        descListView.setAdapter(simpleAdapter);
        
        mHexTextView = (TextView) rootView.findViewById(R.id.characteristic_value_hex_textview);
        mStringTextView = (TextView) rootView.findViewById(R.id.characteristic_value_string_textview);
        mIntTextView = (TextView) rootView.findViewById(R.id.characteristic_value_int_textview);
        mStmpTextView = (TextView) rootView.findViewById(R.id.characteristic_value_stmp_textview);
        
        
        initBtns(rootView);
        updateBtnState();
        mCharacteristicDetailPresenter.updateValue();
    }
    
    private void initBtns(View rootView) {
        mReadBtn = (Button) rootView.findViewById(R.id.characteristic_read_btn);
        mReadBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                mCharacteristicDetailPresenter.onReadBtnPressed();
            }
        });
        
        mWriteBtn = (Button) rootView.findViewById(R.id.characteristic_write_btn);
        mWriteBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                mCharacteristicDetailPresenter.onWriteBtnPressed();
            }
        });
        
        mNotifyBtn = (Button) rootView.findViewById(R.id.characteristic_notify_btn);
        mNotifyBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                mCharacteristicDetailPresenter.onNotifyBtnPressed(!mEnableNofity);
            }
        });
        
        mIndicateBtn = (Button) rootView.findViewById(R.id.characteristic_indicate_btn);
        mIndicateBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                mCharacteristicDetailPresenter.onIndicateBtnPressed();
            }
        });
    }
    
    public void updateBtnState() {
        int property = mBleCharacteristic.getProperties();
        if (GattAttributesHelper.hasProperty(BluetoothGattCharacteristic.PROPERTY_READ, property)) {
            mReadBtn.setVisibility(View.VISIBLE);
        }
        if (GattAttributesHelper.hasProperty(BluetoothGattCharacteristic.PROPERTY_WRITE, property)) {
            mWriteBtn.setVisibility(View.VISIBLE);
        }
        if (GattAttributesHelper.hasProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY, property)) {
            mNotifyBtn.setVisibility(View.VISIBLE);
        }
        if (GattAttributesHelper.hasProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE, property)) {
            mIndicateBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateValueUI(Map<String, String> newData) {
        mHexTextView.setText("hex: " + newData.get("hex"));
        mStringTextView.setText("string: " + newData.get("string"));
        mIntTextView.setText(
                String.format(
                        "uint: %s    sint: %s",
                        newData.get("uint"),
                        newData.get("sint")
                        )
                );
        mStmpTextView.setText("stmp: " + newData.get("stmp"));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showReadingCharacteristicIndicator(
            BLECharacteristic mBleCharacteristic) {
        mProgressDialog.setMessage("Reading Characteristic Value");
        mProgressDialog.show();
    }

    @Override
    public void showEnablingNotifitionIndicator(BLECharacteristic mBleCharacteristic,
            boolean enabled) {
        String msg = null;
        if (enabled) {
            msg = "Enabling notification";
        } else {
            msg = "Disabling notification";
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
        
        mEnableNofity = enabled;
        if (mEnableNofity) {
            mNotifyBtn.setText("Disable Notify");
        } else {
            mNotifyBtn.setText("Enable Notify");
        }
    }

    @Override
    public void showGotNotifitionIndicator(byte[] value) {
        Toast.makeText(getContext(), StringUtil.ByteArrayToHexString(value), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotifyDescriptorValueIndicator(int intExtra) {
        if (intExtra == BluetoothGatt.GATT_SUCCESS) {
            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showReadCharacteristicIndicatorCompleted(
            BLECharacteristic mBleCharacteristic, boolean status) {
        mProgressDialog.dismiss();
        if (!status) {
            Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showWriteCharacteristicIndicatorCompleted(
            BLECharacteristic mBleCharacteristic, boolean status) {
        mProgressDialog.dismiss();
        if (!status) {
            Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void showWriteCharacteristicValueDialog(
            BLECharacteristic mBleCharacteristic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("input hex value");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCharacteristicDetailPresenter.writeCharacteristic(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public void showWritingCharacteristicIndicator(
            BLECharacteristic mBleCharacteristic, String content) {
        mProgressDialog.setMessage("Writing Characteristic Value");
        mProgressDialog.show();
    }

}
