package com.wdf.fudoc.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.CommonObjectType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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
    private final Set<String> codeNameList = Sets.newLinkedHashSet(
            Lists.newArrayList(FuDocConstants.Enum.CODE,
                    FuDocConstants.Enum.CODE_INDEX));

    public static final Set<String> codeTypeList = Sets.newLinkedHashSet(
            Lists.newArrayList(CommonObjectType.PRIMITIVE_INT.getObjPkg(),
                    CommonObjectType.INTEGER.getObjPkg(),
                    CommonObjectType.PRIMITIVE_SHORT.getObjPkg(),
                    CommonObjectType.PRIMITIVE_LONG.getObjPkg(),
                    CommonObjectType.STRING.getObjPkg())
    );

    /**
     * 枚举中文说明名称候选集合
     */
    private final Set<String> valueNameList = Sets.newLinkedHashSet(
            Lists.newArrayList(FuDocConstants.Enum.MSG,
                    FuDocConstants.Enum.MSG_VIEW,
                    FuDocConstants.Enum.MSG_MESSAGE,
                    FuDocConstants.Enum.MSG_DESC));


    public static final Set<String> valueTypeList = Sets.newLinkedHashSet(
            Lists.newArrayList(CommonObjectType.STRING.getObjPkg())
    );

    public void addCode(List<String> codeNames){
        codeNameList.addAll(codeNames);
    }

    public void addMsg(List<String> msgList){
        valueNameList.addAll(msgList);
    }

}
