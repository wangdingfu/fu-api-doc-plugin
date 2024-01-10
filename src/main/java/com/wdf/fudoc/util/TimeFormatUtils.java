package com.wdf.fudoc.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-26 18:53:38
 */
public class TimeFormatUtils {

    private static final Long ONE_HOUR = 3600L;
    private static final Long THREE_HOUR = 10800L;
    private static final Long ONE_DAY = ONE_HOUR * 24;
    private static final Long ONE_WEEK = ONE_HOUR * 24 * 7;
    private static final Long ONE_MONTH = ONE_DAY * 31;
    private static final Long ONE_YEAR = ONE_MONTH * 12;


    public static String format(Long time) {
        if (Objects.isNull(time) || time == 0) {
            return FuStringUtils.EMPTY;
        }
        long currentSeconds = DateUtil.currentSeconds();
        if (time > currentSeconds) {
            return FuStringUtils.EMPTY;
        }

        long diffTime = currentSeconds - time;
        if (diffTime < 60) {
            return diffTime + " seconds ago";
        } else if (diffTime < ONE_HOUR) {
            Long minutes = diffTime / 60;
            return minutes + " minutes ago";
        } else if (diffTime < THREE_HOUR) {
            Long hour = diffTime / ONE_HOUR;
            return hour + " hours ago";
        } else if (diffTime < ONE_DAY) {
            DateTime endOfDay = DateUtil.endOfDay(new Date(time * 1000));
            long endDay = endOfDay.toTimestamp().getTime() / 1000;
            String day = currentSeconds < endDay ? "today" : "yesterday";
            DateTime dateTime = DateTime.of(time * 1000);
            int hour = dateTime.hour(true);
            int minute = dateTime.minute();
            String hourStr = hour < 10 ? "0" + hour : String.valueOf(hour);
            String minuteStr = minute < 10 ? "0" + minute : String.valueOf(minute);
            return day + " " + hourStr + ":" + minuteStr;
        } else if (diffTime < ONE_WEEK) {
            Long day = diffTime / ONE_DAY;
            return day + " days ago";
        } else if (diffTime < ONE_MONTH) {
            Long week = diffTime / ONE_WEEK;
            return week + " weeks ago";
        } else if (diffTime < ONE_YEAR) {
            Long month = diffTime / ONE_MONTH;
            return month + " months ago";
        }
        Long year = diffTime / ONE_YEAR;
        return year + " years ago";
    }
}
