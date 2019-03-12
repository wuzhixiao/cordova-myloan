package com.plugin.br.cordova.device;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.plugin.br.cordova.Constant;
import com.plugin.br.cordova.bean.ErrorResult;
import com.plugin.br.cordova.util.DebugLog;
import com.plugin.br.cordova.util.DensityUtil;
import com.plugin.br.cordova.util.DeviceUtil;
import com.plugin.br.cordova.util.LocationUtil;
import com.plugin.br.cordova.util.NetWorkUtil;
import com.plugin.br.cordova.util.RootManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TimeZone;
import android.os.Build;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.net.wifi.WifiManager;
import android.content.ClipData;
import android.content.ClipboardManager;

/**
 * This class echoes a string called from JavaScript.
 */
public class DeviceBr extends CordovaPlugin {

    HashMap<String, CallbackContext> callbackContextMap = new HashMap<String, CallbackContext>();
    public static final String GET_TELPHONE = "getTelPhone";
    public static final String GET_NETWORK_INFO = "getNetworkInfo";
    public static final String GET_LOCATION = "getLocation";
    public static final String GET_DEVICEID = "getDeviceId";
    public static final String GET_DEVICEDETAIL = "getDeviceDetail";
    public static final String GET_CLIPBOARD = "getClipBoard";
    private LocationListener locationListener;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        DebugLog.i("wang","==action=="+action);
        if (action.equals(GET_TELPHONE)) {
            //    String message = args.getString(0);
            callbackContextMap.put(GET_TELPHONE, callbackContext);
            getTelePhonePermission();
            return true;
        }
        if (action.equals("getPid")) {
            callbackContext.success(getPid());
            return true;
        }
        if (action.equals("getPhoneInfo")) {
            getPhoneInfo(callbackContext);
            return true;
        }
        //获取时区
        if (action.equals("getTimeZome")) {
            getTimeZone(callbackContext);
            return true;
        }

        if (action.equals("getChanelId")) {
            getChanelId(callbackContext);
            return true;
        }
        if (action.equals(GET_NETWORK_INFO)) {
            callbackContextMap.put(GET_NETWORK_INFO, callbackContext);
            getNetworkInfoPermission();
            return true;
        }
        if (action.equals(GET_LOCATION)) {
            if (locationListener == null) {
                locationListener = new LocationUtil.MyLocation(cordova.getActivity(), callbackContext);
            }
            callbackContextMap.put(GET_LOCATION, callbackContext);
            getLocationPermission();
            return true;
        }

        if (action.equals(GET_DEVICEID)) {
            callbackContextMap.put(GET_DEVICEID, callbackContext);
            getDeviceIdPermission();
            return true;
        }

