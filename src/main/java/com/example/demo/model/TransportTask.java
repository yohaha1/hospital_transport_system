package com.example.demo.model;

import java.util.Date;
import lombok.Data;

/**
 * TRANSPORT_TASK
 * @TableName transport_task
 */
@Data
public class TransportTask {
    /**
     * 
     */
    private Integer taskid;

    /**
     * 
     */
    private String itemname;

    /**
     * 
     */
    private String itemtype;

    /**
     * 
     */
    private Integer priority;

    /**
     * 
     */
    private Object status;

    /**
     * 
     */
    private String note;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date completion;

    /**
     * 
     */
    private Integer docid;

    /**
     * 
     */
    private Integer transid;

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getCompletion() {
        return completion;
    }

    public void setCompletion(Date completion) {
        this.completion = completion;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }

    public Integer getTransid() {
        return transid;
    }

    public void setTransid(Integer transid) {
        this.transid = transid;
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
        TransportTask other = (TransportTask) that;
        return (this.getTaskid() == null ? other.getTaskid() == null : this.getTaskid().equals(other.getTaskid()))
            && (this.getItemname() == null ? other.getItemname() == null : this.getItemname().equals(other.getItemname()))
            && (this.getItemtype() == null ? other.getItemtype() == null : this.getItemtype().equals(other.getItemtype()))
            && (this.getPriority() == null ? other.getPriority() == null : this.getPriority().equals(other.getPriority()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getNote() == null ? other.getNote() == null : this.getNote().equals(other.getNote()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getCompletion() == null ? other.getCompletion() == null : this.getCompletion().equals(other.getCompletion()))
            && (this.getDocid() == null ? other.getDocid() == null : this.getDocid().equals(other.getDocid()))
            && (this.getTransid() == null ? other.getTransid() == null : this.getTransid().equals(other.getTransid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTaskid() == null) ? 0 : getTaskid().hashCode());
        result = prime * result + ((getItemname() == null) ? 0 : getItemname().hashCode());
        result = prime * result + ((getItemtype() == null) ? 0 : getItemtype().hashCode());
        result = prime * result + ((getPriority() == null) ? 0 : getPriority().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getNote() == null) ? 0 : getNote().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getCompletion() == null) ? 0 : getCompletion().hashCode());
        result = prime * result + ((getDocid() == null) ? 0 : getDocid().hashCode());
        result = prime * result + ((getTransid() == null) ? 0 : getTransid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", taskid=").append(taskid);
        sb.append(", itemname=").append(itemname);
        sb.append(", itemtype=").append(itemtype);
        sb.append(", priority=").append(priority);
        sb.append(", status=").append(status);
        sb.append(", note=").append(note);
        sb.append(", createtime=").append(createtime);
        sb.append(", completion=").append(completion);
        sb.append(", docid=").append(docid);
        sb.append(", transid=").append(transid);
        sb.append("]");
        return sb.toString();
    }
}