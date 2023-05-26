package com.wdf.fudoc.navigation.recent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @date 2023-05-26 18:02:24
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentApiLog {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 记录时间
     */
    private Long time;


    @Override
    public String toString() {
        return this.url + "|" + this.time;
    }

    public RecentApiLog(String content) {
        if (StringUtils.isBlank(content)) {
            return;
        }
        String[] split = StringUtils.split(content, "|");
        if (split.length == 1) {
            this.url = split[0];
        }
        if (split.length == 2) {
            this.url = split[0];
            String timeStr = split[1];
            this.time = (StringUtils.isNotBlank(timeStr) && StringUtils.isNumeric(timeStr)) ? Long.parseLong(timeStr) : null;
        }
    }
}
