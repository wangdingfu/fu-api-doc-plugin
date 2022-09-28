package com.wdf.fudoc.apidoc.pojo.bo;

import com.wdf.fudoc.apidoc.pojo.annotation.SpringAnnotationData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-09-27 17:04:03
 */
@Getter
@Setter
public class RootParamBO {


    /**
     * 当前参数对应标识的注解
     */
    private SpringAnnotationData springAnnotationData;

    /**
     * 对象解析后的描述信息
     */
    private List<FuDocParamData> fuDocParamDataList;

    /**
     * mock的请求数据
     */
    private String mockData;
}
