package com.huskarpan.timecardpunching.entities;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.huskarpan.timecardpunching.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Huskar on 2015/9/20.
 *
 * @description 打卡记录
 */
@Table(name="punchingInfoRecord")
public class PunchingInfoRecord extends Model {

    /**
     * 打卡日期
     */
    @Column(name = "date")
    public long date;

    /**
     * 当日上班总时间
     */
    @Column(name = "totalHours")
    public double totalHours;

    public long getDate() {
        return date;
    }

    public String getDateFormat(Context context){
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.punching_record_date_format));
        return format.format(new Date(date));
    }
    public void setDate(long date) {
        this.date = date;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public PunchingInfoRecord() {
        super();
    }

    public PunchingInfoRecord(long date, double totalHours) {
        super();
        this.date = date;
        this.totalHours = totalHours;
    }
}
