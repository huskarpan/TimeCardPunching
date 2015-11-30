package com.huskarpan.timecardpunching.entities;


import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.huskarpan.timecardpunching.R;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Huskar on 2015/9/22.
 *
 * @description 打卡记录明细
 */
@Table(name="punchingInfoDetail")
public class PunchingInfoDetail extends Model{

    public static final int RECORD_TYPE_ON_DUTY=1;
    public static final int RECORD_TYPE_OFF_DUTY=2;

    @Column(name = "time")
    public long time;

    @Column(name = "recordType")
    public int recordType;

    @Column(name = "recordId")
    public long recordId;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getTime() {
        return time;
    }

    public String getFormatTime(Context context){
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.punching_detail_time_format));
        return format.format(new Date(time));
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public PunchingInfoDetail() {
        super();
    }

    public PunchingInfoDetail(long recordId,long time, int recordType) {
        super();
        this.recordId = recordId;
        this.time = time;
        this.recordType = recordType;
    }

    @Override
    public String toString() {
        return "Record{" +
                "time=" + time +
                ", recordType=" + recordType +
                '}';
    }
}
