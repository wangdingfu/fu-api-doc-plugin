package com.wdf.apidoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.constant.enumtype.ContentType;
import com.wdf.apidoc.mock.ApiDocMockData;
import com.wdf.apidoc.mock.FakerMockData;
import com.wdf.apidoc.pojo.data.FuApiDocParamData;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 组装抽象类
 * @date 2022-05-09 23:32:00
 */
public abstract class AbstractAssembleService implements ApiDocAssembleService {


    /**
     * 构建渲染接口文档参数的数据对象
     *
     * @param objectInfoDescList 对象解析后的描述信息集合
     * @return 接口文档页面显示的参数数据
     */
    protected List<FuApiDocParamData> buildFuApiDocParamData(List<ObjectInfoDesc> objectInfoDescList) {
        List<FuApiDocParamData> fuApiDocParamDataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            fuApiDocParamDataList.addAll(buildFuApiDocParamData(objectInfoDescList, 0 + ""));
        }
        return fuApiDocParamDataList;
    }


    protected List<FuApiDocParamData> buildFuApiDocParamData(List<ObjectInfoDesc> objectInfoDescList, String groupSort) {
        List<FuApiDocParamData> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(objectInfoDescList)) {
            for (int i = 0; i < objectInfoDescList.size(); i++) {
                ObjectInfoDesc objectInfoDesc = objectInfoDescList.get(i);
                FuApiDocParamData fuApiDocParamData = new FuApiDocParamData();
                fuApiDocParamData.setGroupSort(groupSort + "_" + i);
                fuApiDocParamData.setParamName(objectInfoDesc.getName());
                fuApiDocParamData.setParamDesc(objectInfoDesc.getDocText());
                fuApiDocParamData.setParamType(objectInfoDesc.getTypeView());
                //设置是否必填
                objectInfoDesc.consumerAnnotation(AnnotationConstants.VALID_NOT, annotationData -> fuApiDocParamData.setParamRequire("是"));
                resultList.add(fuApiDocParamData);
                List<ObjectInfoDesc> childList = objectInfoDesc.getChildList();
                if (CollectionUtils.isNotEmpty(childList)) {
                    resultList.addAll(buildFuApiDocParamData(childList, i + ""));
                }
            }
        }
        return resultList;
    }


    protected String mockData(ContentType contentType, List<ObjectInfoDesc> objectInfoDescList) {
        ApiDocMockData apiDocMockData = new FakerMockData();
        return apiDocMockData.mock(contentType, objectInfoDescList);
    }
}
