package com.github.kafka2Hdfs.streaming.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Change implements Serializable {

    @SerializedName("schema")
    @Expose
    private String schema;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("columnvalues")
    @Expose
    private List<String> columnvalues = null;
    private List<String> columnnames = null;
    @SerializedName("table")
    @Expose
    private String table;
    private SimpleDateFormat format;

    private final static long serialVersionUID = -6303422363571000701L;

    public Change() {
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public String getYear() throws ParseException {
        Date date = format.parse(getColumnvalues().get(2));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public String getMonth() throws ParseException {
        Date date = format.parse(getColumnvalues().get(2));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.MONTH));
    }

    public String getDay() throws ParseException {
        Date date = format.parse(getColumnvalues().get(2));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<String> getColumnvalues() {
        return columnvalues;
    }

    public void setColumnvalues(List<String> columnvalues) {
        this.columnvalues = columnvalues;
    }

    public List<String> getColumnnames() {
        return columnnames;
    }

    public void setColumnnames(List<String> columnnames) {
        this.columnnames = columnnames;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("schema", schema)
                .append("kind", kind)
                .append("columnvalues", columnvalues)
                .append("table", table).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(columnvalues)
                .append(schema)
                .append(table)
                .append(columnnames)
                .append(kind).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Change)) {
            return false;
        }
        Change rhs = ((Change) other);
        return new EqualsBuilder()
                .append(columnvalues, rhs.columnvalues)
                .append(schema, rhs.schema)
                .append(table, rhs.table)
                .append(columnnames, rhs.columnnames)
                .append(kind, rhs.kind).isEquals();
    }

}
