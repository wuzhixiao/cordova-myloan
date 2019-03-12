package com.plugin.br.cordova.googlemapplugin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.IslamicCalendar;
import android.location.LocationListener;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import com.plugin.br.cordova.Constant;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;

import com.plugin.br.cordova.bean.ErrorResult;
import com.plugin.br.cordova.googlemapplugin.AlxLocationManager;
import com.plugin.br.cordova.googlemapplugin.MyLocation;
import com.plugin.br.cordova.util.LocationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * This class echoes a string called from JavaScript.
 */
public class GoogleMapPlugin extends CordovaPlugin {
    private boolean isRun = true;
    private int time;
    private LocationListener locationListener;
    private CallbackContext callbackContext;
    public static final int REQUEST_PERMISSION = 0X01;
    private String mPermission = "0";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());

        if (PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlxLocationManager.onCreateGPS(cordova.getActivity().getApplication());
        } else {
            String[] locatoinPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            PermissionHelper.requestPermissions(this, REQUEST_PERMISSION, locatoinPermission);
        }

    }


//    @Override
//    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//        this.callbackContext = callbackContext;
//        if (action.equals("getLocation")) {
//            mPermission = args.getJSONObject(0).getInt("permission");
//            isRun = true;
//            time = 0;
//            getLocationByAndroid(callbackContext);
//            return true;
//        }
//        return false;
//    }

    @Override
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("getLocation")) {
//            if (!TextUtils.isEmpty(rawArgs))
//                mPermission = rawArgs;
            isRun = true;
            time = 0;
            getLocationByAndroid(callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void getLocation(final CallbackContext callbackContext) {
        final Handler handler = new Handler();
        if (PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //每隔2s更新一下经纬度结果
            new Timer().scheduleAtFixedRate(new TimerTask() {//每秒钟检查一下当前位置
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isRun) {
                                int latitude = (int) MyLocation.getInstance().latitude;
                                int longitude = (int) MyLocation.getInstance().longitude;
                                Log.d("wzx", String.valueOf(MyLocation.getInstance().latitude) + "  " + String.valueOf(MyLocation.getInstance().longitude));
                                time++;
                                if (latitude != 0 && longitude != 0) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("latitude", String.valueOf(MyLocation.getInstance().latitude));
                                    map.put("longitude", String.valueOf(MyLocation.getInstance().longitude));
                                    if (callbackContext != null) {
                                        callbackContext.success(new Gson().toJson(map));
                                    }
//                                    Log.d("wzx", String.valueOf(MyLocation.getInstance().latitude) + "  " + String.valueOf(MyLocation.getInstance().longitude));
                                    isRun = false;
                                    return;
                                } else {
                                    if (time >= 30) {
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("latitude", String.valueOf(0.00));
                                        map.put("longitude", String.valueOf(0.00));
                                        callbackContext.success(new Gson().toJson(map));
                                        isRun = false;
                                        return;
                                    }
                                }
                            }

                        }
                    });
                }
            }, 0, 1500);
        } else {
            String[] locatoinPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            PermissionHelper.requestPermissions(this, Constant.Manifest.ACCESS_LOACATION_CODE, locatoinPermission);
        }


    }

    private boolean getLocationByAndroid(CallbackContext callbackContext) {
        if (locationListener == null) {
            locationListener = new LocationUtil.MyLocation(cordova.getActivity(), callbackContext);
        }
        getLocationPermission(callbackContext);
        return true;
    }

    public void getLocationPermission(CallbackContext callbackContext) {
        if (LocationUtil.isGPSOPen(cordova.getActivity())) {
            if (PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) && PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                regitLocationInfo(callbackContext);
            } else {
                String[] locatoinPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                PermissionHelper.requestPermissions(this, Constant.Manifest.ACCESS_LOACATION_CODE, locatoinPermission);
            }
        } else {
          /*      ErrorResult errorResult = new ErrorResult();
                errorResult.setErrorCode(Constant.GPS_ON_OPEN);
                errorResult.setDate("gps no open");*/
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("latitude", "-1");
//            map.put("longitude", "-1");
//            if (callbackContext != null) {
//                callbackContext.success(new Gson().toJson(map));
//
//            }
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            cordova.getActivity().startActivityForResult(intent, 10);
            getLocation(callbackContext);
        }
    }

    private int toget = 0;

    /**
     * 获取位置信息
     */
    public void regitLocationInfo(CallbackContext callbackContext) {
        LocationUtil.getLngAndLat(cordova.getActivity(), locationListener, callbackContext);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        for (int r : grantResults) {
            Log.d("onRequestPermifffff", r + "::::::" + requestCode);
            if (r == PackageManager.PERMISSION_DENIED) {
                switch (requestCode) {
                    case Constant.Manifest.ACCESS_LOACATION_CODE:
//                        ErrorResult errorResult = new ErrorResult();
//                        errorResult.setErrorCode(Constant.PERMISSION_DENY);
//                        errorResult.setDate(String.valueOf(Constant.Manifest.ACCESS_LOACATION_CODE));
//                        callbackContext.error(errorResult.toString());
                        Log.d("onRequestPermifffff", "ACCESS_LOACATION_CODE");
                        if (mPermission.equals("1")) {
                            if (PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    && PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            } else {
                                String[] locatoinPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                                PermissionHelper.requestPermissions(this, REQUEST_PERMISSION, locatoinPermission);
                            }
                        } else {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("latitude", String.valueOf("0.0"));
                            map.put("longitude", String.valueOf("0.0"));
                            if (callbackContext != null) {
                                callbackContext.success(new Gson().toJson(map));
                            }
                        }
                        break;
                    case REQUEST_PERMISSION:
                        if (mPermission.equals("1")) {
                            if (PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    && PermissionHelper.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            } else {
                                String[] locatoinPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                                PermissionHelper.requestPermissions(this, REQUEST_PERMISSION, locatoinPermission);
                            }
                        } else {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("latitude", String.valueOf("0.0"));
                            map.put("longitude", String.valueOf("0.0"));
                            if (callbackContext != null) {
                                callbackContext.success(new Gson().toJson(map));
                            }
                        }
                        break;
                }
                //  this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, -1));
                return;
            } else {
                switch (requestCode) {
                    // search(executeArgs);
                    case Constant.Manifest.ACCESS_LOACATION_CODE:
                        getLocationByAndroid(callbackContext);
                        break;
                    case REQUEST_PERMISSION:
                        AlxLocationManager.onCreateGPS(cordova.getActivity().getApplication());
                        break;

                }
            }

        }
    }
}
