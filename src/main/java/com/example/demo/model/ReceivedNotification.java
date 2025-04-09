package com.example.demo.model;

import lombok.Data;

/**
 * RECEIVED_NOTIFICATION
 * @TableName received_notification
 */
@Data
public class ReceivedNotification {
    /**
     * 
     */
    private Integer notificationid;

    /**
     * 
     */
    private Integer userid;

    public Integer getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(Integer notificationid) {
        this.notificationid = notificationid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ReceivedNotification other = (ReceivedNotification) that;
        return (this.getNotificationid() == null ? other.getNotificationid() == null : this.getNotificationid().equals(other.getNotificationid()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNotificationid() == null) ? 0 : getNotificationid().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", notificationid=").append(notificationid);
        sb.append(", userid=").append(userid);
        sb.append("]");
        return sb.toString();
    }
}