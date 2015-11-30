package com.huskarpan.timecardpunching.bll;

import com.huskarpan.timecardpunching.entities.PunchingInfoDetail;
import com.huskarpan.timecardpunching.entities.PunchingInfoRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huskar on 2015/9/28.
 *
 * @description 一天的打卡信息
 */
public class OneDayPunchingInfo {
    private PunchingInfoRecord record;

    private List<PunchingInfoDetail> details;

    public PunchingInfoRecord getRecord() {
        return record;
    }

    public void setRecord(PunchingInfoRecord record) {
        this.record = record;
    }

    public List<PunchingInfoDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PunchingInfoDetail> details) {
        this.details = details;
    }


}
