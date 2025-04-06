package com.example.demo.model;

import java.util.Date;
import lombok.Data;

/**
 * NOTIFICATION
 * @TableName notification
 */
@Data
public class Notification {

    private Integer notificationid;

    private Integer taskid;

    private Integer recipientid;

    private String notificationtype;

    private String message;

    private Date sendtime;

    public Integer getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(Integer notificationid) {
        this.notificationid = notificationid;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public Integer getRecipientid() {
        return recipientid;
    }

    public void setRecipientid(Integer recipientid) {
        this.recipientid = recipientid;
    }

    public String getNotificationtype() {
        return notificationtype;
    }

    public void setNotificationtype(String notificationtype) {
        this.notificationtype = notificationtype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
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
        Notification other = (Notification) that;
        return (this.getNotificationid() == null ? other.getNotificationid() == null : this.getNotificationid().equals(other.getNotificationid()))
            && (this.getTaskid() == null ? other.getTaskid() == null : this.getTaskid().equals(other.getTaskid()))
            && (this.getRecipientid() == null ? other.getRecipientid() == null : this.getRecipientid().equals(other.getRecipientid()))
            && (this.getNotificationtype() == null ? other.getNotificationtype() == null : this.getNotificationtype().equals(other.getNotificationtype()))
            && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()))
            && (this.getSendtime() == null ? other.getSendtime() == null : this.getSendtime().equals(other.getSendtime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNotificationid() == null) ? 0 : getNotificationid().hashCode());
        result = prime * result + ((getTaskid() == null) ? 0 : getTaskid().hashCode());
        result = prime * result + ((getRecipientid() == null) ? 0 : getRecipientid().hashCode());
        result = prime * result + ((getNotificationtype() == null) ? 0 : getNotificationtype().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getSendtime() == null) ? 0 : getSendtime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", notificationid=").append(notificationid);
        sb.append(", taskid=").append(taskid);
        sb.append(", recipientid=").append(recipientid);
        sb.append(", notificationtype=").append(notificationtype);
        sb.append(", message=").append(message);
        sb.append(", sendtime=").append(sendtime);
        sb.append("]");
        return sb.toString();
    }
}