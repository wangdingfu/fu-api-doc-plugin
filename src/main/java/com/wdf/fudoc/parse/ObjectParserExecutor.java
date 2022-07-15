package com.wdf.fudoc.parse;

import cn.hutool.core.bean.BeanUtil;
import com.intellij.psi.PsiType;
import com.wdf.fudoc.parse.object.ApiDocObjectParser;
import com.wdf.fudoc.parse.object.impl.*;
import com.wdf.fudoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.compress.utils.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 解析对象执行器
 * @Date 2022-04-18 21:09:26
 */
public class ObjectParserExecutor {

    private static final List<ApiDocObjectParser> OBJECT_PARSER_LIST = Lists.newArrayList();

    static {
        OBJECT_PARSER_LIST.add(new FuDocPrimitiveParser());
        OBJECT_PARSER_LIST.add(new FuDocCommonObjectParser());
        OBJECT_PARSER_LIST.add(new FuDocFilterObjectParser());
        OBJECT_PARSER_LIST.add(new FuDocArrayParser());
        OBJECT_PARSER_LIST.add(new FuDocCollectionParser());
        OBJECT_PARSER_LIST.add(new FuDocMapParser());
        OBJECT_PARSER_LIST.add(new FuDocObjectParser());
        OBJECT_PARSER_LIST.add(new FuDocMultipartFileParser());
        OBJECT_PARSER_LIST.add(new FuDocDefaultParser());
        OBJECT_PARSER_LIST.add(new FuDocVoidParser());
        //根据各自实现类的优先加载顺序来排序
        OBJECT_PARSER_LIST.sort(Comparator.comparing(ApiDocObjectParser::sort));
    }

    /**
     * 执行对象解析流程
     *
     * @param psiType       需要解析的对象类型
     * @param parseObjectBO 解析对象的所属父级对象
     * @return 返回解析对象后的一些属性 注解 注释等描述信息
     */
    public static ObjectInfoDesc execute(PsiType psiType, ParseObjectBO parseObjectBO) {
        if (Objects.nonNull(psiType) && Objects.nonNull(parseObjectBO) && Objects.nonNull(parseObjectBO.getFuDocContext())) {
            //泛型替换
            psiType = formatPsiType(psiType, parseObjectBO);
            for (ApiDocObjectParser apiDocObjectParser : ObjectParserExecutor.OBJECT_PARSER_LIST) {
                if (apiDocObjectParser.isParse(psiType)) {
                    return apiDocObjectParser.parse(psiType, BeanUtil.copyProperties(parseObjectBO, ParseObjectBO.class));
                }
            }
        }
        //没有解析器可以执行 不支持该类型的对象
        return null;
    }


    /**
     * 格式化对象类型（如果对象为泛型 则将泛型替换为真实对象类型）
     *
     * @param psiType       对象类型
     * @param parseObjectBO 解析对象所需要的参数
     * @return 实际的对象类型
     */
    private static PsiType formatPsiType(PsiType psiType, ParseObjectBO parseObjectBO) {
        PsiType generics;
        if (Objects.nonNull(parseObjectBO) && Objects.nonNull(generics = parseObjectBO.getPsiType(psiType.getCanonicalText()))) {
            //替换泛型类
            return generics;
        }
        return psiType;
    }
}
