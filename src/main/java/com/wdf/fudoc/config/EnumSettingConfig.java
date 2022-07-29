package com.wdf.fudoc.config;

import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.CommonObjectType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 枚举参数设置类
 *
 * @author wangdingfu
 * @date 2022-07-29 17:13:02
 */
@Getter
@Setter
public class EnumSettingConfig {

    /**
     * 枚举code值名称候选集合
     */
    private final List<String> codeNameList = Lists.newArrayList(
            FuDocConstants.Enum.CODE,
            FuDocConstants.Enum.CODE_INDEX);

    private final List<String> codeTypeList = Lists.newArrayList(
            CommonObjectType.PRIMITIVE_INT.getObjPkg(),
            CommonObjectType.INTEGER.getObjPkg(),
            CommonObjectType.PRIMITIVE_SHORT.getObjPkg(),
            CommonObjectType.PRIMITIVE_LONG.getObjPkg(),
            CommonObjectType.STRING.getObjPkg()
    );

    /**
     * 枚举中文说明名称候选集合
     */
    private final List<String> valueNameList = Lists.newArrayList(
            FuDocConstants.Enum.MSG,
            FuDocConstants.Enum.MSG_VIEW,
            FuDocConstants.Enum.MSG_MESSAGE,
            FuDocConstants.Enum.MSG_DESC);


    private final List<String> valueTypeList = Lists.newArrayList(
            CommonObjectType.STRING.getObjPkg()
    );

}
