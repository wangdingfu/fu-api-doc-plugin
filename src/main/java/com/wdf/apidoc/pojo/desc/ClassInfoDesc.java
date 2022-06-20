package com.wdf.apidoc.pojo.desc;

import com.wdf.apidoc.pojo.data.AnnotationDataMap;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 类信息描述
 * @date 2022-05-08 22:21:40
 */
@Getter
@Setter
public class ClassInfoDesc extends BaseInfoDesc {




    /**
     * 方法集合
     */
    private List<MethodInfoDesc> methodList;
}
