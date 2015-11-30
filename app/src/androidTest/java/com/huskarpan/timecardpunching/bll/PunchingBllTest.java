package com.huskarpan.timecardpunching.bll;

import android.os.SystemClock;
import android.test.AndroidTestCase;

import com.huskarpan.common.util.DateSyncUtil;
import com.huskarpan.timecardpunching.entities.PunchingInfoDetail;
import com.lidroid.xutils.util.LogUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Huskar on 2015/9/26.
 *
 * @description
 */
public class PunchingBllTest extends AndroidTestCase {
    public void testWhat() throws ParseException {

    }

    /**
     * 工作时间计算测试
     */
    public void testTimeCalculate() {
        long currentDate = convertToDateLong(new Date());
        ArrayList<PunchingInfoDetail> details = new ArrayList<>();
        try {

            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,9,0,0),PunchingInfoDetail.RECORD_TYPE_ON_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,18,0,0),PunchingInfoDetail.RECORD_TYPE_OFF_DUTY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(8d, WorkTotalTimeCalculator.Calculate(currentDate, details));

        details.clear();
        try {
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,9,30,0),PunchingInfoDetail.RECORD_TYPE_ON_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,12,30,0),PunchingInfoDetail.RECORD_TYPE_OFF_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,2,30,0),PunchingInfoDetail.RECORD_TYPE_ON_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,6,30,0),PunchingInfoDetail.RECORD_TYPE_OFF_DUTY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(6.5d, WorkTotalTimeCalculator.Calculate(currentDate, details));

        details.clear();
        try {
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,9,30,0),PunchingInfoDetail.RECORD_TYPE_ON_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,11,30,0),PunchingInfoDetail.RECORD_TYPE_OFF_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,12,10,0),PunchingInfoDetail.RECORD_TYPE_ON_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,12,50,0),PunchingInfoDetail.RECORD_TYPE_OFF_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,12,55,0),PunchingInfoDetail.RECORD_TYPE_ON_DUTY));
            details.add(new PunchingInfoDetail(1,ConvertToTime(currentDate,18,00,0),PunchingInfoDetail.RECORD_TYPE_OFF_DUTY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(7d, WorkTotalTimeCalculator.Calculate(currentDate, details));
    }

    /**
     * 把时间转换成Long类型日期值
     * @return Long类型日期值
     */
    private long convertToDateLong(Date date) {
        Calendar currentCalendar =   Calendar.getInstance();
        currentCalendar.setTime(date);
        Calendar value = Calendar.getInstance();
        value.clear();
        value.set(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DATE));
        return value.getTimeInMillis();
    }


    private long ConvertToTime(long date, int hour, int minute, int second) throws Exception {
        if (hour < 0 || minute < 0 || second < 0) {
            throw new Exception("时分秒不能为负数");
        }
        return date +(( hour * 60 + minute) * 60 + second) * 1000;
    }
}
