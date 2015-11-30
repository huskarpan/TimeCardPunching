package com.huskarpan.timecardpunching.bll;

import com.huskarpan.common.util.DateSyncUtil;
import com.huskarpan.timecardpunching.entities.PunchingInfoDetail;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huskar on 2015/10/12.
 *
 * @description 工作总时间计算器
 */
public class WorkTotalTimeCalculator {
    /**
     * 计算打卡时数
     * @param currentDate 当前日期
     * @param details 打卡明细集合
     */
    public static double Calculate(long currentDate, List<PunchingInfoDetail> details) {
        double totalMillis = 0d;
        List<TimeFragment> excludeTimeFragments = getExcludeTimeFragments(currentDate);
        List<TimeFragment> workFragments = getWorkTimeFragments(details);
        int excludeFragmentIndex = 0;
        int workFragmentIndex = 0;
        while (workFragmentIndex < workFragments.size()) {
            TimeFragment workFragment = workFragments.get(workFragmentIndex);
            if (excludeFragmentIndex == excludeTimeFragments.size()) {
                totalMillis += workFragment.getDuration();
                workFragmentIndex++;
                continue;
            }
            while (excludeFragmentIndex < excludeTimeFragments.size()) {
                TimeFragment excludeTimeFragment = excludeTimeFragments.get(excludeFragmentIndex);
                if (workFragment.endTime <= excludeTimeFragment.startTime) {
                    totalMillis += workFragment.getDuration();
                    workFragmentIndex++;
                    break;
                }
                if (workFragment.startTime < excludeTimeFragment.endTime) {
                    if (workFragment.startTime < excludeTimeFragment.startTime) {
                        totalMillis += excludeTimeFragment.startTime - workFragment.startTime;
                    }
                    if (workFragment.endTime > excludeTimeFragment.endTime) {
                        workFragment.startTime = excludeTimeFragment.endTime;
                        excludeFragmentIndex++;
                        continue;
                    } else {
                        workFragmentIndex++;
                        break;
                    }
                }
                if (workFragment.startTime >= excludeTimeFragment.endTime) {
                    excludeFragmentIndex++;
                    continue;
                }
            }
        }
        return totalMillis/3600000;
    }

    /**
     * 获取工作时间段集合
     * @param details 打卡明细
     * @return 工作时间段集合
     */
    private static List<TimeFragment> getWorkTimeFragments(List<PunchingInfoDetail> details) {
        ArrayList<TimeFragment> workFragments=new ArrayList<>();
        TimeFragment currentTimeFragment = null;
        for (PunchingInfoDetail detail : details) {
            if (detail.getRecordType() == PunchingInfoDetail.RECORD_TYPE_ON_DUTY) {
                currentTimeFragment = new TimeFragment();
                currentTimeFragment.setStartTime(detail.getTime());
                workFragments.add(currentTimeFragment);
            } else {
                currentTimeFragment.setEndTime(detail.getTime());
            }
        }
        //最后一条记录为上班时，添加当前时间为结束时间来计算
        if (details.size() > 0
                && details.get(details.size() - 1).getRecordType() == PunchingInfoDetail.RECORD_TYPE_ON_DUTY) {
            currentTimeFragment.setEndTime(new Date().getTime());
        }
        return workFragments;
    }

    /**
     * 获取待排除的时间段集合
     * @param currentDate 当前日期
     * @return
     */
    private static List<TimeFragment> getExcludeTimeFragments(long currentDate) {
        ArrayList<TimeFragment> result = new ArrayList<>();
        try {
            long fragment1StartTime = DateSyncUtil.parse("12:00:00", "HH:mm:ss").getTime()+currentDate;
            long fragement1EndTime =  DateSyncUtil.parse("13:00:00", "HH:mm:ss").getTime()+currentDate;
            result.add(new TimeFragment(fragment1StartTime, fragement1EndTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 时间段
     */
    private static class TimeFragment {
        private long startTime;
        private long endTime;

        /**
         * 初始化一个时间段
         */
        public TimeFragment() {
        }

        /**
         * 初始化一个时间段
         * @param startTime 开始时间
         * @param endTime 结束时间
         */
        public TimeFragment(long startTime, long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        /**
         * 获取时间间隔
         * @return 时间间隔
         */
        public long getDuration(){
            return endTime-startTime;
        }
    }
}
