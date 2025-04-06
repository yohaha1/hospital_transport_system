package com.example.demo.model;

import java.util.Date;
import lombok.Data;

/**
 * TASK_NODE
 * @TableName task_node
 */
@Data
public class TaskNode {
    /**
     * 
     */
    private Integer taskid;

    /**
     * 
     */
    private Integer departmentid;

    /**
     * 
     */
    private Integer sequence;

    /**
     * 
     */
    private Date handovertime;

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public Integer getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(Integer departmentid) {
        this.departmentid = departmentid;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Date getHandovertime() {
        return handovertime;
    }

    public void setHandovertime(Date handovertime) {
        this.handovertime = handovertime;
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
        TaskNode other = (TaskNode) that;
        return (this.getTaskid() == null ? other.getTaskid() == null : this.getTaskid().equals(other.getTaskid()))
            && (this.getDepartmentid() == null ? other.getDepartmentid() == null : this.getDepartmentid().equals(other.getDepartmentid()))
            && (this.getSequence() == null ? other.getSequence() == null : this.getSequence().equals(other.getSequence()))
            && (this.getHandovertime() == null ? other.getHandovertime() == null : this.getHandovertime().equals(other.getHandovertime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTaskid() == null) ? 0 : getTaskid().hashCode());
        result = prime * result + ((getDepartmentid() == null) ? 0 : getDepartmentid().hashCode());
        result = prime * result + ((getSequence() == null) ? 0 : getSequence().hashCode());
        result = prime * result + ((getHandovertime() == null) ? 0 : getHandovertime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", taskid=").append(taskid);
        sb.append(", departmentid=").append(departmentid);
        sb.append(", sequence=").append(sequence);
        sb.append(", handovertime=").append(handovertime);
        sb.append("]");
        return sb.toString();
    }
}