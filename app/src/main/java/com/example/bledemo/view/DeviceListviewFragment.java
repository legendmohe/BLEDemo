package com.example.bledemo.view;

import java.util.ArrayList;

import com.example.bledemo.R;
import com.example.bledemo.R.id;
import com.example.bledemo.R.layout;
import com.example.bledemo.adapter.DeviceListAdapter;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.model.BLEDevice;
import com.example.bledemo.mvp.presenter.DeviceListPresenter;
import com.example.bledemo.mvp.view.DeviceListView;
import com.example.bledemo.view.MainActivity.MenuStub;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DeviceListviewFragment extends Fragment implements DeviceListView, MenuStub {
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    private DeviceListPresenter mDeviceListPresenter;
    private DeviceListAdapter mDeviceListAdapter;

    private Menu mMenu;
    private ProgressDialog mProgressDialog;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static DeviceListviewFragment newInstance(int sectionNumber) {
        DeviceListviewFragment fragment = new DeviceListviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceListviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_list_fragment, container,
                false);
        this.setupViews(rootView);
        mDeviceListPresenter = new DeviceListPresenter(this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(
                ARG_SECTION_NUMBER));
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mDeviceListPresenter.start();
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onStop() {
        mDeviceListPresenter.stop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onStop();
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onDestroyView() {
        mProgressDialog = null;
        super.onDestroyView();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setupViews(View rootView) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface arg0) {
                mDeviceListPresenter.onConnectingDialogCancel();
            }
        });
        
        ListView listView = (ListView) rootView.findViewById(R.id.device_listview);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                    long id) {
                if (BLEManager.getInstance().isScanning()) {
                    BLEManager.getInstance().scanLeDevice(false);
                }
                BLEManager.getInstance().connect(mDeviceListAdapter.getItem(pos));
            }
        });
        
        mDeviceListAdapter = new DeviceListAdapter(getActivity());
        listView.setAdapter(mDeviceListAdapter);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void updateDeviceList(ArrayList<BLEDevice> devices) {
        mDeviceListAdapter.clear();
        addDeviceList(devices);
    }

    @Override
    public void updateDevice(int position, BLEDevice device) {
        mDeviceListAdapter.updateDevice(position, device);
    }

    @Override
    public void updateDeviceInfo() {
        mDeviceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void addDeviceList(ArrayList<BLEDevice> devices) {
        mDeviceListAdapter.addDevices(devices);
    }

    @Override
    public void addDevice(BLEDevice device) {
        mDeviceListAdapter.add(device);
    }

    @Override
    public void updateDevice(BLEDevice device) {
        mDeviceListAdapter.updateDevice(device);
    }

    @Override
    public int devicePosInList(BLEDevice device) {
        return mDeviceListAdapter.devicePosInList(device);
    }

    @Override
    public void changeScanMenuState(boolean scanning) {
        if (this.mMenu == null) {
            return;
        }
        String newTitle = getString(R.string.action_ble_scan);
        if (scanning) {
            newTitle = getString(R.string.action_ble_stop_scan);
        }
        MenuItem menuItem = this.mMenu.findItem(R.id.action_scan);
        menuItem.setTitle(newTitle);
    }

    @Override
    public void injectMenu(Menu menu) {
        this.mMenu = menu;
    }

    @Override
    public void onMenuSelected(int id) {
        switch (id) {
            case R.id.action_scan:
                mDeviceListPresenter.onScanMenuPress();
                break;

            default:
                break;
        }
    }

    @Override
    public void showConnectingDeviceIndicator(BLEDevice device) {
        mProgressDialog.setMessage("Connecting");
        mProgressDialog.show();
    }

    @Override
    public void showDisconnectedIndicator(BLEDevice device) {
        Toast.makeText(getActivity(), "disconnected", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
    }

    @Override
    public void clearUI() {
        mDeviceListAdapter.clear();
        mDeviceListAdapter.notifyDataSetChanged();
    }
}
