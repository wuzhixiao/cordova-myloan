package com.vaenow.appupdate;

import org.apache.cordova.BuildHelper;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import com.ctj.swk.Myloan.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LuoWen on 2015/12/14.
 */
public class DownloadHandler extends Handler {
    private String TAG = "DownloadHandler";

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    /* 记录进度条数量 */
    private int progress;
    /* 下载保存路径 */
    private String mSavePath;
    /* 保存解析的XML信息 */
    private HashMap<String, String> mHashMap;
    private MsgHelper msgHelper;
    private AlertDialog mDownloadDialog;
    private CallbackContext callbackContext;
    private String apkName;

    public DownloadHandler(Context mContext, ProgressBar mProgress, AlertDialog mDownloadDialog, String mSavePath, HashMap<String, String> mHashMap) {
        this.msgHelper = new MsgHelper(mContext.getPackageName(), mContext.getResources());
        this.mDownloadDialog = mDownloadDialog;
        this.mContext = mContext;
        this.mProgress = mProgress;
        this.mSavePath = mSavePath;
        this.mHashMap = mHashMap;
    }

    public DownloadHandler(Context mContext, String mSavePath,String apkName, CallbackContext callbackContext) {
        this.msgHelper = new MsgHelper(mContext.getPackageName(), mContext.getResources());
     //   this.mDownloadDialog = mDownloadDialog;
        this.mContext = mContext;
  //      this.mProgress = mProgress;
        this.mSavePath = mSavePath;
        this.callbackContext=callbackContext;
        this.apkName=apkName;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            // 正在下载
            case Constants.DOWNLOAD:
                // 设置进度条位置
                if(mProgress!=null){
                    mProgress.setProgress(progress);
                }
                Log.i("wang","下载进度:"+progress+"||"+callbackContext);
                if(callbackContext!=null){
                    JSONObject object=new JSONObject();
                    try {
                        object.put("status",1);
                        object.put("data",progress);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PluginResult pluginResult=new PluginResult(PluginResult.Status.OK,object);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                   // callbackContext.success(progress);
                }
                break;
            case Constants.DOWNLOAD_FINISH:
                if(mDownloadDialog!=null){
                    updateMsgDialog();
                }
                if(callbackContext!=null){
                    JSONObject object=new JSONObject();
                    try {
                        object.put("status",3);
                        object.put("data","");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PluginResult pluginResult=new PluginResult(PluginResult.Status.OK,object);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);

                }
                // 安装文件
                installApk();
                break;
               case Constants.DOWNLOAD_FAILTURE:
                   String error= (String) msg.obj;
                   if(callbackContext!=null){
                       JSONObject object=new JSONObject();
                       try {
                           object.put("status",2);
                           object.put("data",error);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       PluginResult pluginResult=new PluginResult(PluginResult.Status.OK,object);
                       pluginResult.setKeepCallback(true);
                       callbackContext.sendPluginResult(pluginResult);

                   }
                   break;
            default:
                break;
        }
    }

    public void updateProgress(int progress) {
        this.progress = progress;
    }

    public void updateMsgDialog() {
        if(mDownloadDialog!=null){
            mDownloadDialog.setTitle(msgHelper.getString(MsgHelper.DOWNLOAD_COMPLETE_TITLE));
            if (mDownloadDialog.isShowing()) {
                mDownloadDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE); //Update in background
                mDownloadDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.VISIBLE); //Install Manually
                mDownloadDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.VISIBLE); //Download Again

                mDownloadDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(downloadCompleteOnClick);
            }
        }
    }

    private OnClickListener downloadCompleteOnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            installApk();
        }
    };

    /**
     * 安装APK文件
     */
    private void installApk() {
        LOG.d(TAG, "Installing APK");

        File apkFile = new File(mSavePath, apkName+".apk");
        if (!apkFile.exists()) {
            LOG.e(TAG, "Could not find APK: " +apkName);
            return;
        }

        LOG.d(TAG, "APK Filename: " + apkFile.toString());

        // 通过Intent安装APK文件
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            LOG.d(TAG, "Build SDK Greater than or equal to Nougat");
            String applicationId = (String) BuildHelper.getBuildConfigValue((Activity) mContext, "APPLICATION_ID");
			// DebugLog.i("wang","=====applicationId==="+applicationId);
            Uri apkUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            Intent i = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            i.setDataAndType(apkUri, /*"application/vnd.android.package-archive"*/mContext.getContentResolver().getType(apkUri));
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivity(i);
        }else{
            LOG.d(TAG, "Build SDK less than Nougat");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
            mContext.startActivity(i);
        }

    }
}
