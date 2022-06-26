package com.wdf.fudoc.pojo.context;

import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.factory.ObjectInfoDescFactory;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
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
public class FuDocContext {
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
     * key:根节点ID value:根节点描述对象
     */
    private Map<Integer, ObjectInfoDesc> rootInfoDescMap;


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
     * 生成descId
     */
    public int genDescId() {
        return ++descId;
    }


    public ObjectInfoDesc getByRootId(Integer rootId) {
        if (Objects.nonNull(this.rootInfoDescMap) && Objects.nonNull(rootId)) {
            return this.rootInfoDescMap.get(rootId);
        }
        return null;
    }

    public void addRoot(Integer rootId, ObjectInfoDesc objectInfoDesc) {
        if (Objects.nonNull(rootId) && Objects.nonNull(objectInfoDesc)) {
            if (Objects.isNull(this.rootInfoDescMap)) {
                this.rootInfoDescMap = new HashMap<>();
            }
            this.rootInfoDescMap.put(rootId, objectInfoDesc);
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
            ObjectInfoDesc remove = this.earlyObjectInfoDescMap.remove(key);
            if (Objects.nonNull(remove)) {
                this.objectInfoDescMap.put(key, remove);
            }
        }
    }
}
