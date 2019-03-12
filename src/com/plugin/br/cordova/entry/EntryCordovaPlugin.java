package com.plugin.br.cordova.entry;

import com.plugin.br.cordova.util.DESCoder;
import com.plugin.br.cordova.util.DebugLog;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

/**
 * Created by Administrator on 2018/9/19 0019.
 */

public class EntryCordovaPlugin extends CordovaPlugin {
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }


    @Override
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        if ("Crypto".equals(action)) {
            DebugLog.i("wang","加密前："+rawArgs);
            String entry= DESCoder.encryData(rawArgs);
            DebugLog.i("wang","加密后："+entry);
            callbackContext.success(entry);
            return true;
        }
        if ("Decrypto".equals(action)) {
            callbackContext.success(rawArgs);
            return true;
        }
        return false;
    }
}
