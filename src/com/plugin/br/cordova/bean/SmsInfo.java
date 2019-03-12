package com.plugin.br.cordova.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class SmsInfo {

    /**
     * list : [{"createTime":"2018-08-20T09:03:08.667Z","deleteStatus":true,"id":0,"modifyTime":"2018-08-20T09:03:08.667Z","phonePid":"string","smsContent":"string","smsPartyNumber":"string","smsStatus":"string","smsTime":"string","userId":0}]
     * phonePid : string
     * userId : 0
     */

    private String phonePid;
    private int userId;
    private List<ListBean> list;

    public String getPhonePid() {
        return phonePid;
    }

    public void setPhonePid(String phonePid) {
        this.phonePid = phonePid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * createTime : 2018-08-20T09:03:08.667Z
         * deleteStatus : true
         * id : 0
         * modifyTime : 2018-08-20T09:03:08.667Z
         * phonePid : string
         * smsContent : string
         * smsPartyNumber : string
         * smsStatus : string
         * smsTime : string
         * userId : 0
         */

        private String createTime;
        private boolean deleteStatus;
        private int id;
        private String modifyTime;
        private String phonePid;
        private String smsContent;
        private String smsPartyNumber;
        private String smsStatus;
        private String smsTime;
        private long timeKey;
        private int userId;

        public String getCreateTime() {
            return createTime;
        }

        public long getTimeKey() {
            return timeKey;
        }

        public void setTimeKey(long timeKey) {
            this.timeKey = timeKey;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public boolean isDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(boolean deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getPhonePid() {
            return phonePid;
        }

        public void setPhonePid(String phonePid) {
            this.phonePid = phonePid;
        }

        public String getSmsContent() {
            return smsContent;
        }

        public void setSmsContent(String smsContent) {
            this.smsContent = smsContent;
        }

        public String getSmsPartyNumber() {
            return smsPartyNumber;
        }

        public void setSmsPartyNumber(String smsPartyNumber) {
            this.smsPartyNumber = smsPartyNumber;
        }

        public String getSmsStatus() {
            return smsStatus;
        }

        public void setSmsStatus(String smsStatus) {
            this.smsStatus = smsStatus;
        }

        public String getSmsTime() {
            return smsTime;
        }

        public void setSmsTime(String smsTime) {
            this.smsTime = smsTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "createTime='" + createTime + '\'' +
                    ", deleteStatus=" + deleteStatus +
                    ", id=" + id +
                    ", modifyTime='" + modifyTime + '\'' +
                    ", phonePid='" + phonePid + '\'' +
                    ", smsContent='" + smsContent + '\'' +
                    ", smsPartyNumber='" + smsPartyNumber + '\'' +
                    ", smsStatus='" + smsStatus + '\'' +
                    ", smsTime='" + smsTime + '\'' +
                    ", timeKey=" + timeKey +
                    ", userId=" + userId +
                    '}';
        }
    }
}
