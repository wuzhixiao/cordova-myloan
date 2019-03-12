package com.vaenow.appupdate;

import android.Manifest;
import android.content.pm.PackageManager;

import com.plugin.br.cordova.Constant;
import com.plugin.br.cordova.bean.ErrorResult;
import com.plugin.br.cordova.util.DebugLog;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LuoWen on 2015/10/27.
 */
public class CheckAppUpdate extends CordovaPlugin {
    public static final String TAG = "CheckAppUpdate";

    private UpdateManager updateManager = null;
    private CallbackContext callbackContext=null;
    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE =0X00000012;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String apkUrl;
    private String apkName;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        if ("downApk".equals(action)) {
            JSONArray array = new JSONArray(args);
            this.apkUrl = array.getString(0);
            this.apkName = array.getString(1);
             this.callbackContext=callbackContext;
            downApk(apkUrl, apkName,callbackContext);

            return true;
        }
        callbackContext.error(Utils.makeJSON(Constants.NO_SUCH_METHOD, "no such method: " + action));
        return false;
    }


    @Override
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        if (action.equals("downApk")) {
            DebugLog.i("wang","===raw==="+rawArgs);
            this.callbackContext=callbackContext;
            JSONObject jsonObject = new JSONObject(rawArgs);
            if (!jsonObject.isNull("apkName")) {
                apkName = jsonObject.getString("apkName");
            }
            if (!jsonObject.isNull("apkUrl")) {
                apkUrl = jsonObject.getString("apkUrl");
            }
            downApk(apkUrl, apkName,callbackContext);
            return true;
        }
        callbackContext.error(Utils.makeJSON(Constants.NO_SUCH_METHOD, "no such method: " + action));
        return false;
    }


    public void downApk(String apkUrl, String apkName,CallbackContext callbackContext) {
        this.apkUrl = apkUrl;
        this.apkName = apkName;
        if (PermissionHelper.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new UpdateManager(this.cordova.getActivity(), this.cordova,callbackContext).downloadApk(apkUrl, apkName);
        } else {
            PermissionHelper.requestPermission(this, REQUEST_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public UpdateManager getUpdateManager(JSONArray args, CallbackContext callbackContext)
            throws JSONException {

        if (this.updateManager == null) {
            this.updateManager = new UpdateManager(this.cordova.getActivity(), this.cordova,callbackContext);
        }

        return this.updateManager.options(args, callbackContext);
    }

    public void verifyStoragePermissions() {
        // Check if we have write permission
        // and if we don't prompt the user
        if (!cordova.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            cordova.requestPermissions(this, REQUEST_EXTERNAL_STORAGE, PERMISSIONS_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                switch (requestCode) {
                    case REQUEST_EXTERNAL_STORAGE:
                        ErrorResult errorResult=new ErrorResult();
                        errorResult.setErrorCode(Constant.PERMISSION_DENY);
                        errorResult.setDate(String.valueOf(REQUEST_EXTERNAL_STORAGE));
                        callbackContext.error(errorResult.toString());
                   
                        break;
                }
                //  this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, -1));

            }
            return;
        }
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                downApk(apkUrl, apkName,callbackContext);
                break;
        }
    }
}
