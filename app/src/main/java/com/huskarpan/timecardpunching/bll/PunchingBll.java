package com.huskarpan.timecardpunching.bll;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.huskarpan.common.util.DateSyncUtil;
import com.huskarpan.timecardpunching.entities.PunchingInfoDetail;
import com.huskarpan.timecardpunching.entities.PunchingInfoRecord;
import com.lidroid.xutils.util.LogUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Huskar on 2015/9/22.
 *
 * @description 打卡业务逻辑
 */
public class PunchingBll {
    public PunchingBll() {
    }

    /**
     * 获取今天的打卡信息
     * @return 今天的打卡信息
     */
    public OneDayPunchingInfo getTodayPunchingInfo() {
        return getOneDayPunchingInfo(new Date());
    }

    /**
     * 获取指定日期的打卡信息
     * @param date 打卡日期
     * @return 指定日期的打卡信息
     */
    public OneDayPunchingInfo getOneDayPunchingInfo(Date date) {
        PunchingInfoRecord record = getRecordByDate(date);
        if(record==null) {
            record= addPunchingInfoRecord(date);
        }
        OneDayPunchingInfo oneDayPunchingInfo = new OneDayPunchingInfo();
        oneDayPunchingInfo.setRecord(record);
        List<PunchingInfoDetail> details = getDetials(record.getId());
        oneDayPunchingInfo.setDetails(details);
        return oneDayPunchingInfo;
    }

    /**
     * 获取打卡明细集合
     * @param recordId 记录Id
     * @return 打卡明细集合
     */
    private List<PunchingInfoDetail> getDetials(long recordId) {
        return new Select().from(PunchingInfoDetail.class)
                .where("recordId = ?", recordId).orderBy("time ASC").execute();
    }

    /**
     * 通过日期获取打卡记录
     * @param date 日期
     * @return 打卡记录
     */
    private PunchingInfoRecord getRecordByDate(Date date) {
        return new Select().from(PunchingInfoRecord.class)
                .where("date = ?", convertToDateLong(date)).executeSingle();
    }

    /**
     * 添加打卡记录
     * @param date 日期
     * @return 打卡记录
     */
    private PunchingInfoRecord addPunchingInfoRecord(Date date){
        PunchingInfoRecord record = new PunchingInfoRecord(convertToDateLong(date), 0);
        record.save();
        return record;
    }

    /**
     * 判断当前是否处于上班状态
     * @param details 指定日期的打卡明细
     * @return 当前是否处于上班状态
     */
    public boolean isOnDuty(List<PunchingInfoDetail> details) {
        if(details== null || details.size()==0)
            return false;
        return details.get(details.size()-1).getRecordType()==PunchingInfoDetail.RECORD_TYPE_ON_DUTY;
    }

    /**
     * 添加打卡明细
     * @param recordId 记录Id
     * @param date 日期
     * @return 打卡明细
     * @throws Exception 添加打卡明细失败异常
     */
    public PunchingInfoDetail addPunchingInfoDetail(long recordId,Date date) throws Exception {
        ActiveAndroid.beginTransaction();

        PunchingInfoDetail detail = null;
        try {
            PunchingInfoRecord record = new Select().from(PunchingInfoRecord.class)
                    .where("Id = ?", recordId).executeSingle();
            if (record == null) {
                String msg = "添加打卡明细失败，找不到记录Id，RecordId：" + recordId;
                LogUtils.e(msg);
                throw new Exception(msg);
            }
            List<PunchingInfoDetail> details = getDetials(recordId);
            boolean onDuty = isOnDuty(details);
            int recordType=onDuty?PunchingInfoDetail.RECORD_TYPE_OFF_DUTY:PunchingInfoDetail.RECORD_TYPE_ON_DUTY;
            detail = new PunchingInfoDetail(recordId, date.getTime(), recordType);
            detail.save();
            if (onDuty) {
                details.add(detail);
                double totalHours=calculateTotalHours(record.getDate(),details);
                record.setTotalHours(totalHours);
                record.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtils.e("添加打卡明细失败，Exception: "+e.getMessage() );
        } finally {
            ActiveAndroid.endTransaction();
        }
        return detail;
    }

    /**
     * 计算当日上班总时数
     * @param currentDate 当前日期
     * @param details 打卡明细
     * @return 当日上班总时数
     */
    public double calculateTotalHours(long currentDate,List<PunchingInfoDetail> details) {
        return WorkTotalTimeCalculator.Calculate(currentDate, details);
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

}

