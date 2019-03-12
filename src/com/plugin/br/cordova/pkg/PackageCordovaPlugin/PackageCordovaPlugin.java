package com.plugin.br.cordova.pkg;

import com.google.gson.Gson;
import com.plugin.br.cordova.bean.PackInfo;
import com.plugin.br.cordova.util.DebugLog;
import com.plugin.br.cordova.util.PackageManagerUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**包管理插件
 * Created by Administrator on 2018/9/12 0012.
 */

public class PackageCordovaPlugin extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if("getInsallPack".equals(action)){
            getPackInfo(callbackContext);
           return true;
        }
        return false;
    }

    public void getPackInfo(CallbackContext callbackContext){
            List<PackInfo> packInfoList= PackageManagerUtil.getAppPackageInfo(cordova.getActivity(),true);
            Gson gson=new Gson();
            DebugLog.i("wang","==list=="+ gson.toJson(packInfoList));
            if(callbackContext!=null){
                callbackContext.success(gson.toJson(packInfoList));
            }
        }

}
