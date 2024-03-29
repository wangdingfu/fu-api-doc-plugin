package com.wdf.fudoc.apidoc.pojo.desc;

import cn.hutool.json.JSONObject;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.common.constant.FuDocConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 对象信息描述
 * @date 2022-05-08 22:22:39
 */
@Getter
@Setter
public class ObjectInfoDesc extends BaseInfoDesc {

    /**
     * 根节点ID
     */
    private Integer rootId;

    /**
     * 当前节点ID
     */
    private Integer descId;

    /**
     * 对象类型枚举
     */
    private FuDocObjectType fuDocObjectType;
    /**
     * 字段类型
     */
    private String type;

    /**
     * 显示在页面的字段类型
     */
    private String typeView;

    /**
     * 子对象类型
     */
    private String childTypeView;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段注释
     */
    private String docText;

    /**
     * 针对该类型mock的值
     */
    private Object value;

    /**
     * 子属性字段集合(当前对象不为基本对象 且有自己属性字段时)
     */
    private List<ObjectInfoDesc> childList;

    /**
     * 扩展属性
     */
    private JSONObject extInfo;


    @Override
    public String getParamType() {
        return getValue(FuDocConstants.ExtInfo.PARAM_TYPE, String.class);
    }

    public void addExtInfo(String name, Object value) {
        if (Objects.isNull(this.extInfo)) {
            this.extInfo = new JSONObject();
        }
        this.extInfo.putOpt(name, value);
    }


    public <T> T getValue(String name, Class<T> clazz) {
        if (Objects.isNull(this.extInfo)) {
            return null;
        }
        return this.extInfo.get(name, clazz);
    }

    public boolean getBooleanValue(String name) {
        Boolean value = getValue(name, boolean.class);
        return !Objects.isNull(value) && value;
    }

}
