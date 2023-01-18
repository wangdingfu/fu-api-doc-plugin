package com.wdf.fudoc.apidoc.pojo.bo;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.ValidMessageType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * valid注解相关配置
 *
 * @author wangdingfu
 * @date 2022-08-15 20:34:13
 */
@Getter
@Setter
public class SettingValidBO {

    /**
     * 读取message内容的配置
     */
    private List<SettingValidMessageBO> message =
            Lists.newArrayList(new SettingValidMessageBO(ValidMessageType.REPLACE.getMsg(), ValidMessageType.REPLACE.getDefaultValue()));
}
