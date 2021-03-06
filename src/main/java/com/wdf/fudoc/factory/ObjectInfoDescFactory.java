package com.wdf.fudoc.factory;

import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;

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
        objectInfoDesc.setFuDocObjectType(FuDocObjectType.DEFAULT_OBJECT);
        objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.IS_ATTR, true);
        objectInfoDesc.setDocText("`引用和父级同级的所有字段`");
        return objectInfoDesc;
    }


    public static ObjectInfoDesc build(String name, String typeView, String comment) {
        ObjectInfoDesc objectInfoDesc = new ObjectInfoDesc();
        objectInfoDesc.setName(name);
        objectInfoDesc.setTypeView(typeView);
        objectInfoDesc.setFuDocObjectType(FuDocObjectType.DEFAULT_OBJECT);
        objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.IS_ATTR, true);
        objectInfoDesc.setDocText(comment);
        return objectInfoDesc;
    }
}
