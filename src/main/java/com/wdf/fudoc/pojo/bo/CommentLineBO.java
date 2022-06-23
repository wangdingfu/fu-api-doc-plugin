package com.wdf.fudoc.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @descption: 每一行注释
 * @date 2022-04-24 22:58:19
 */
@Getter
@Setter
public class CommentLineBO {

    /**
     * 行数(第几行注释)
     */
    private int row;

    /**
     * 注释tag
     */
    private String tag;

    /**
     * 注释tag的参数名
     */
    private String key;

    /**
     * 注释内容
     */
    private String content;

    /**
     * 注释前缀
     */
    private String prefix;

    /**
     * 注释后缀
     */
    private String suffix;

    @Override
    public String toString() {
        String tagText = StringUtils.isNotBlank(tag) ? "@" + tag : "";
        return this.prefix + tagText + this.content + this.suffix;
    }
}
