package com.plugin.br.cordova.face;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import com.ctj.swk.Myloan.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Random;
import android.widget.Toast;
import java.util.HashMap;
import android.util.Base64;

public class FacePlugin extends CordovaPlugin {
    private static int RESULT = 9111;
    HashMap<String, CallbackContext> callbackContextMap = new HashMap<String, CallbackContext>();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        callbackContextMap.put(action, callbackContext);
        ThreadPoolUtil.runTaskInThread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(cordova.getActivity());
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(cordova.getActivity());
                manager.registerLicenseManager(licenseManager);
                String uuid = ConUtil.getUUIDString(cordova.getActivity());
                manager.takeLicenseFromNetwork(uuid);
                if (licenseManager.checkCachedLicense() > 0) {
                    Intent intent = new Intent(cordova.getActivity(), LivenessActivity.class);
                    cordova.startActivityForResult(FacePlugin.this, intent, RESULT);
                }
            }
        });
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            upLoadFaceFile(bundle);
        } else {
            CallbackContext callbackContext = callbackContextMap.get("facego");
            if (callbackContext != null) {
                callbackContext.success("fail");
            }
        }
    }

    public void upLoadFaceFile(Bundle bundle) {
        CallbackContext callbackContext = callbackContextMap.get("facego");
        String result = "";
        int resultcode = 0;
        String delta = "";
        String deltaAdrr = "";
        String bestPath = "";
        String envPath = "";
        String action1Path = "";
        String action2Path = "";
        String action3Path = "";
        try {
            String resultOBJ = bundle.getString("result");
            JSONObject resultJsonObject = new JSONObject(resultOBJ);
            result = resultJsonObject.getString("result");
            resultcode = resultJsonObject.getInt("resultcode");
            boolean isSuccess = resultJsonObject.getString("result").equals(
                    cordova.getActivity().getResources().getString(R.string.verify_success));
            if (isSuccess) {
                delta = bundle.getString("delta");
                Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
                byte[] image_best = images.get("image_best");
                byte[] image_env = images.get("image_env");
                byte[] image_action1 = images.get("image_action1");
                byte[] image_action2 = images.get("image_action2");
                byte[] image_action3 = images.get("image_action3");
                bestPath = new String(Base64.encode(image_best, Base64.NO_WRAP));
                envPath = new String(Base64.encode(image_env, Base64.NO_WRAP));
                action1Path = new String(Base64.encode(image_action1, Base64.NO_WRAP));
                action2Path = new String(Base64.encode(image_action2, Base64.NO_WRAP));
                action3Path = new String(Base64.encode(image_action3, Base64.NO_WRAP));


//                bestPath = ConUtil.saveJPGFile(cordova.getActivity(), image_best, "image_best");
//                envPath = ConUtil.saveJPGFile(cordova.getActivity(), image_env, "image_env");
//                action1Path = ConUtil.saveJPGFile(cordova.getActivity(), image_action1, "image_action1");
//                action2Path = ConUtil.saveJPGFile(cordova.getActivity(), image_action2, "image_action2");
//                action3Path = ConUtil.saveJPGFile(cordova.getActivity(), image_action3, "image_action3");
//                if (TextUtils.isEmpty(result)) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (resultcode == 0) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (TextUtils.isEmpty(delta)) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (TextUtils.isEmpty(delta)) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (TextUtils.isEmpty(bestPath)) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (TextUtils.isEmpty(envPath)) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (TextUtils.isEmpty(action1Path)) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (TextUtils.isEmpty(action2Path)) {
//                    callbackContext.success("fail");
//                    return;
//                }
//                if (TextUtils.isEmpty(action3Path)) {
//                    callbackContext.success("fail");
//                    return;
//                }
                String all = "{\"result\":\"" + result + "\",\"resultcode\":\"" + resultcode + "\",\"delta\":\"" + delta + "\",\"image_best\":\"" + bestPath + "\",\"image_env\":\"" + envPath + "\",\"image_action1\":\"" + action1Path + "\",\"image_action2\":\"" + action2Path + "\",\"image_action3\":\"" + action3Path + "\"}";
                callbackContext.success(all);
            } else {
                callbackContext.success("fail");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.success("fail");
        }
    }

    public static String putStringSDC(String name, String s, Context mContext) {
        FileOutputStream outStream = null;
        OutputStreamWriter writer = null;
        try {
            File mediaStorageDir = mContext.getExternalFilesDir(Constant.cacheImage);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            String jpgFileName = System.currentTimeMillis() + "" + new Random().nextInt(1000000) + "_" + name + ".jpg";
            outStream = new FileOutputStream(mediaStorageDir + "/" + jpgFileName);
            writer = new OutputStreamWriter(outStream);
            writer.write(s);
            writer.flush();
            writer.close();//记得关闭
            outStream.close();
            return mediaStorageDir.getAbsolutePath() + "/" + jpgFileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }
}
