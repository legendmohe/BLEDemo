package com.example.bledemo.helper;

import java.util.HashMap;
import java.util.Locale;

import android.util.Log;
/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class GattAttributesHelper {
    private static HashMap<String, String> attributes = new HashMap<String, String>();
    public static final String ALERT_LEVEL_CHARACTERISTIC_UUID = "00002A06-0000-1000-8000-00805F9B34FB";
    public static final String APPEARANCE_CHARACTERISTIC_UUID = "00002A01-0000-1000-8000-00805F9B34FB";
    public static final String BATTERY_LEVEL_CHARACTERISTIC_UUID = "00002A19-0000-1000-8000-00805F9B34FB";
    public static final String BATTERY_SERVICE_UUID = "0000180F-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_BROADCAST_PERIOD_CHARACTERISTIC_UUID = "0000FFF7-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_CORRECTION_POWER_CHARACTERISTIC_UUID = "0000FFF4-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_MAJOR_CHARACTERISTIC_UUID = "0000FFF2-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_MINOR_CHARACTERISTIC_UUID = "0000FFF3-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_PASSCODE_CHARACTERISTIC_UUID = "0000FFF6-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_POWER_CHARACTERISTIC_UUID = "0000FFF5-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_PROXIMITY_CHARACTERISTIC_UUID = "0000FFF1-0000-1000-8000-00805F9B34FB";
    public static final String BEACON_SERVICE_UUID = "0000FFF0-0000-1000-8000-00805F9B34FB";
    public static final String CHAR_DESCRIPTOR_UUID = "00002901-0000-1000-8000-00805F9B34FB";
    public static final String CLIENT_CONFIG_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805F9B34FB";
    public static final String DEVICE_INFO_SERVICE_UUID = "0000180A-0000-1000-8000-00805F9B34FB";
    public static final String DEVICE_NAME_CHARACTERISTIC_UUID = "00002A00-0000-1000-8000-00805F9B34FB";
    public static final String FIRMWARE_REVISION_CHARACTERISTIC_UUID = "00002A26-0000-1000-8000-00805F9B34FB";
    public static final String GENERIC_ACCESS_SERVICE_UUID = "00001800-0000-1000-8000-00805F9B34FB";
    public static final String GENERIC_ATTRIBUTE_SERVICE_UUID = "00001801-0000-1000-8000-00805F9B34FB";
    public static final String HARDWARE_REVISION_CHARACTERISTIC_UUID = "00002A27-0000-1000-8000-00805F9B34FB";
    public static final String HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID = "00002A37-0000-1000-8000-00805F9B34FB";
    public static final String HEART_RATE_MEASUREMENT_SERVICE_UUID = "0000180D-0000-1000-8000-00805F9B34FB";
    public static final String IEEE_REGULATORY_CERT_DATALIST_CHARACTERISTIC_UUID = "00002A2A-0000-1000-8000-00805F9B34FB";
    public static final String IMMEDIATE_ALERT_SERVICE_UUID = "00001802-0000-1000-8000-00805F9B34FB";
    public static final String LINK_LOSS_SERVICE_UUID = "00001803-0000-1000-8000-00805F9B34FB";
    public static final String MANUFACTURER_CHARACTERISTIC_UUID = "00002A29-0000-1000-8000-00805F9B34FB";
    public static final String MODEL_NUMBER_CHARACTERISTIC_UUID = "00002A24-0000-1000-8000-00805F9B34FB";
    public static final String PERIPHERAL_PREFERRED_CONNECTION_CHARACTERISTIC_UUID = "00002A04-0000-1000-8000-00805F9B34FB";
    public static final String PERIPHERAL_PRIVACY_FLAG_CHARACTERISTIC_UUID = "00002A02-0000-1000-8000-00805F9B34FB";
    public static final String PNP_ID_CHARACTERISTIC_UUID = "00002A50-0000-1000-8000-00805F9B34FB";
    public static final String RECONNECTION_ADDRESS_CHARACTERISTIC_UUID = "00002A03-0000-1000-8000-00805F9B34FB";
    public static final String SERIAL_NUMBER_CHARACTERISTIC_UUID = "00002A25-0000-1000-8000-00805F9B34FB";
    public static final String SERVICE_CHANGED_CHARACTERISTIC_UUID = "00002A05-0000-1000-8000-00805F9B34FB";
    public static final String SOFTWARE_REVISION_CHARACTERISTIC_UUID = "00002A28-0000-1000-8000-00805F9B34FB";
    public static final String SYSTEM_ID_CHARACTERISTIC_UUID = "00002A23-0000-1000-8000-00805F9B34FB";
    public static final String TX_POWER_LEVEL_CHARACTERISTIC_UUID = "00002A07-0000-1000-8000-00805F9B34FB";
    public static final String TX_POWER_SERVICE_UUID = "00001804-0000-1000-8000-00805F9B34FB";
    public static final String UNKNOWN4_SERVICE_UUID = "F000FFC0-0451-4000-B000-000000000000";
    public static final String UNKOWN3_DESCRIPTOR_UUID = "00002908-0000-1000-8000-00805F9B34FB";
    public static final String UNKOWN7_CHARACTERISTIC_UUID = "F000FFC1-0451-4000-B000-000000000000";
    public static final String UNKOWN8_CHARACTERISTIC_UUID = "F000FFC2-0451-4000-B000-000000000000";
    
    static {
        // Services.
        attributes.put(CLIENT_CONFIG_DESCRIPTOR_UUID, "Client Characteristic Config");
        attributes.put("00001800-0000-1000-8000-00805F9B34FB", "General Access");
        attributes.put("00002A00-0000-1000-8000-00805F9B34FB", "Device Name");
        attributes.put("00002A01-0000-1000-8000-00805F9B34FB", "Apperance");
        attributes.put("00002A02-0000-1000-8000-00805F9B34FB", "Peripheral Privacy Flag");
        attributes.put("00002A03-0000-1000-8000-00805F9B34FB", "Reconnection Address");
        attributes.put("00002A04-0000-1000-8000-00805F9B34FB", "Peripheral Preferred Connection");
        attributes.put("00001801-0000-1000-8000-00805F9B34FB", "General Attribute");
        attributes.put("00002A05-0000-1000-8000-00805F9B34FB", "Service Changed");
        attributes.put("00001802-0000-1000-8000-00805F9B34FB", "Immediate Alert");
        attributes.put("00002A06-0000-1000-8000-00805F9B34FB", "Alert Level");
        attributes.put("00001803-0000-1000-8000-00805F9B34FB", "Link Loss");
        attributes.put("00001804-0000-1000-8000-00805F9B34FB", "Tx Power");
        attributes.put("00002A07-0000-1000-8000-00805F9B34FB", "Tx Power Level");
        attributes.put("0000180A-0000-1000-8000-00805F9B34FB", "Device Information");
        attributes.put("00002A23-0000-1000-8000-00805F9B34FB", "System ID");
        attributes.put("00002A24-0000-1000-8000-00805F9B34FB", "Model Number");
        attributes.put("00002A25-0000-1000-8000-00805F9B34FB", "Serial Number");
        attributes.put("00002A26-0000-1000-8000-00805F9B34FB", "Firware Revision");
        attributes.put("00002A27-0000-1000-8000-00805F9B34FB", "Hardware Revision");
        attributes.put("00002A28-0000-1000-8000-00805F9B34FB", "Software Revision");
        attributes.put("00002A29-0000-1000-8000-00805F9B34FB", "Manufacturer");
        attributes.put("00002A2A-0000-1000-8000-00805F9B34FB", "IEEE Regulatory Certfication Data List");
        attributes.put(HEART_RATE_MEASUREMENT_SERVICE_UUID, "Heart Rate Measurement");
        attributes.put(HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID, "Heart Rate Measurement");
        attributes.put("00002A50-0000-1000-8000-00805F9B34FB", "PNP ID");
        attributes.put("0000180F-0000-1000-8000-00805F9B34FB", "Battery Service");
        attributes.put("00002A19-0000-1000-8000-00805F9B34FB", "Battery Level");
        attributes.put("0000FFF0-0000-1000-8000-00805F9B34FB", "Beacon");
        attributes.put("0000FFF1-0000-1000-8000-00805F9B34FB", "Proximity");
        attributes.put("0000FFF2-0000-1000-8000-00805F9B34FB", "Major");
        attributes.put("0000FFF3-0000-1000-8000-00805F9B34FB", "Minor");
        attributes.put("0000FFF4-0000-1000-8000-00805F9B34FB", "TX Power");
        attributes.put("0000FFF5-0000-1000-8000-00805F9B34FB", "Power");
        attributes.put("0000FFF6-0000-1000-8000-00805F9B34FB", "Passcode");
        attributes.put("0000FFF7-0000-1000-8000-00805F9B34FB", "Broadcast Period");
        attributes.put("00002901-0000-1000-8000-00805F9B34FB", "Char");
        attributes.put("00002902-0000-1000-8000-00805F9B34FB", "Client Configuration");
    }
    
    private static Locale gLocale = Locale.getDefault();
    
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid.toUpperCase(gLocale));
        return name == null ? defaultName : name;
    }
    
    public static boolean hasProperty(int propertie_type, int propertie) {
        return (propertie & propertie_type) != 0;
    }
    
    public static boolean hasPermission(int permission_type, int permission) {
        return (permission & permission_type) != 0;
    }

    public static String parsePermissions(int permissions) {
        StringBuffer buffer = new StringBuffer();
        if ((permissions & 0x01) != 0) {
            buffer.append("read");
            buffer.append(" | ");
        }
        if ((permissions & 0x02) != 0) {
            buffer.append("read encrypted");
            buffer.append(" | ");
        }
        if ((permissions & 0x04) != 0) {
            buffer.append("read encrypted MITM");
            buffer.append(" | ");
        }
        if ((permissions & 0x10) != 0) {
            buffer.append("write");
            buffer.append(" | ");
        }
        if ((permissions & 0x20) != 0) {
            buffer.append("write encrypted");
            buffer.append(" | ");
        }
        if ((permissions & 0x40) != 0) {
            buffer.append("write encrypted MITM");
            buffer.append(" | ");
        }
        if ((permissions & 0x80) != 0) {
            buffer.append("write signed");
            buffer.append(" | ");
        }
        if ((permissions & 0x100) != 0) {
            buffer.append("write signed MITM");
            buffer.append(" | ");
        }
        if (buffer.length() != 0) {
            buffer.delete(buffer.length() - " | ".length(), buffer.length());
        }
        return buffer.toString();
    }
    
    public static String parseProperties(int properties) {
        StringBuffer buffer = new StringBuffer();
        if ((properties & 0x01) != 0) {
            buffer.append("broadcast");
            buffer.append(" | ");
        }
        if ((properties & 0x02) != 0) {
            buffer.append("read");
            buffer.append(" | ");
        }
        if ((properties & 0x04) != 0) {
            buffer.append("write no response");
            buffer.append(" | ");
        }
        if ((properties & 0x08) != 0) {
            buffer.append("write");
            buffer.append(" | ");
        }
        if ((properties & 0x10) != 0) {
            buffer.append("notify");
            buffer.append(" | ");
        }
        if ((properties & 0x20) != 0) {
            buffer.append("indicate");
            buffer.append(" | ");
        }
        if ((properties & 0x40) != 0) {
            buffer.append("signed write");
            buffer.append(" | ");
        }
        if ((properties & 0x80) != 0) {
            buffer.append("extended properties");
            buffer.append(" | ");
        }
        if (buffer.length() != 0) {
            buffer.delete(buffer.length() - " | ".length(), buffer.length());
        }
        return buffer.toString();
    }
}