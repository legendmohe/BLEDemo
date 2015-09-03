package com.example.bledemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BLEDevice implements Parcelable {
    private String name;
    private int rssi;
    private String mac;
    private String _dump;
    
    public BLEDevice() {
    }
    
    public BLEDevice(String name, int rssi, String mac) {
        this.name = name;
        this.rssi = rssi;
        this.mac = mac;
    }
    
    public BLEDevice(String dump) {
        setDump(dump);
        // set name..., etc
    }
    
    public BLEDevice(Parcel in) {
        this.name = in.readString();
        this.rssi = in.readInt();
        this.mac = in.readString();
        this._dump = in.readString();
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
    /**
     * @return the rssi
     */
    public int getRssi() {
        return rssi;
    }
    /**
     * @param rssi the rssi to set
     */
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }
    /**
     * @param mac the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }
    /**
     * @return the _dump
     */
    public String getDump() {
        return _dump;
    }
    /**
     * @param _dump the _dump to set
     */
    public void setDump(String _dump) {
        this._dump = _dump;
    }
    
    @Override
    public int hashCode() {
        return mac.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        return mac.equals(((BLEDevice)o).mac);
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(rssi);
        dest.writeString(mac);
        dest.writeString(_dump);
    }
    
    public static final Parcelable.Creator<BLEDevice> CREATOR = new Parcelable.Creator<BLEDevice>() {  
        
        public BLEDevice createFromParcel(Parcel in) {  
            return new BLEDevice(in);  
        }  
  
        public BLEDevice[] newArray(int size) {  
            return new BLEDevice[size];  
        }  
    };  
}
