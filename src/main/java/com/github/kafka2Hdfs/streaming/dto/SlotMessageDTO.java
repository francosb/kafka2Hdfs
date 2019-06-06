package com.github.kafka2Hdfs.streaming.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import scala.Serializable;

import java.util.List;


public class SlotMessageDTO implements Serializable {

    @SerializedName("xid")
    @Expose
    private int xid;
    @SerializedName("change")
    @Expose
    private List<Change> change = null;
    @SerializedName("nextlsn")
    @Expose
    private String nextlsn;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    private final static long serialVersionUID = -8373242691019769813L;


    public int getXid() {
        return xid;
    }

    public void setXid(int xid) {
        this.xid = xid;
    }

    public List<Change> getChange() {

        return change;
    }

    public void setChange(List<Change> change) {
        this.change = change;
    }

    public String getNextlsn() {
        return nextlsn;
    }

    public void setNextlsn(String nextlsn) {
        this.nextlsn = nextlsn;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("xid", xid)
                .append("change", change)
                .append("nextlsn", nextlsn)
                .append("timestamp", timestamp)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(timestamp)
                .append(change)
                .append(xid)
                .append(nextlsn).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SlotMessageDTO)) {
            return false;
        }
        SlotMessageDTO rhs = ((SlotMessageDTO) other);
        return new EqualsBuilder()
                .append(timestamp, rhs.timestamp)
                .append(change, rhs.change)
                .append(xid, rhs.xid)
                .append(nextlsn, rhs.nextlsn).isEquals();
    }




}

