package com.wdf.apidoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.pojo.data.AnnotationValueData;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.apidoc.pojo.desc.MethodInfoDesc;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 组装Controller接口文档
 * @date 2022-05-09 23:32:39
 */
public class ControllerAssembleService extends AbstractAssembleService {


    /**
     * 判断当前实现类是否为Controller
     *
     * @param classInfoDesc java类信息描述对象
     * @return true:是  false:不是
     */
    @Override
    public boolean isAssemble(ClassInfoDesc classInfoDesc) {
        if (Objects.isNull(classInfoDesc)) {
            return false;
        }
        //判断是否有Controller|RestController注解
        return CollectionUtils.isNotEmpty(classInfoDesc.getMethodList())
                && classInfoDesc.exists(AnnotationConstants.CONTROLLER, AnnotationConstants.REST_CONTROLLER);
    }

    /**
     * Controller类组装成接口文档
     *
     * @param classInfoDesc Controller类描述信息
     * @return 接口集合
     */
    @Override
    public List<FuApiDocItemData> assemble(ClassInfoDesc classInfoDesc) {
        //获取Controller类上的请求路径
        List<String> controllerUrlList = Lists.newArrayList();
        classInfoDesc.getAnnotation(AnnotationConstants.REQUEST_MAPPING).ifPresent(annotationData -> {
            //获取value属性值
            AnnotationValueData value = annotationData.getValue();
            List<String> listValue = value.getListValue();

        });

        List<MethodInfoDesc> methodList = classInfoDesc.getMethodList();
        if (CollectionUtils.isNotEmpty(methodList)) {
            //解析方法
            for (MethodInfoDesc methodInfoDesc : methodList) {
                if (methodInfoDesc.exists(AnnotationConstants.MAPPING)) {

                }
            }
        }


        return null;
    }
}
