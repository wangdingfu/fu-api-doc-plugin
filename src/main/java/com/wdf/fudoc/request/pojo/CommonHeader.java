package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.components.bo.KeyValueTableBO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 公共请求头对象
 *
 * @author wangdingfu
 * @date 2022-12-26 21:30:23
 */
@Getter
@Setter
public class CommonHeader extends KeyValueTableBO {

    /**
     * 作用范围 所有项目 当前项目 指定module
     */
    private String scope;


    /**
     * 如果是当前项目 则为当前项目id 其他为空字符串
     */
    private List<String> projectIdList;

    /**
     * 判断当前请求头的值是否为变量(用{{}}括住为变量)
     *
     * @return true 当前请求头值为变量
     */
    public boolean isVariable() {
        String value = getValue();
        return StringUtils.isNotBlank(value) && value.startsWith("{{") && value.endsWith("}}");
    }

}
