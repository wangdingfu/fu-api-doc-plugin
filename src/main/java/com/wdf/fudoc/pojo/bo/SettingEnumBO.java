package com.wdf.fudoc.pojo.bo;

import lombok.Getter;
import lombok.Setter;


/**
 * 枚举相关配置
 * @author wangdingfu
 * @date 2022-08-15 20:41:05
 */
@Getter
@Setter
public class SettingEnumBO {

    /**
     * 配置枚举code的属性名
     */
    private String code = "code,index,intValue";


    /**
     * 配置枚举的msg属性名
     */
    private String msg = "msg,message,name,desc,view";


}
