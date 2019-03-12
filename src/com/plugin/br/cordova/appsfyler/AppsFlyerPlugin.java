package com.plugin.br.cordova.appsfyler;

import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.plugin.br.cordova.util.DebugLog;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * appsFlyer 插件
 */
public class AppsFlyerPlugin extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        DebugLog.i("wang", "====appsflyer====" + action);
        if ("loginEven".equals(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> eventValue = new HashMap<String, Object>();
                    eventValue.put(AFInAppEventParameterName.LEVEL, 9);
                    eventValue.put(AFInAppEventParameterName.SCORE, 100);
                    AppsFlyerLib.getInstance().trackEvent(cordova.getActivity().getApplicationContext(), AFInAppEventType.LOGIN, eventValue);
                    DebugLog.i("wang", "记录登陆成功");
                }
            });
            return true;
        }
        if ("regitEven".equals(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> eventValue = new HashMap<String, Object>();
                    eventValue.put(AFInAppEventParameterName.LEVEL, 9);
                    eventValue.put(AFInAppEventParameterName.SCORE, 100);
                    AppsFlyerLib.getInstance().trackEvent(cordova.getActivity().getApplicationContext(), AFInAppEventType.COMPLETE_REGISTRATION, eventValue);
                    DebugLog.i("wang", "记录注册信息");
                }
            });
            return true;
        }
        if ("orderEven".equals(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> eventValue = new HashMap<String, Object>();
                    eventValue.put(AFInAppEventParameterName.LEVEL, 9);
                    eventValue.put(AFInAppEventParameterName.SCORE, 100);
                    AppsFlyerLib.getInstance().trackEvent(cordova.getActivity().getApplicationContext(), "ordenSucess", eventValue);
                    DebugLog.i("wang", "下单成功");
                }
            });
            return true;
        }
        if ("initAppsFlyer".equals(action)) {
            DebugLog.i("wang", "=====initialize====");
            AppsFlyerLib.getInstance().init("mjBybJPN4C2dM3s9FUVS4g", new AppStusAppsFlyerConversionListener(), cordova.getActivity().getApplicationContext());
            AppsFlyerLib.getInstance().startTracking(cordova.getActivity().getApplication());
            return true;
        }
        if ("getAppsFlyerId".equals(action)) {
            DebugLog.i("wang", "===appsflyerId===" + AppsFlyerLib.getInstance().getAppsFlyerUID(cordova.getActivity().getApplicationContext()));
            callbackContext.success(AppsFlyerLib.getInstance().getAppsFlyerUID(cordova.getActivity().getApplicationContext()));
            return true;
        }
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, 9);
        eventValue.put(AFInAppEventParameterName.SCORE, 100);
        AppsFlyerLib.getInstance().trackEvent(cordova.getActivity().getApplicationContext(), action, eventValue);
        return true;


    }


    class AppStusAppsFlyerConversionListener implements AppsFlyerConversionListener {

        @Override
        public void onInstallConversionDataLoaded(Map<String, String> conversionData) {

            for (String attrName : conversionData.keySet()) {
                Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                if (conversionData.containsKey("media_source")) {
                    //  ToastUtil.showToast(conversionData.get("media_source"));
                }
            }

        }

        @Override
        public void onInstallConversionFailure(String errorMessage) {
            Log.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
        }

        @Override
        public void onAppOpenAttribution(Map<String, String> map) {

        }

        @Override
        public void onAttributionFailure(String errorMessage) {
            Log.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
        }
    }
}
