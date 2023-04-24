package com.wdf.fudoc.apidoc.pojo.bo;

import com.intellij.psi.PsiType;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamType;
import com.wdf.fudoc.apidoc.mock.real.MockRealData;
import com.wdf.fudoc.apidoc.parse.field.FuDocField;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author wangdingfu
 * @description 解析对象的父级对象信息bo
 * @date 2022-04-18 20:58:23
 */
@Getter
@Setter
public class ParseObjectBO {

    /**
     * 根节点ID
     */
    private Integer rootId;
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

    /**
     * 参数类型 标识当前解析的参数是请求参数还是响应参数
     */
    private ParamType paramType;


    /**
     * mock真实示例数据
     */
    private MockRealData mockRealData;


    public PsiType getPsiType(String generics) {
        if (MapUtils.isNotEmpty(genericsMap) && StringUtils.isNotBlank(generics)) {
            return genericsMap.get(generics);
        }
        return null;
    }

    public ParseObjectBO(FuDocContext fuDocContext, MockRealData mockRealData) {
        this.fuDocContext = fuDocContext;
        this.mockRealData = mockRealData;
    }
}
