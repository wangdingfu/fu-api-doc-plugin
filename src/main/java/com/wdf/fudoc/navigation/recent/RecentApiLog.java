package com.wdf.fudoc.navigation.recent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.wdf.fudoc.util.FuStringUtils;

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
    private long time;


    @Override
    public String toString() {
        return this.url + "|" + this.time;
    }

    public RecentApiLog(String content) {
        if (FuStringUtils.isBlank(content)) {
            return;
        }
        String[] split = FuStringUtils.split(content, "|");
        if (split.length == 1) {
            this.url = split[0];
            this.time = 0;
        }
        if (split.length == 2) {
            this.url = split[0];
            String timeStr = split[1];
            this.time = (FuStringUtils.isNotBlank(timeStr) && FuStringUtils.isNumeric(timeStr)) ? Long.parseLong(timeStr) : 0;
        }
    }
}
