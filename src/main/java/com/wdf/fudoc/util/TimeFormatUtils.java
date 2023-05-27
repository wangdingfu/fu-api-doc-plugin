package com.wdf.fudoc.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-26 18:53:38
 */
public class TimeFormatUtils {

    /**
     * 标准时间格式：HH:mm
     */
    public static final String NORM_TIME_PATTERN = "HH:mm";
    /**
     * 标准时间格式 {@link FastDateFormat}：HH:mm
     */
    public static final FastDateFormat NORM_TIME_FORMAT = FastDateFormat.getInstance(NORM_TIME_PATTERN);

    private static final Long ONE_HOUR = 3600L;
    private static final Long THREE_HOUR = 10800L;
    private static final Long ONE_DAY = ONE_HOUR * 24;
    private static final Long ONE_WEEK = ONE_HOUR * 24 * 7;
    private static final Long ONE_MONTH = ONE_DAY * 31;
    private static final Long ONE_YEAR = ONE_MONTH * 12;


    public static String format(Long time) {
        if (Objects.isNull(time) || time == 0) {
            return StringUtils.EMPTY;
        }
        long currentSeconds = DateUtil.currentSeconds();
        if (time > currentSeconds) {
            return StringUtils.EMPTY;
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
            //判断是否为今天
            int endDay = DateUtil.endOfDay(new Date()).second();
            String day = currentSeconds < endDay ? "today" : "yesterday";
            return day + " " + NORM_TIME_FORMAT.format(time);
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
