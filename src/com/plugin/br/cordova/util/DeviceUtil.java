package com.plugin.br.cordova.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;


public class DeviceUtil {
    /**
     * 设备名称 设备型号
     * @return
     */
    public static String getDeviceName() {
//		String devName = android.os.Build.MANUFACTURER +"-"+ android.os.Build.PRODUCT;
        String devName = Build.MODEL;
        devName = devName.trim();
        Log.i("wang","===dev_name==="+devName);
        return devName;
    }

    public static String getUniqueID(Context sContext) {
        TelephonyManager telephonyManager = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        String m_IMEI = null; //IMEI码
        if (telephonyManager.getDeviceId() != null) {
            m_IMEI = telephonyManager.getDeviceId();
        } else {
            m_IMEI = Settings.Secure.getString(sContext.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        //这个同一个厂商同样设备同样的rom下会重复
        String m_DEVID = "35" + //we make this look like a valid IMEI
                Build.BOARD + Build.BRAND
                + Build.CPU_ABI+ Build.DEVICE
                + Build.DISPLAY+ Build.HOST
                + Build.ID + Build.MANUFACTURER
                + Build.MODEL+ Build.PRODUCT
                + Build.TAGS+ Build.TYPE
                + Build.USER; // 13 digits

        WifiManager wm = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
        String m_WLANMAC = wm.getConnectionInfo().getMacAddress();
        DebugLog.i("wang","====uniqueID=="+m_IMEI + m_DEVID + m_WLANMAC);
        String uniqueID = MD5Util.md5(m_IMEI + m_DEVID + m_WLANMAC);
        DebugLog.i("wang","====uniqueID==MD5="+uniqueID);
        return uniqueID;
    }
    public static String getUUID(Context context){

        final TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;

        tmDevice = "" + tm.getDeviceId();

        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());

        String uniqueId = deviceUuid.toString();

        Log.d("debug","uuid="+uniqueId);

        return uniqueId;

    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context sContext) {
        TelephonyManager telephonyManager = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId(); //IMEI码
        return IMEI;
    }

    /**
     * 获取手机IMSI号
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();

        return imsi;
    }

    /** iccd
     *
     * @param context
     * @return
     */
    public static String getICCID(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iccid = mTelephonyMgr.getSimSerialNumber();
        Log.i("wang","==iccid==="+iccid);
        return iccid;
    }

    /**
     * 获取电话号码
     * @param context
     * @return
     */
    public static String  getTelPhone(Context context){
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNum= mTelephonyMgr.getLine1Number();
        Log.i("wang","==phoneNum=="+phoneNum);
        return phoneNum;
    }

    /**
     *
     * @return
     */
    public static String getMEID(){
        Class<?> clazz = null;
        Method method = null;//(int slotId)
        try {
            clazz = Class.forName("android.os.SystemProperties");
            method = clazz.getMethod("get", String.class, String.class);
            String meid = (String) method.invoke(null, "ril.cdma.meid", "");
            Log.i("wang","=meid==="+meid);
            return meid;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean isScreenChange(Context ctx) {
        Configuration mConfiguration = ctx.getResources().getConfiguration(); // 获取设置的配置信息
        int ori = mConfiguration.orientation; // 获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            return true;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            return false;
        }
        return false;
    }

    /**
     * 获取收集主板信息
     * @return
     */
    public static String getAndroidBorder(){
        return  Build.BOARD;
    }

    /**
     * 获取CPU型号
     * @return
     */
    public static String getAndroidCpuApi(){
        return  Build.CPU_ABI;
    }

    /**
     * 开机时间
     * @return
     */
    public static long getOpenTime(){
        return SystemClock.elapsedRealtime();
    }

    /**
     * 获取手机尺寸
     * @param context
     */
    public static double getSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 屏幕宽度
        float screenWidth = display.getWidth();
        // 屏幕高度
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        Log.i("wang","==size="+screenInches);
        return screenInches;
    }

    /**
     * 获取手机电量
     * @param context
     * @return
     */
    public static int getBatteryLevel(Context context){
        BatteryManager batteryManager= (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        int battery= batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.i("wang","=b=="+battery);
        return battery;
    }


    public static boolean screenLocked(Context ctx) {
        KeyguardManager mKeyguardManager = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
        boolean status = mKeyguardManager.inKeyguardRestrictedInputMode();
        return status;
    }
/*
    public List<TelephonyManager> getTelephonyManager(){
        String simId_1,simId_2;
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> c = null;
       *//* try {
            c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            simId_1 = (Integer) fields1.get(null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            simId_2 = (Integer) fields2.get(null);

            Method mx = TelephonyManager.class.getMethod("getDefault",
                    int.class);
            TelephonyManager tm1 = (TelephonyManager) mx.invoke(tm, simId_1);
            TelephonyManager tm2 = (TelephonyManager) mx.invoke(tm, simId_2);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
*//*

        return null;
    }*/
    //bm.getPixels(x,y);
   public static class Wifi{
       //   无线路由器的BSSID
        public static String getBSSID(Context context){
            WifiManager wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo=wifiManager.getConnectionInfo();
            if(wifiInfo!=null){
               /* String ssid = wifiInfo.getSSID();
                int networkID = wifiInfo.getNetworkId();
                int speed = wifiInfo.getLinkSpeed();
                String bssid = wifiInfo.getBSSID();*/
               return wifiInfo.getBSSID();
            }
            return "";
        }
        //   无线路由器的MAC地址
        public static String getWifiMac(Context context){
            WifiManager wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo=wifiManager.getConnectionInfo();
            if(wifiInfo!=null){
               /* String ssid = wifiInfo.getSSID();
                int networkID = wifiInfo.getNetworkId();
                int speed = wifiInfo.getLinkSpeed();
                String bssid = wifiInfo.getBSSID();*/
                return wifiInfo.getMacAddress();
            }
            return "";
        }

        public static String getWifiMac1(){
         /*   new Thread(new Runnable() {
                @Override
                public void run() {*/
                    Enumeration<NetworkInterface> interfaces = null;
                    try {
                        interfaces = NetworkInterface.getNetworkInterfaces();
                        while (interfaces.hasMoreElements()) {
                            NetworkInterface iF = interfaces.nextElement();

                            byte[] addr = iF.getHardwareAddress();
                            if (addr == null || addr.length == 0) {
                                continue;
                            }

                            StringBuilder buf = new StringBuilder();
                            for (byte b : addr) {
                                buf.append(String.format("%02X:", b));
                            }
                            if (buf.length() > 0) {
                                buf.deleteCharAt(buf.length() - 1);
                            }
                            String mac = buf.toString();
                            Log.d("---mac", "interfaceName="+iF.getName()+", mac="+mac);
                            if("wlan0".equals(iF.getName())){
                              //  return mac;
                             //   AppStaus.wifiMac=mac;
                                return mac;
                            }
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }

            /*    }
            }).start();*/
            return "";
        }

    }

    public static String getChannel(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo.metaData.getString("CHANNEL");
    }

}
