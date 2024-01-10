package com.wdf.fudoc.apidoc.pojo.context;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.apidoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.apidoc.config.EnumSettingConfig;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.data.CustomerSettingData;
import com.wdf.fudoc.apidoc.data.SettingData;
import com.wdf.fudoc.apidoc.factory.ObjectInfoDescFactory;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.*;

/**
 * @author wangdingfu
 * @descption: 生成API接口文档全局上下文
 * @date 2022-04-05 19:53:44
 */
@Getter
@Setter
public class FuDocContext {

    /**
     * 枚举参数设置类
     */
    private EnumSettingConfig enumSettingConfig = new EnumSettingConfig();

    /**
     * 配置参数
     */
    private SettingData settingData;

    /**
     * 过滤参数map
     */
    private Map<String, String> filterMap;

    private CustomerSettingData customerSettingData;

    /**
     * desc自增ID
     */
    private transient int descId;

    /**
     * swagger功能解析是否启用（默认启用）
     */
    private boolean enableSwagger = true;

    /**
     * 校验注解message解析成字段说明是否启用（默认启用）
     */
    private boolean enableValidMessage = true;

    /**
     * 触发当前事件时鼠标在java类中的节点
     */
    private PsiElement targetElement;

    /**
     * 默认不开启弹窗
     */
    private boolean syncDialog = true;

    private Set<String> classIdSet = new HashSet<>();

    /**
     * key:节点ID value:节点描述对象
     */
    private Map<Integer, ObjectInfoDesc> descInfoMap;


    /**
     * 存放每一个参数解析后的数据对象
     * key: 参数对象全路径
     * value: 当前参数对象解析后的属性数据
     */
    private Map<String, ObjectInfoDesc> objectInfoDescMap;

    /**
     * 存放还未解析完成的对象
     * 当对象解析完成后会被移动到objectInfoDescMap中
     */
    private Map<String, ObjectInfoDesc> earlyObjectInfoDescMap;

    /**
     * 是否为【Fu Request】模块流程
     */
    private boolean isFuRequest;


    public void setSettingData(SettingData settingData) {
        this.settingData = settingData;
        initSettingData();
    }

    public void initSettingData() {
        Map<String, String> filterMap = new HashMap<>();
        CustomerSettingData customerSettingData;
        if (Objects.isNull(this.settingData) || Objects.isNull(customerSettingData = this.settingData.getCustomerSettingData())) {
            this.settingData = new SettingData();
            customerSettingData = new CustomerSettingData();
        }
        List<FilterFieldBO> filterField = customerSettingData.getSettings_filter_field();
        if (CollectionUtils.isNotEmpty(filterField)) {
            for (FilterFieldBO filterFieldBO : filterField) {
                String fieldNames = filterFieldBO.getFieldNames();
                filterMap.put(filterFieldBO.getClassName(), Objects.isNull(fieldNames) ? FuStringUtils.EMPTY : fieldNames);
            }
        }
        this.customerSettingData = customerSettingData;
        this.filterMap = filterMap;
    }

    /**
     * 生成descId
     */
    public int genDescId() {
        return ++descId;
    }

    public int getClassNo(String classId) {
        if (FuStringUtils.isNotBlank(classId)) {
            classIdSet.add(classId);
        }
        return classIdSet.size();
    }


    public ObjectInfoDesc getByDescId(Integer descId) {
        if (Objects.nonNull(this.descInfoMap) && Objects.nonNull(descId)) {
            return this.descInfoMap.get(descId);
        }
        return null;
    }

    public void add(Integer descId, ObjectInfoDesc objectInfoDesc) {
        if (Objects.nonNull(descId) && Objects.nonNull(objectInfoDesc)) {
            if (Objects.isNull(this.descInfoMap)) {
                this.descInfoMap = new HashMap<>();
            }
            this.descInfoMap.put(descId, objectInfoDesc);
        }
    }


    public ObjectInfoDesc getFromCache(String key) {
        ObjectInfoDesc objectInfoDesc = getObjectInfoDesc(key);
        if (Objects.isNull(objectInfoDesc)) {
            objectInfoDesc = getObjectInfoDescFromEarly(key);
            if (Objects.nonNull(objectInfoDesc)) {
                objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.IS_EARLY, true);
                //构造引用对象返回
                objectInfoDesc.setChildList(Lists.newArrayList(ObjectInfoDescFactory.buildReference()));
            }
        }
        return objectInfoDesc;
    }


    public ObjectInfoDesc getObjectInfoDesc(String key) {
        if (Objects.nonNull(objectInfoDescMap)) {
            return objectInfoDescMap.get(key);
        }
        return null;
    }

    public ObjectInfoDesc getObjectInfoDescFromEarly(String key) {
        if (Objects.nonNull(earlyObjectInfoDescMap)) {
            return earlyObjectInfoDescMap.get(key);
        }
        return null;
    }


    public void add(String key, ObjectInfoDesc objectInfoDesc) {
        if (FuStringUtils.isNotBlank(key) && Objects.nonNull(objectInfoDesc)) {
            if (Objects.isNull(this.earlyObjectInfoDescMap)) {
                this.earlyObjectInfoDescMap = new HashMap<>();
            }
            this.earlyObjectInfoDescMap.put(key, objectInfoDesc);
        }
    }

    public void parseFinish(String key) {
        if (FuStringUtils.isNotBlank(key) && Objects.nonNull(this.earlyObjectInfoDescMap)) {
            if (Objects.isNull(this.objectInfoDescMap)) {
                this.objectInfoDescMap = new HashMap<>();
            }
            ObjectInfoDesc remove = this.earlyObjectInfoDescMap.remove(key);
            if (Objects.nonNull(remove)) {
                this.objectInfoDescMap.put(key, remove);
            }
        }
    }
}
