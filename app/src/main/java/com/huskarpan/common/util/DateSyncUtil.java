package com.huskarpan.common.util;

import android.text.TextUtils;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Huskar on 2015/9/30.
 *
 * @description 同步的日期工具
 */
public class DateSyncUtil {

    private static String formatString = "";
    private static SimpleDateFormat sdf;
    private static Object lock = new Object();

    /**
     * 格式化日期时间
     * @param date 日期时间
     * @param formatString 日期格式化字符串
     * @return 日期时间字符串
     * @throws ParseException
     */
    public static String formatDate(Date date, String formatString) throws ParseException {
        synchronized (lock) {
            initSdf(formatString);
            return sdf.format(date);
        }

    }

    /**'
     * 解析日期时间
     * @param strDate 日期时间字符串
     * @param formatString 日期格式化字符串
     * @return 日期时间
     * @throws ParseException
     */
    public static Date parse(String strDate, String formatString) throws ParseException {
        synchronized (lock) {
            initSdf(formatString);
            return sdf.parse(strDate);
        }
    }


    /**
     * 根据日期格式化字符串初始化SimpleDateFormat
     * @param formatString 日期时间格式化字符串
     */
    private static void initSdf(String formatString) {
        boolean sameFormatString= TextUtils.equals(DateSyncUtil.formatString, formatString);
        if (!sameFormatString) {
            DateSyncUtil.formatString = formatString;
        }
        if (!sameFormatString || sdf == null) {
            sdf = new SimpleDateFormat(DateSyncUtil.formatString, Locale.CHINESE);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        }

    }
}
