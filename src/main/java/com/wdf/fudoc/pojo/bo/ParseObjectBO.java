package com.wdf.fudoc.pojo.bo;

import com.intellij.psi.PsiType;
import com.wdf.fudoc.parse.field.FuDocField;
import com.wdf.fudoc.pojo.context.FuDocContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author wangdingfu
 * @description 解析对象的父级对象信息bo
 * @Date 2022-04-18 20:58:23
 */
@Getter
@Setter
public class ParseObjectBO {

    /**
     * 上下文对象
     */
    private FuDocContext fuDocContext;

    /**
     * 字段
     */
    private FuDocField fuDocField;

    /**
     * 泛型map
     */
    private Map<String, PsiType> genericsMap;


    public PsiType getPsiType(String generics) {
        if (MapUtils.isNotEmpty(genericsMap) && StringUtils.isNotBlank(generics)) {
            return genericsMap.get(generics);
        }
        return null;
    }

    public ParseObjectBO(FuDocContext fuDocContext) {
        this.fuDocContext = fuDocContext;
    }
}
