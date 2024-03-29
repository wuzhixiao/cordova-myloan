package com.plugin.br.cordova.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;

import com.plugin.br.cordova.Constant;
import com.plugin.br.cordova.bean.CallRecordReq;
import com.plugin.br.cordova.bean.SmsInfo;
import com.plugin.br.cordova.bean.userContactInputVOS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class ContactUtil {
    public static final int SMS_ACCOUNT =249;
    public static final int CALLLOG_ACCOUNT =400;
    /**
     * 读取联系人
     * @param context
     * @return
     */
    public static List<userContactInputVOS> readContact(Context context){
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<userContactInputVOS> list=new ArrayList<userContactInputVOS>();
        long start= System.currentTimeMillis();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long start1= System.currentTimeMillis();
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                //   list.add(displayName + '\n' + number);
                userContactInputVOS userContactInputVOS = new userContactInputVOS();
//                userContactInputVOS.setContactName(displayName);
//                userContactInputVOS.setContactPhone(number);
                userContactInputVOS.setId(id + "");
                DebugLog.i("wang", "id:" + id + "==name==" + displayName + "||" + "=phone==" + number);
                list.add(userContactInputVOS);
                DebugLog.i("wang","==contact_item_time=="+(System.currentTimeMillis()-start1));
            }
            DebugLog.i("wang","==contact_total_time=="+(System.currentTimeMillis()-start));
            //notify公布

        }
        return list;
    }

    public static List<userContactInputVOS> readContactMessage(Context context) {
        List<userContactInputVOS> list = new ArrayList();
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            //新建一个联系人实例
            userContactInputVOS userContactInputVOS = new userContactInputVOS();
            List<String> phoneNumberList = new ArrayList<String>();
            List<String> emailList = new ArrayList<String>();
            List<String> addressList = new ArrayList<String>();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            userContactInputVOS.setContactName(name);
            userContactInputVOS.setId(contactId);
            //获取联系人所有电话号
            Cursor phones = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            if (phones != null) {
                while (phones.moveToNext()) {

                String phoneNumber = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNumberList.add(phoneNumber);
                }
                userContactInputVOS.setContactPhones(phoneNumberList);
            }
            phones.close();
            //获取联系人所有邮箱.
            Cursor emails = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
                            + contactId, null, null);

            if (emails != null) {
            while (emails.moveToNext()) {
                String email = emails
                        .getString(emails
                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    emailList.add(email);

            }
                userContactInputVOS.setContactEmails(emailList);
            }
            emails.close();

            //
            Cursor address = context.getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                    null, null);
            if (address != null) {
                while (address.moveToNext()) {
                    String workAddress = address.getString(address.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredPostal.DATA));
                    addressList.add(workAddress);

                }
                userContactInputVOS.setContactAdresses(addressList);
            }

            list.add(userContactInputVOS);
        }

        return list;
    }


    /**
     * 读取第一条通话记录数据
     * @param context
     * @return
     */
    public static CallRecordReq.ListBean readFirstReadCallLog(Context context){
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION
        }, null, null, "date asc"+" limit 1");
        CallRecordReq.ListBean userCallRecordInputVOSBean=null;
        long start= System.currentTimeMillis();
        if (cursor.moveToNext()) {
            long start1= System.currentTimeMillis();
            userCallRecordInputVOSBean = new CallRecordReq.ListBean();
            String callName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //名称
            String callNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            //如果名字为空，在通讯录查询一次有没有对应联系人
            if (callName == null || callName.equals("")) {
                callName = "未知";
            }
            //通话类型
            int callType = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            String callTypeStr = "";
            switch (callType) {
                case CallLog.Calls.INCOMING_TYPE:
                    //   callTypeStr = CallLogInfo.CALLIN;
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    //    callTypeStr = CallLogInfo.CALLOUT;
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    //  callTypeStr = CallLogInfo.CAllMISS;
                    break;
                default:
                    //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
                    Log.i("ssss", "" + callType);
                    break;
                    // i--;
                //    continue;
            }
            String callDurationStr = "";
            if (callType == CallLog.Calls.MISSED_TYPE) {

            } else {
                callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            }
            String callDateStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            //     String callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                     /*   Log.i("Msg", "callName" + callName);
                        Log.i("Msg", "callNumber" + callNumber);
                        Log.i("Msg", "callTypeStr" + callType);
                        Log.i("Msg", "callDateStr" + callDateStr);
                        Log.i("Msg", "callDurationStr" + callDurationStr);*/
            userCallRecordInputVOSBean.setCallerName(callName);
            userCallRecordInputVOSBean.setCallPhoneNumber(callNumber);
            userCallRecordInputVOSBean.setCallType(String.valueOf(callType));
            userCallRecordInputVOSBean.setCallStartTime(callDateStr);
            userCallRecordInputVOSBean.setTimeKey(Long.parseLong(callDateStr));
            if(!TextUtils.isEmpty(callDurationStr)){
                userCallRecordInputVOSBean.setCallDuration(callDurationStr);
            }else{
                userCallRecordInputVOSBean.setCallDuration("0");
            }
            DebugLog.i("wang","==callLog__first_item_time=="+(System.currentTimeMillis()-start1));
         //   DebugLog.i("wang","==phone==="+callNumber+"||"+"=callDateStr==="+DateUtil.TimeStamp2Date(callDateStr,DateUtil.DATE_FORMAT,Locale.CANADA));
        }else{
           userCallRecordInputVOSBean= new CallRecordReq.ListBean();
        }
        DebugLog.i("wang","==callLog_First_time=="+(System.currentTimeMillis()-start));
        cursor.close();
      return userCallRecordInputVOSBean;
    }
	
	
	 /**
     * 获取通讯录条数
     *
     * @return
     */
    public static int getCallLogSize(Context context) {
        final List<CallRecordReq.ListBean> listContact = new ArrayList<CallRecordReq.ListBean>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION
        }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        long start= System.currentTimeMillis();
        while (cursor.moveToNext()) {
            long start1= System.currentTimeMillis();
            CallRecordReq.ListBean userCallRecordInputVOSBean = new CallRecordReq.ListBean();
                      /*  for (cursor.moveToFirst(); (!cursor.isAfterLast())*//* && i < num; cs.moveToNext(), i++*//*) {*/
            String callName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //名称
            String callNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            //如果名字为空，在通讯录查询一次有没有对应联系人
            if (callName == null || callName.equals("")) {
                callName = "未知";
                               /* String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
                                //设置查询条件
                                String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + callNumber + "'";
                                Cursor cs = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        cols, selection, null, null);
                                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                                if (cs.getCount() > 0) {
                                    cs.moveToFirst();
                                    callName = cs.getString(nameFieldColumnIndex);
                                }
                                cs.close();*/
            }
            //通话类型
            int callType = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            String callTypeStr = "";
            switch (callType) {
                case CallLog.Calls.INCOMING_TYPE:
                    //   callTypeStr = CallLogInfo.CALLIN;
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    //    callTypeStr = CallLogInfo.CALLOUT;
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    //  callTypeStr = CallLogInfo.CAllMISS;
                    break;
                default:
                    //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
                    Log.i("ssss", "" + callType);
                    // i--;
                    continue;
            }
            String callDurationStr = "";
            if (callType == CallLog.Calls.MISSED_TYPE) {

            } else {
                callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            }
            String callDateStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            //     String callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                     /*   Log.i("Msg", "callName" + callName);
                        Log.i("Msg", "callNumber" + callNumber);
                        Log.i("Msg", "callTypeStr" + callType);
                        Log.i("Msg", "callDateStr" + callDateStr);
                        Log.i("Msg", "callDurationStr" + callDurationStr);*/
            userCallRecordInputVOSBean.setCallerName(callName);
            userCallRecordInputVOSBean.setCallPhoneNumber(callNumber);
            userCallRecordInputVOSBean.setCallType(String.valueOf(callType));
            userCallRecordInputVOSBean.setCallStartTime(callDateStr);
            userCallRecordInputVOSBean.setTimeKey(Long.parseLong(callDateStr));
            if(!TextUtils.isEmpty(callDurationStr)){
                userCallRecordInputVOSBean.setCallDuration(callDurationStr);
            }else{
                userCallRecordInputVOSBean.setCallDuration("0");
            }
            listContact.add(userCallRecordInputVOSBean);
            DebugLog.i("wang","==callLog_itemNum_time=="+(System.currentTimeMillis()-start1));
        }
        DebugLog.i("wang","==callLog_totalNum_time=="+(System.currentTimeMillis()-start));
        cursor.close();
        return listContact.size();
    }
	
    /**
     * 读取通话记录
     * @param context
     * @return
     */
    public static List<CallRecordReq.ListBean> readCallLog(Context context){
        final List<CallRecordReq.ListBean> listContact = new ArrayList<CallRecordReq.ListBean>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION
        }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER+" limit "+CALLLOG_ACCOUNT);
        long start= System.currentTimeMillis();
        while (cursor.moveToNext()) {
            long start1= System.currentTimeMillis();
            CallRecordReq.ListBean userCallRecordInputVOSBean = new CallRecordReq.ListBean();
                      /*  for (cursor.moveToFirst(); (!cursor.isAfterLast())*//* && i < num; cs.moveToNext(), i++*//*) {*/
            String callName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //名称
            String callNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            //如果名字为空，在通讯录查询一次有没有对应联系人
            if (callName == null || callName.equals("")) {
                callName = "未知";
                               /* String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
                                //设置查询条件
                                String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + callNumber + "'";
                                Cursor cs = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        cols, selection, null, null);
                                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                                if (cs.getCount() > 0) {
                                    cs.moveToFirst();
                                    callName = cs.getString(nameFieldColumnIndex);
                                }
                                cs.close();*/
            }
            //通话类型
            int callType = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            String callTypeStr = "";
            switch (callType) {
                case CallLog.Calls.INCOMING_TYPE:
                    //   callTypeStr = CallLogInfo.CALLIN;
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    //    callTypeStr = CallLogInfo.CALLOUT;
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    //  callTypeStr = CallLogInfo.CAllMISS;
                    break;
                default:
                    //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
                    Log.i("ssss", "" + callType);
                    // i--;
                    continue;
            }
            String callDurationStr = "";
            if (callType == CallLog.Calls.MISSED_TYPE) {

            } else {
                callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            }
            String callDateStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            //     String callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                     /*   Log.i("Msg", "callName" + callName);
                        Log.i("Msg", "callNumber" + callNumber);
                        Log.i("Msg", "callTypeStr" + callType);
                        Log.i("Msg", "callDateStr" + callDateStr);
                        Log.i("Msg", "callDurationStr" + callDurationStr);*/
            userCallRecordInputVOSBean.setCallerName(callName);
            userCallRecordInputVOSBean.setCallPhoneNumber(callNumber);
            userCallRecordInputVOSBean.setCallType(String.valueOf(callType));
            userCallRecordInputVOSBean.setCallStartTime(callDateStr);
            userCallRecordInputVOSBean.setTimeKey(Long.parseLong(callDateStr));
            if(!TextUtils.isEmpty(callDurationStr)){
                userCallRecordInputVOSBean.setCallDuration(callDurationStr);
            }else{
                userCallRecordInputVOSBean.setCallDuration("0");
            }
            listContact.add(userCallRecordInputVOSBean);
            DebugLog.i("wang","==callLog_item_time=="+(System.currentTimeMillis()-start1));
        }
        cursor.close();
        DebugLog.i("wang","==callLog_total_time=="+(System.currentTimeMillis()-start));
        return listContact;
    }

    /**
     * 读取第一条短信
     * @param context
     * @return
     */
    public static SmsInfo.ListBean readFirstSms(Context context){
     //  List<SmsInfo.ListBean> listSms=new ArrayList<>();
        String[] field = {Telephony.Sms._ID,Telephony.Sms.ADDRESS, Telephony.Sms.PERSON,Telephony.Sms.BODY, Telephony.Sms.DATE,Telephony.Sms.TYPE};  //字段
        ContentResolver contentResolver = context.getContentResolver();  //内容解析器(数据共享)——读取短信内容

        Date date0=new Date();
        int year=date0.getYear();
        int month=date0.getMonth()-5;
        int day=date0.getDate();
        Date date1=new Date(year,month,day);
        long start= System.currentTimeMillis();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/"),field,/* Telephony.Sms.DATE+">?"*/null,/*new String[]{time}*/null, "date asc limit 1");  //获取短信内容

        if (cursor == null) {  //游标无内容
            return null;
        }

        //  list = new ArrayList<MesInfo>();
        SmsInfo.ListBean smsInfo=null;
       if (cursor.moveToNext()) {
            long start1= System.currentTimeMillis();
            smsInfo=new SmsInfo.ListBean();
            int id=cursor.getInt(cursor.getColumnIndex(Telephony.Sms._ID));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));  //手机号 发件人地址，即手机号，如+8613811810000
            //   String name = cursor.getString(cursor.getColumnIndex(Telephony.Sms.PERSON));  //联系人
            String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));  //短信内容
            //格式化短信日期
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date time = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
            String date = simpleDateFormat.format(time);*/
            String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));
            //  DebugLog.i("wang","==time=="+DateUtil.TimeStamp2Date(String.valueOf(date),DateUtil.DATE_FORMAT,context.getResources().getConfiguration().locale));
            String type = cursor.getString(cursor.getColumnIndex(Telephony.Sms.TYPE));//type 1接受，2 发送
            smsInfo.setId(id);
            smsInfo.setSmsPartyNumber(phoneNumber);
            //    smsInfo.setPerson(name);
            smsInfo.setSmsContent(CharFilter.filterCharToNormal(body));
            smsInfo.setSmsTime(date);
            smsInfo.setTimeKey(Long.parseLong(date));
            smsInfo.setSmsStatus(type);
            DebugLog.i("wang", "==phoneNum==" + phoneNumber  + "==body===" + CharFilter.filterCharToNormal(body) + "==date==" +DateUtil.TimeStamp2Date(date,DateUtil.DATE_FORMAT, Locale.CANADA) + "==type==" + type);
            DebugLog.i("wang","==sms_first_item_time=="+(System.currentTimeMillis()-start1));
            //保存短信信息
            //   list.add(new MesInfo(name, phoneNumber, date, body));
        }else{
            smsInfo=new SmsInfo.ListBean(); 
        }
        DebugLog.i("wang","==sms_first_time=="+(System.currentTimeMillis()-start));
        return smsInfo;
    }
    /**
     * 读取短信
     * @param context
     * @return
     */
  public static List<SmsInfo.ListBean> readSms(Context context){
      List<SmsInfo.ListBean> listSms=new ArrayList<SmsInfo.ListBean>();
      String[] field = {Telephony.Sms._ID,Telephony.Sms.ADDRESS, Telephony.Sms.PERSON,Telephony.Sms.BODY, Telephony.Sms.DATE,Telephony.Sms.TYPE};  //字段
      ContentResolver contentResolver = context.getContentResolver();  //内容解析器(数据共享)——读取短信内容

      Date date0=new Date();
      int year=date0.getYear();
      int month=date0.getMonth()-5;
      int day=date0.getDate();
      Date date1=new Date(year,month,day);

    /*  String formateDate=DateUtil.formatDate(date1,DateUtil.DATE_FORMAT);
      DebugLog.i("wang","==format=="+formateDate);
      String time=DateUtil.Date2TimeStamp(formateDate,DateUtil.DATE_FORMAT);*/
   //   DebugLog.i("wang","==start=="+DateUtil.TimeStamp2Date(String.valueOf(time),DateUtil.DATE_FORMAT,context.getResources().getConfiguration().locale));limit 600
      long start= System.currentTimeMillis();
      Cursor cursor = contentResolver.query(Uri.parse("content://sms/"),field,/* Telephony.Sms.DATE+">?"*/null,/*new String[]{time}*/null, "date desc limit "+SMS_ACCOUNT);  //获取短信内容

      if (cursor == null) {  //游标无内容
          return null;
      }

      //  list = new ArrayList<MesInfo>();
      while (cursor.moveToNext()) {
          long start1= System.currentTimeMillis();
          SmsInfo.ListBean smsInfo=new SmsInfo.ListBean();
          int id=cursor.getInt(cursor.getColumnIndex(Telephony.Sms._ID));
          String phoneNumber = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));  //手机号 发件人地址，即手机号，如+8613811810000
       //   String name = cursor.getString(cursor.getColumnIndex(Telephony.Sms.PERSON));  //联系人
          String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));  //短信内容
          //格式化短信日期
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date time = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
            String date = simpleDateFormat.format(time);*/
          String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));
        //  DebugLog.i("wang","==time=="+DateUtil.TimeStamp2Date(String.valueOf(date),DateUtil.DATE_FORMAT,context.getResources().getConfiguration().locale));
          String type = cursor.getString(cursor.getColumnIndex(Telephony.Sms.TYPE));//type 1接受，2 发送
          smsInfo.setId(id);
          smsInfo.setSmsPartyNumber(phoneNumber);
      //    smsInfo.setPerson(name);
          smsInfo.setSmsContent(CharFilter.filterCharToNormal(body));
          smsInfo.setSmsTime(date);
          smsInfo.setTimeKey(Long.parseLong(date));
          smsInfo.setSmsStatus(type);
          listSms.add(smsInfo);
          DebugLog.i("wang","==sms_item_time=="+(System.currentTimeMillis()-start1));
            DebugLog.i("wang", "==phoneNum==" + phoneNumber  + "==body===" + CharFilter.filterCharToNormal(body) + "==date==" + date + "==type==" + type);
          //保存短信信息
          //   list.add(new MesInfo(name, phoneNumber, date, body));
      }
      DebugLog.i("wang","==sms_first_time=="+(System.currentTimeMillis()-start));
      return listSms;
  }
  
   /**
     * 读取短信条数
     *
     * @param context
     * @return
     */
    public static int readSmsSize(Context context) {
        List<SmsInfo.ListBean> listSms=new ArrayList<SmsInfo.ListBean>();
        String[] field = {Telephony.Sms._ID,Telephony.Sms.ADDRESS, Telephony.Sms.PERSON,Telephony.Sms.BODY, Telephony.Sms.DATE,Telephony.Sms.TYPE};  //字段
        ContentResolver contentResolver = context.getContentResolver();  //内容解析器(数据共享)——读取短信内容

        Date date0=new Date();
        int year=date0.getYear();
        int month=date0.getMonth()-5;
        int day=date0.getDate();
        Date date1=new Date(year,month,day);
        long start= System.currentTimeMillis();
    /*  String formateDate=DateUtil.formatDate(date1,DateUtil.DATE_FORMAT);
      DebugLog.i("wang","==format=="+formateDate);
      String time=DateUtil.Date2TimeStamp(formateDate,DateUtil.DATE_FORMAT);*/
        //   DebugLog.i("wang","==start=="+DateUtil.TimeStamp2Date(String.valueOf(time),DateUtil.DATE_FORMAT,context.getResources().getConfiguration().locale));limit 600
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/"),field,/* Telephony.Sms.DATE+">?"*/null,/*new String[]{time}*/null, "date desc ");  //获取短信内容

        if (cursor == null) {  //游标无内容
            return 0;
        }

        //  list = new ArrayList<MesInfo>();
        while (cursor.moveToNext()) {
            long start1= System.currentTimeMillis();
            SmsInfo.ListBean smsInfo=new SmsInfo.ListBean();
            int id=cursor.getInt(cursor.getColumnIndex(Telephony.Sms._ID));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));  //手机号 发件人地址，即手机号，如+8613811810000
            //   String name = cursor.getString(cursor.getColumnIndex(Telephony.Sms.PERSON));  //联系人
            String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));  //短信内容
            //格式化短信日期
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date time = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
            String date = simpleDateFormat.format(time);*/
            String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));
            //  DebugLog.i("wang","==time=="+DateUtil.TimeStamp2Date(String.valueOf(date),DateUtil.DATE_FORMAT,context.getResources().getConfiguration().locale));
            String type = cursor.getString(cursor.getColumnIndex(Telephony.Sms.TYPE));//type 1接受，2 发送
            smsInfo.setId(id);
            smsInfo.setSmsPartyNumber(phoneNumber);
            //    smsInfo.setPerson(name);
            smsInfo.setSmsContent(CharFilter.filterCharToNormal(body));
            smsInfo.setSmsTime(date);
            smsInfo.setTimeKey(Long.parseLong(date));
            smsInfo.setSmsStatus(type);
            listSms.add(smsInfo);
            DebugLog.i("wang","==sms_item_num_time=="+(System.currentTimeMillis()-start1));
            DebugLog.i("wang", "==phoneNum==" + phoneNumber  + "==body===" + CharFilter.filterCharToNormal(body) + "==date==" + date + "==type==" + type);
            //保存短信信息
            //   list.add(new MesInfo(name, phoneNumber, date, body));
        }
        DebugLog.i("wang","==sms_num_time=="+(System.currentTimeMillis()-start));
        return listSms.size();
    }

}