        if (action.equals(GET_DEVICEDETAIL)) {
            callbackContextMap.put(GET_DEVICEDETAIL, callbackContext);
            getDeviceDetail(cordova.getActivity());
            return true;
        }
        if (action.equals(GET_CLIPBOARD)) {
            callbackContextMap.put(GET_CLIPBOARD, callbackContext);
            getClip(cordova.getActivity());
            return true;
        }
        return false;
    }

    public void getPhoneInfo(CallbackContext callbackContext) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("screenResolution", DensityUtil.getDisplayWidth(cordova.getActivity()) + "*" + DensityUtil.getDisplayHeight(cordova.getActivity()));
        object.put("screenResolutionDpi", DensityUtil.getDensity(cordova.getActivity()));
        object.put("screenSize", DeviceUtil.getSize(cordova.getActivity()));
        object.put("batteryPower", DeviceUtil.getBatteryLevel(cordova.getActivity()));
        object.put("bootTime", DeviceUtil.getOpenTime());
        object.put("root", RootManager.RootCommand(""));
        // object.put("phoneUuid ", DeviceUtil.getUUID(cordova.getActivity()));
        object.put("motherboard", DeviceUtil.getAndroidBorder());
        object.put("equipmentModel", DeviceUtil.getDeviceName());
        object.put("cpu", DeviceUtil.getAndroidCpuApi());
        object.put("systemVersion", android.os.Build.VERSION.SDK);
        Log.i("wang", "==phoneInfo==" + object.toString());
        if (callbackContext != null) {
            callbackContext.success(object);
        }
    }

    public void getDeviceIdPermission() {
                if (PermissionHelper.hasPermission(DeviceBr.this, Manifest.permission.READ_PHONE_STATE)) {
                    resultUniqueId();
                } else {
                    PermissionHelper.requestPermission(DeviceBr.this, Constant.Manifest.READ_PHONE_STATE_CODE1, Manifest.permission.READ_PHONE_STATE);
                }

    }

    /**
     * 获取网络信息
     *
     * @param
     */
    public void getNetworkInfoPermission() {
     /*   cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
        if (PermissionHelper.hasPermission(DeviceBr.this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            getNetworkInfo();
        } else {
            PermissionHelper.requestPermission(DeviceBr.this, Constant.Manifest.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE);
        }
        /*    }
        });*/
    }

    public void getLocationPermission() {
        if(LocationUtil.isGPSOPen(cordova.getActivity())){
            if (PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) && PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (callbackContextMap.get(GET_LOCATION) != null) {
                    CallbackContext callbackContext = callbackContextMap.get(GET_LOCATION);
                    regitLocationInfo(callbackContext);
                }
            } else {
                String[] locatoinPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                PermissionHelper.requestPermissions(this, Constant.Manifest.ACCESS_LOACATION_CODE, locatoinPermission);
            }
        }else{
            if(callbackContextMap.get(GET_LOCATION)!=null){
                CallbackContext callbackContext= callbackContextMap.get(GET_LOCATION);
          /*      ErrorResult errorResult = new ErrorResult();
                errorResult.setErrorCode(Constant.GPS_ON_OPEN);
                errorResult.setDate("gps no open");*/
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("latitude","-1");
                map.put("longitude","-1");
                if(callbackContext!=null){
                    callbackContext.success(new Gson().toJson(map));
                }

            }
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            cordova.getActivity().startActivityForResult(intent, 10);
        }
    }

    public void getNetworkInfo() {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DeviceUtil.Wifi.getWifiMac1();
                JSONObject object = new JSONObject();
                try {
                    object.put("macAddress", DeviceUtil.Wifi.getWifiMac1());
                    object.put("bssid", DeviceUtil.Wifi.getBSSID(cordova.getActivity()));
                    HashMap<String, String> map = NetWorkUtil.getIp(cordova.getActivity());
                    if (map != null) {
                        object.put("ipAddress", map.get("ip"));
                        object.put("ipAddressAttribute", map.get("ipType"));
                    }
                    Log.i("wang", "==wifi==" + object.toString());
                    if (callbackContextMap.get(GET_NETWORK_INFO) != null) {
                        CallbackContext callbackContext = callbackContextMap.get(GET_NETWORK_INFO);
                        callbackContext.success(object);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (callbackContextMap.get(GET_NETWORK_INFO) != null) {
                        CallbackContext callbackContext = callbackContextMap.get(GET_NETWORK_INFO);
                        callbackContext.error(e.toString());
                    }
                }
            }
        });
    }

    /**
     * 获取位置信息
     */
    public void regitLocationInfo(CallbackContext callbackContext) {
        LocationUtil.getLngAndLat(cordova.getActivity(), locationListener, callbackContext);
    }

    /**
     * 获取打电话权限
     */
    public void getTelePhonePermission() {
/*        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
        if (PermissionHelper.hasPermission(DeviceBr.this, Manifest.permission.READ_PHONE_STATE)) {
            resultTelPhoneInfo();
        } else {
            try {
                PermissionHelper.requestPermission(DeviceBr.this, Constant.Manifest.READ_PHONE_STATE_CODE, Manifest.permission.READ_PHONE_STATE);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
       /*     }
        });*/

    }

    public int getPid() {
        return android.os.Process.myPid();
    }

    public void getTimeZone(CallbackContext callbackContext) {
        if (callbackContext != null) {
            TimeZone tz = TimeZone.getDefault();
            String strTz = tz.getDisplayName(false, TimeZone.SHORT);
            JSONObject object = new JSONObject();
            try {
                object.put("timeZone", strTz);
                callbackContext.success(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getChanelId(CallbackContext callbackContext) {
        if (callbackContext != null) {
            String chanelId = DeviceUtil.getChannel(cordova.getActivity());
            JSONObject object = new JSONObject();
            try {
                object.put("chanelId", chanelId);
                callbackContext.success(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getChanelId() {
        String chanelId = DeviceUtil.getChannel(cordova.getActivity());
        JSONObject object = new JSONObject();
        try {
            object.put("chanelId", chanelId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 回调手机sim卡信息
     */
    public void resultTelPhoneInfo() {
        CallbackContext callbackContext = callbackContextMap.get(GET_TELPHONE);
        JSONObject object = new JSONObject();
        try {
            object.put("phoneIccd", DeviceUtil.getICCID(cordova.getActivity()));
            object.put("phoneImei", DeviceUtil.getIMEI(cordova.getActivity()));
            object.put("phoneImsi", DeviceUtil.getIMSI(cordova.getActivity()));
            object.put("phoneUuid", DeviceUtil.getUUID(cordova.getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (callbackContext != null) {
            callbackContext.success(object);
        }
    }

    /**
     * 回调唯一标识id
     */
    public void resultUniqueId() {
        String uniqueId = DeviceUtil.getUniqueID(cordova.getActivity());
        if (callbackContextMap.get(GET_DEVICEID) != null) {
            CallbackContext callbackContext = callbackContextMap.get(GET_DEVICEID);
            callbackContext.success(uniqueId);
        }
    }


    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                switch (requestCode) {
                    case Constant.Manifest.READ_PHONE_STATE_CODE:
                        if (callbackContextMap.get(GET_TELPHONE) != null) {
                            CallbackContext callbackContext = callbackContextMap.get(GET_TELPHONE);
                            ErrorResult errorResult = new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.READ_PHONE_STATE_CODE));
                            callbackContext.error(errorResult.toString());
                        }
                        break;
                    case Constant.Manifest.ACCESS_NETWORK_STATE:
                        if (callbackContextMap.get(GET_NETWORK_INFO) != null) {
                            CallbackContext callbackContext = callbackContextMap.get(GET_NETWORK_INFO);
                            ErrorResult errorResult = new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.ACCESS_NETWORK_STATE));
                            callbackContext.error(errorResult.toString());
                        }
                        break;
                    case Constant.Manifest.ACCESS_LOACATION_CODE:
                        if (callbackContextMap.get(GET_LOCATION) != null) {
                           // getLocationPermission();
                            CallbackContext callbackContext = callbackContextMap.get(GET_LOCATION);
                            ErrorResult errorResult = new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.ACCESS_LOACATION_CODE));
                            callbackContext.error(errorResult.toString());
                        }
                        break;
                    case Constant.Manifest.READ_PHONE_STATE_CODE1:
                        if (callbackContextMap.get(GET_DEVICEID) != null) {
                            CallbackContext callbackContext = callbackContextMap.get(GET_DEVICEID);
                            ErrorResult errorResult = new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.READ_PHONE_STATE_CODE1));
                            callbackContext.error(errorResult.toString());
                        }
                        break;
                }
                //  this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, -1));
                return;
            }

        }
        switch (requestCode) {
            case Constant.Manifest.READ_PHONE_STATE_CODE:
                if (callbackContextMap.get(GET_TELPHONE) != null) {
                    resultTelPhoneInfo();
                }
            case Constant.Manifest.ACCESS_NETWORK_STATE:
                if (callbackContextMap.get(GET_NETWORK_INFO) != null) {
                    getNetworkInfo();
                }
                break;
            case Constant.Manifest.ACCESS_LOACATION_CODE:
                if (callbackContextMap.get(GET_LOCATION) != null) {
                    CallbackContext callbackContext = callbackContextMap.get(GET_LOCATION);
                    regitLocationInfo(callbackContext);
                }
                break;
            // search(executeArgs);
            case Constant.Manifest.READ_PHONE_STATE_CODE1:
                if (callbackContextMap.get(GET_DEVICEID) != null) {
                   resultUniqueId();
                }
                break;

        }
    }
    public void getDeviceDetail(Context context) {
        String deviceDetail = "{\"board\":\"" + Build.BOARD + "\",\"brand\":\"" + Build.BRAND + "\",\"buildId\":\"" + Build.ID + "\",\"cpuAbi\":\"" + Build.CPU_ABI + "\",\"device\":\"" + Build.DEVICE + "\",\"display\":\"" + Build.DISPLAY + "\"," +
                "\"host\":\"" + Build.HOST + "\",\"imei\":\"" + getIMEI(context) + "\",\"manufacturer\":\"" + Build.MANUFACTURER + "\",\"model\":\"" + Build.MODEL + "\"," +
                "\"mwlanmac\":\"" + getWIFIMAC(context) + "\",\"product\":\"" + Build.PRODUCT + "\",\"tags\":\"" + Build.TAGS + "\",\"type\":\"" + Build.TYPE + "\",\"user\":\"" + Build.USER + "\"}";
        CallbackContext callbackContext= callbackContextMap.get(GET_DEVICEDETAIL);
        callbackContext.success(deviceDetail);
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId(); //IMEI码
        return IMEI;
    }
    public static String getWIFIMAC(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_WLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_WLANMAC;
    }

    private void getClip(Context context) {
        ClipboardManager mClipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = mClipManager.getPrimaryClip();
        StringBuilder clipStr = new StringBuilder("");
        if (clip != null) {
            int count = clip.getItemCount();
            for (int i = 0; i < count; i++) {
                CharSequence content = clip.getItemAt(i).getText();
                clipStr.append(content);
            }
        }
        CallbackContext callbackContext= callbackContextMap.get(GET_CLIPBOARD);
        callbackContext.success(clipStr.toString());
    }

}
