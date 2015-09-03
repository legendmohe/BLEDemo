package com.example.bledemo.adapter;

import java.lang.ref.WeakReference;
import java.util.UUID;

import com.example.bledemo.R;
import com.example.bledemo.helper.GattAttributesHelper;
import com.example.bledemo.model.BLECharacteristic;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.util.ViewHolder;
import com.example.bledemo.view.CharacteristicListActivity;
import com.example.bledemo.view.CharacteristicDetalActivity;
import com.example.bledemo.view.ServiceListActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CharacteristicListAdapter extends ArrayAdapter<BLECharacteristic> {
    
    public static final String EXTRA_SELECTED_CHARACTERISTIC_UUID = "EXTRA_SELECTED_CHARACTERISTIC_UUID";
    public static final String EXTRA_SELECTED_SERVICE_INDEX = "EXTRA_SELECTED_SERVICE_INDEX";
    
    private WeakReference<Context> mContext;

    private int mServiceIndex;
    
    public CharacteristicListAdapter(Context context, int serverIndex) {
        super(context, R.layout.characteristic_list_item_view);
        mContext = new WeakReference<Context>(context);
        this.mServiceIndex = serverIndex;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mContext.get() == null) {
            return null;
        }
        if (convertView == null) {  
            convertView = LayoutInflater.from(mContext.get())  
              .inflate(R.layout.characteristic_list_item_view, parent, false);
        }  

        TextView name_textview = ViewHolder.get(convertView, R.id.characteristic_name_textview);  
        TextView uuid_textview = ViewHolder.get(convertView, R.id.characteristic_uuid_textview);  
        TextView instance_id_textview = ViewHolder.get(convertView, R.id.characteristic_instance_id_textview);  
        TextView property_textView = ViewHolder.get(convertView, R.id.characteristic_property_textview);
        TextView permission_textView = ViewHolder.get(convertView, R.id.characteristic_permission_textview);
        
        final BLECharacteristic characteristic = getItem(position);  
        name_textview.setText("Name:  " + characteristic.getName());  
        uuid_textview.setText("UUID:  " + characteristic.getUuid().toString());
        instance_id_textview.setText("Instance ID:  " + characteristic.getInstanceId());
        property_textView.setText("Property:  " + characteristic.getPropertiesString());
        permission_textView.setText("Permission:  " + characteristic.getPermissionsString());

        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getContext(), CharacteristicDetalActivity.class);
                intent.putExtra(EXTRA_SELECTED_SERVICE_INDEX, mServiceIndex);
                intent.putExtra(EXTRA_SELECTED_CHARACTERISTIC_UUID, characteristic.getUuid());
                getContext().startActivity(intent);
            }
        });
        
        return convertView;  
    }
}
