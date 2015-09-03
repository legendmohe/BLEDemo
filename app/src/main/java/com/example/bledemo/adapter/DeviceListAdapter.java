package com.example.bledemo.adapter;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bledemo.R;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.util.ViewHolder;
import com.example.bledemo.view.ServiceListActivity;

public class DeviceListAdapter extends ArrayAdapter<BLEDevice> {
    
//    public static final String EXTRA_SELECTED_DEVICE = "EXTRA_SELECTED_DEVICE";
    
    private WeakReference<Context> mContext;
    
    public DeviceListAdapter(Context context) {
        super(context, R.layout.device_list_item_view);
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public void add(BLEDevice object) {
        super.add(object);
    }
    
    public void addDevice(BLEDevice object) {
        add(object);
    }

    public void addDevices(List<BLEDevice> items) {
        clear();
        setNotifyOnChange(false);
        if (items != null) {
            for (BLEDevice item : items)
                add(item);
        }
        notifyDataSetChanged();
    }
    
    public boolean updateDevice(BLEDevice device) {
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).equals(device)) {
                BLEDevice old_device = getItem(i);
                old_device.setName(device.getName());
                old_device.setMac(device.getMac());
                old_device.setRssi(device.getRssi());
                old_device.setDump(device.getDump());
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }
    
    public boolean updateDevice(int pos, BLEDevice device) {
        if (getItem(pos).equals(device)) {
            BLEDevice old_device = getItem(pos);
            old_device.setName(device.getName());
            old_device.setMac(device.getMac());
            old_device.setRssi(device.getRssi());
            old_device.setDump(device.getDump());
            notifyDataSetChanged();
            return true;
        }
        return false;
    }
    
    public int devicePosInList(BLEDevice device) {
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).equals(device)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mContext.get() == null) {
            return null;
        }
        if (convertView == null) {  
            convertView = LayoutInflater.from(mContext.get())  
              .inflate(R.layout.device_list_item_view, parent, false);
        }  

        TextView name_textview = ViewHolder.get(convertView, R.id.device_name_textview);  
        TextView mac_textview = ViewHolder.get(convertView, R.id.device_mac_textview);  
        TextView rssi_textview = ViewHolder.get(convertView, R.id.device_rssi_textview);  
        TextView dump_textView = ViewHolder.get(convertView, R.id.device_dump_textview);
        
        final BLEDevice device = getItem(position);  
        name_textview.setText("Name:  " + device.getName());  
        mac_textview.setText("Address:  " + device.getMac());
        rssi_textview.setText("RSSI:  " + String.valueOf(device.getRssi()));
        dump_textView.setText("Dump:\n" + device.getDump());

//        convertView.setOnClickListener(new OnClickListener() {
//            
//            @Override
//            public void onClick(View arg0) {
////                if (BLEManager.getInstance().isScanning()) {
////                    BLEManager.getInstance().scanLeDevice(false);
////                }
////                Intent intent = new Intent(getContext(), ServiceListActivity.class);
////                intent.putExtra(EXTRA_SELECTED_DEVICE, device);
////                getContext().startActivity(intent);
//
//            }
//        });
        
        return convertView;  
    }
}
