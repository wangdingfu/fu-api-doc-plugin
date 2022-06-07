package com.wdf.apidoc.factory;

import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @Descption 构建ObjectInfoDesc的工厂类
 * @Date 2022-06-07 15:52:42
 */
public class ObjectInfoDescFactory {


    public static ObjectInfoDesc buildReference() {
        ObjectInfoDesc objectInfoDesc = new ObjectInfoDesc();
        objectInfoDesc.setName("`reference`");
        objectInfoDesc.setTypeView("object");
        objectInfoDesc.setApiDocObjectType(ApiDocObjectType.DEFAULT_OBJECT);
        objectInfoDesc.addExtInfo(ApiDocConstants.ExtInfo.IS_ATTR, true);
        objectInfoDesc.setDocText("引用和父级同级的所有字段");
        return objectInfoDesc;
    }
}
