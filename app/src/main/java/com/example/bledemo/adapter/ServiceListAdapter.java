package com.example.bledemo.adapter;

import java.lang.ref.WeakReference;
import java.util.UUID;

import com.example.bledemo.R;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.model.BLEService;
import com.example.bledemo.util.ViewHolder;
import com.example.bledemo.view.CharacteristicListActivity;
import com.example.bledemo.view.ServiceListActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ServiceListAdapter extends ArrayAdapter<BLEService> {
    
    public static final String EXTRA_SELECTED_SERVICE_INDEX = "EXTRA_SELECTED_SERVICE";
    
    private WeakReference<Context> mContext;
    
    public ServiceListAdapter(Context context) {
        super(context, R.layout.service_list_item_view);
        mContext = new WeakReference<Context>(context);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mContext.get() == null) {
            return null;
        }
        if (convertView == null) {  
            convertView = LayoutInflater.from(mContext.get())  
              .inflate(R.layout.service_list_item_view, parent, false);
        }  

        TextView name_textview = ViewHolder.get(convertView, R.id.service_name_textview);  
        TextView uuid_textview = ViewHolder.get(convertView, R.id.service_uuid_textview);  
        TextView instance_id_textview = ViewHolder.get(convertView, R.id.service_instance_id_textview);  
        TextView type_textView = ViewHolder.get(convertView, R.id.service_type_textview);
        
        final int pos = position;
        final BLEService service = getItem(position);  
        name_textview.setText("Name:  " + service.getName());  
        uuid_textview.setText("UUID:  " + service.getUuid().toString());
        instance_id_textview.setText("Instance ID:  " + service.getInstanceId());
        type_textView.setText("Type:  " + service.getTypeName());

        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getContext(), CharacteristicListActivity.class);
                intent.putExtra(EXTRA_SELECTED_SERVICE_INDEX, pos);
                getContext().startActivity(intent);
            }
        });
        
        return convertView;  
    }
}
