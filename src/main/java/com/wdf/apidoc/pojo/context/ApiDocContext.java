package com.wdf.apidoc.pojo.context;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.factory.ObjectInfoDescFactory;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 生成API接口文档全局上下文
 * @date 2022-04-05 19:53:44
 */
@Getter
@Setter
public class ApiDocContext {

    /**
     * 当前项目
     */
    private Project project;

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


    public ObjectInfoDesc getFromCache(String key) {
        ObjectInfoDesc objectInfoDesc = getObjectInfoDesc(key);
        if (Objects.isNull(objectInfoDesc)) {
            objectInfoDesc = getObjectInfoDescFromEarly(key);
            if (Objects.nonNull(objectInfoDesc)) {
                objectInfoDesc.addExtInfo(ApiDocConstants.ExtInfo.IS_EARLY, true);
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
        if (StringUtils.isNotBlank(key) && Objects.nonNull(objectInfoDesc)) {
            if (Objects.isNull(this.earlyObjectInfoDescMap)) {
                this.earlyObjectInfoDescMap = new HashMap<>();
            }
            this.earlyObjectInfoDescMap.put(key, objectInfoDesc);
        }
    }

    public void parseFinish(String key) {
        if (StringUtils.isNotBlank(key) && Objects.nonNull(this.earlyObjectInfoDescMap)) {
            if (Objects.isNull(this.objectInfoDescMap)) {
                this.objectInfoDescMap = new HashMap<>();
            }
            this.objectInfoDescMap.put(key, this.earlyObjectInfoDescMap.remove(key));
        }
    }
}
