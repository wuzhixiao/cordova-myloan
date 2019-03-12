package com.plugin.br.cordova.contact;

import android.Manifest;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.plugin.br.cordova.Constant;
import com.plugin.br.cordova.bean.CallRecordReq;
import com.plugin.br.cordova.bean.ErrorResult;
import com.plugin.br.cordova.bean.SmsInfo;
import com.plugin.br.cordova.bean.userContactInputVOS;
import com.plugin.br.cordova.util.ContactUtil;
import com.plugin.br.cordova.util.DebugLog;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;

import com.plugin.br.cordova.bean.PackInfo;
import com.plugin.br.cordova.util.PackageManagerUtil;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;


/**
 * 联系人插件
 * Created by Administrator on 2018/9/12 0012.
 */

public class ContactCordovaPlugin extends CordovaPlugin {
    public static final String GET_CONTACT = "getContact";
    public static final String GET_CALLLOG = "getCallLog";
    public static final String GET_FIRST_CALLLOG = "getFirstCallLog";
	public static final String GET_CALLLOG_SIZE = "getCallLogSize";
    public static final String GET_SMS = "getSMS";
    public static final String GET_FIRST_SMS = "getFistSMS";
	public static final String GET_SMS_SIZE = "getSMSSize";

    public HashMap<String, CallbackContext> callbackContextHashMap = new HashMap<String,CallbackContext>();
    private Gson gson=new Gson();

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (GET_CONTACT.equals(action)) {
            callbackContextHashMap.put(GET_CONTACT, callbackContext);
            readContact();
            return true;
        }
        if (GET_CALLLOG.equals(action)) {
            callbackContextHashMap.put(GET_CALLLOG, callbackContext);
            readCallLog();
            return true;
        }
        if (GET_FIRST_CALLLOG.equals(action)) {
            callbackContextHashMap.put(GET_FIRST_CALLLOG, callbackContext);
            readFirstCallLog();
            return true;
        }
        if(GET_SMS.equals(action)){
            callbackContextHashMap.put(GET_SMS,callbackContext);
            readSMS();
            return true;
        }
        if(GET_FIRST_SMS.equals(action)){
            callbackContextHashMap.put(GET_FIRST_SMS,callbackContext);
            readFistSMS();
            return true;
        }
        if(GET_SMS_SIZE.equals(action)){
            callbackContextHashMap.put(GET_SMS_SIZE,callbackContext);
            readSizeSMS();
            return true;
        }
		if(GET_CALLLOG_SIZE.equals(action)){
			callbackContextHashMap.put(GET_CALLLOG_SIZE,callbackContext);
            readCallLogSize();
			return true;
		}
        if ("getInsallPack".equals(action)) {
            getPackInfo(callbackContext);
            return true;
        }
        return false;
    }

    public void getPackInfo(CallbackContext callbackContext) {
        List<PackInfo> packInfoList = PackageManagerUtil.getAppPackageInfo(cordova.getActivity(), true);
        Gson gson = new Gson();
        DebugLog.i("wang", "==list==" + gson.toJson(packInfoList));
        if (callbackContext != null) {
            callbackContext.success(gson.toJson(packInfoList));
        }
    }
    /**
     * 读取通讯录
     */
    public void readContact() {
        if (PermissionHelper.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
            List<userContactInputVOS> listContact = ContactUtil.readContactMessage(cordova.getActivity());
            reusltContact(listContact);
        } else {
            PermissionHelper.requestPermission(this, Constant.Manifest.READ_CONTACTS, Manifest.permission.READ_CONTACTS);
        }
    }

    /**
     * 获取第一条通话记录
     */
    public void readFirstCallLog() {
        if (PermissionHelper.hasPermission(this, Manifest.permission.READ_CALL_LOG)) {
            CallRecordReq.ListBean firstReadCallLog = ContactUtil.readFirstReadCallLog(cordova.getActivity());
            DebugLog.i("wang", "==readFirstCallLog==" + firstReadCallLog.toString());
            resultFirstCallLog(firstReadCallLog);
        } else {
            PermissionHelper.requestPermission(this, Constant.Manifest.READ_FIRST_CALL_LOG, Manifest.permission.READ_CALL_LOG);
        }
    }

    /**
     * 获取通话记录
     */
    public void readCallLog() {
        if (PermissionHelper.hasPermission(this, Manifest.permission.READ_CALL_LOG)) {
            List<CallRecordReq.ListBean> listBeanList = ContactUtil.readCallLog(cordova.getActivity());
            DebugLog.i("wang", "==readCallLog==" + listBeanList.toString());
            resultCallLog(listBeanList);
        } else {
            PermissionHelper.requestPermission(this, Constant.Manifest.READ_CALL_LOG, Manifest.permission.READ_CALL_LOG);
        }
    }
	
	    /**
     * 获取通话记录条数
     */
    public void readCallLogSize() {
        if (PermissionHelper.hasPermission(this,Manifest.permission.READ_CALL_LOG)) {
            int callLogSize = ContactUtil.getCallLogSize(cordova.getActivity());
            DebugLog.i("wang", "==readCallLogSize==" + callLogSize);
            resultCallLogSize(callLogSize);
        } else {
            PermissionHelper.requestPermission(this, Constant.Manifest.READ_CALL_LOG_SIZE, Manifest.permission.READ_CALL_LOG);
        }
    }


    /**
     * 读取短信
     */
    public void readSMS() {
        if (PermissionHelper.hasPermission(this, Manifest.permission.READ_SMS)) {
            List<SmsInfo.ListBean> listSms = ContactUtil.readSms(cordova.getActivity());
            resultSMS(listSms);
        } else {
            PermissionHelper.requestPermission(this, Constant.Manifest.READ_SMS, Manifest.permission.READ_SMS);
        }
    }
    /**
     * 读取第一条短信
     */
    public void readFistSMS() {
        if (PermissionHelper.hasPermission(this, Manifest.permission.READ_SMS)) {
            SmsInfo.ListBean sms = ContactUtil.readFirstSms(cordova.getActivity());
            resultFistSMS(sms);
        } else {
            PermissionHelper.requestPermission(this, Constant.Manifest.READ_FIRST_SMS, Manifest.permission.READ_SMS);
        }
    }
	
	    /**
     * 读取第短信数量
     */
    public void readSizeSMS() {
        if (PermissionHelper.hasPermission(this, Manifest.permission.READ_SMS)) {
             int smsAccount = ContactUtil.readSmsSize(cordova.getActivity());
            resultSmsSize(smsAccount);
        } else {
            PermissionHelper.requestPermission(this, Constant.Manifest.READ_SMS_SIZE, Manifest.permission.READ_SMS);
        }
    }

    /**
     * 回调通讯率
     *
     * @param listContact
     */
    public void reusltContact(List<userContactInputVOS> listContact) {
        if (callbackContextHashMap.get(GET_CONTACT) != null) {
            CallbackContext callbackContext = callbackContextHashMap.get(GET_CONTACT);
            if (listContact != null) {
                DebugLog.i("wang","==result_contact=="+gson.toJson(listContact));
                callbackContext.success(gson.toJson(listContact));
            }
        }
    }

    /**
     * 回调通话记录
     *
     * @param listRecallLog
     */
    public void resultCallLog(List<CallRecordReq.ListBean> listRecallLog) {
       /* JSONObject object = new JSONObject();
        try {
            object.put("code", 1);
            object.put("data", listRecallLog);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        if (callbackContextHashMap.get(GET_CALLLOG) != null) {
            CallbackContext callbackContext = callbackContextHashMap.get(GET_CALLLOG);
            if (listRecallLog != null) {
                DebugLog.i("wang","==result_contact=="+gson.toJson(listRecallLog));
                callbackContext.success(gson.toJson(listRecallLog));
            }
        }
    }

	
	
    /**
     * 回调通话大小
     *
     * @param size
     */
    public void resultCallLogSize(int size) {
       /* JSONObject object = new JSONObject();
        try {
            object.put("code", 1);
            object.put("data", listRecallLog);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        if (callbackContextHashMap.get(GET_CALLLOG_SIZE) != null) {
            CallbackContext callbackContext = callbackContextHashMap.get(GET_CALLLOG_SIZE);
            if (callbackContext != null) {
                DebugLog.i("wang","==result_callLog_size=="+size);
                callbackContext.success(size);
            }
        }
    }

    /**
     * 回调第一条通话记录
     *
     * @param listBean
     */
    public void resultFirstCallLog(CallRecordReq.ListBean listBean) {
        if (callbackContextHashMap.get(GET_FIRST_CALLLOG) != null) {
            CallbackContext callbackContext = callbackContextHashMap.get(GET_FIRST_CALLLOG);
            if (callbackContext != null) {
                DebugLog.i("wang","==result_first_callLog=="+gson.toJson(listBean));
                callbackContext.success(gson.toJson(listBean));
            }
        }
    }

    /**
     * 回调短信
     *
     * @param smsList
     */
    public void resultSMS(List<SmsInfo.ListBean> smsList) {
        if (callbackContextHashMap.get(GET_SMS) != null) {
            CallbackContext callbackContext = callbackContextHashMap.get(GET_SMS);
            if (smsList != null) {
                DebugLog.i("wang","==result__SMS=="+gson.toJson(smsList));
                callbackContext.success(gson.toJson(smsList));
            }

        }
    } 
	/**
     * 回调第一条短信
     *
     * @param sms
     */
    public void resultFistSMS(SmsInfo.ListBean sms) {
        if (callbackContextHashMap.get(GET_FIRST_SMS) != null) {
            CallbackContext callbackContext = callbackContextHashMap.get(GET_FIRST_SMS);
            if (sms != null) {
                DebugLog.i("wang","==result_first__SMS=="+gson.toJson(sms));
                callbackContext.success(gson.toJson(sms));
            }
        }
    }
 /**
     * 回调短信数量
     *
     * @param size
     */
    public void resultSmsSize(int size) {
        if (callbackContextHashMap.get(GET_SMS_SIZE) != null) {
            CallbackContext callbackContext = callbackContextHashMap.get(GET_SMS_SIZE);    
                DebugLog.i("wang","==result__SMS_size=="+size);
				if(callbackContext!=null){
					 callbackContext.success(size);
				}
               
          
        }
    }
	


    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                switch (requestCode) {
                    case Constant.Manifest.READ_CALL_LOG:
                        if (callbackContextHashMap.get(GET_CALLLOG) != null) {
                            CallbackContext callbackContext = callbackContextHashMap.get(GET_CALLLOG);
                            ErrorResult errorResult=new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.READ_CALL_LOG));
                            callbackContext.error(errorResult.toString());
                        }
                        break;
                    case Constant.Manifest.READ_FIRST_CALL_LOG:
                        if (callbackContextHashMap.get(GET_FIRST_CALLLOG) != null) {
                            CallbackContext callbackContext = callbackContextHashMap.get(GET_CALLLOG);
                            ErrorResult errorResult=new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.READ_FIRST_CALL_LOG));
                            callbackContext.error(errorResult.toString());
                        }
                        break;
                    case Constant.Manifest.READ_CONTACTS:
                        if (callbackContextHashMap.get(GET_CONTACT) != null) {
                            CallbackContext callbackContext = callbackContextHashMap.get(GET_CONTACT);
                            ErrorResult errorResult=new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.READ_CONTACTS));
                            callbackContext.error(errorResult.toString());
                        }
                        break;
                    case Constant.Manifest.READ_SMS:
                        if (callbackContextHashMap.get(GET_SMS) != null) {
                            CallbackContext callbackContext = callbackContextHashMap.get(GET_SMS);
                            ErrorResult errorResult=new ErrorResult();
                            errorResult.setErrorCode(Constant.PERMISSION_DENY);
                            errorResult.setDate(String.valueOf(Constant.Manifest.READ_SMS));
                            callbackContext.error(errorResult.toString());
                        }
                      case Constant.Manifest.READ_FIRST_SMS:
                          if(callbackContextHashMap.get(GET_FIRST_SMS)!=null){
                              CallbackContext callbackContext = callbackContextHashMap.get(GET_FIRST_SMS);
                              ErrorResult errorResult=new ErrorResult();
                              errorResult.setErrorCode(Constant.PERMISSION_DENY);
                              errorResult.setDate(String.valueOf(Constant.Manifest.READ_FIRST_SMS));
                              callbackContext.error(errorResult.toString());
                          }
						case Constant.Manifest.READ_SMS_SIZE:
                         	 if(callbackContextHashMap.get(GET_SMS_SIZE)!=null){
                              CallbackContext callbackContext = callbackContextHashMap.get(GET_SMS_SIZE);
                              ErrorResult errorResult=new ErrorResult();
                              errorResult.setErrorCode(Constant.PERMISSION_DENY);
                              errorResult.setDate(String.valueOf(Constant.Manifest.READ_SMS_SIZE));
                              callbackContext.error(errorResult.toString());
                          }	
						  break;
						  
						  
						 case Constant.Manifest.READ_CALL_LOG_SIZE	:
                         	 if(callbackContextHashMap.get(GET_CALLLOG_SIZE	)!=null){
                              CallbackContext callbackContext = callbackContextHashMap.get(GET_CALLLOG_SIZE);
                              ErrorResult errorResult=new ErrorResult();
                              errorResult.setErrorCode(Constant.PERMISSION_DENY);
                              errorResult.setDate(String.valueOf(Constant.Manifest.READ_CALL_LOG_SIZE));
                              callbackContext.error(errorResult.toString());
                          }	

					  
                        break;

                }
                //  this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, -1));
                return;
            }
        }
        switch (requestCode) {
            case Constant.Manifest.READ_CONTACTS:
                List<userContactInputVOS> contactInputVOSList = ContactUtil.readContact(cordova.getActivity());

                reusltContact(contactInputVOSList);
                break;
            case Constant.Manifest.READ_CALL_LOG:
                List<CallRecordReq.ListBean> listBeanList = ContactUtil.readCallLog(cordova.getActivity());
                DebugLog.i("wang", "==readCallLog=Allow=" + listBeanList);
                resultCallLog(listBeanList);
                break;
            case Constant.Manifest.READ_FIRST_CALL_LOG:
                CallRecordReq.ListBean callLogFirst = ContactUtil.readFirstReadCallLog(cordova.getActivity());
                resultFirstCallLog(callLogFirst);
                break;
            case Constant.Manifest.READ_SMS:
                List<SmsInfo.ListBean> listSms= ContactUtil.readSms(cordova.getActivity());
                resultSMS(listSms);
                break;
            case Constant.Manifest.READ_FIRST_SMS:
                SmsInfo.ListBean smsInfo=ContactUtil.readFirstSms(cordova.getActivity());
                resultFistSMS(smsInfo);
                break;
		    case Constant.Manifest.READ_SMS_SIZE:
			    int size= ContactUtil.readSmsSize(cordova.getActivity());
				resultSmsSize(size);
                break;  		
            case Constant.Manifest.READ_CALL_LOG_SIZE:
                 int callLogSize= ContactUtil.getCallLogSize(cordova.getActivity());
				 resultCallLogSize(callLogSize);
			break;			
        }
    }
}
