package com.wdf.apidoc.pojo.desc;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.wdf.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.apidoc.pojo.data.AnnotationDataMap;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 对象信息描述
 * @date 2022-05-08 22:22:39
 */
@Getter
@Setter
public class ObjectInfoDesc extends AnnotationDataMap {

    private PsiType psiType;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 显示在页面的字段类型
     */
    private String typeView;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段注释
     */
    private String docText;

    /**
     * 子属性字段集合(当前对象不为基本对象 且有自己属性字段时)
     */
    private List<ObjectInfoDesc> childList;


    public boolean isCollection() {
        return InheritanceUtil.isInheritor(psiType, CommonClassNames.JAVA_UTIL_COLLECTION);
    }

    public boolean isCommonObject() {
        return StringUtils.isNotBlank(CommonObjectType.getName(psiType.getCanonicalText()));
    }

    public boolean isPrimitive() {
        return psiType instanceof PsiPrimitiveType;
    }

}
