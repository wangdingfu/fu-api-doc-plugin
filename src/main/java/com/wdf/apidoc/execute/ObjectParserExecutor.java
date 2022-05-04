package com.wdf.apidoc.execute;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.parse.object.impl.*;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import com.wdf.apidoc.parse.object.ApiDocObjectParser;
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
        OBJECT_PARSER_LIST.add(new ApiDocPrimitiveParser());
        OBJECT_PARSER_LIST.add(new ApiDocCommonObjectParser());
        OBJECT_PARSER_LIST.add(new ApiDocOtherObjectParser());
        OBJECT_PARSER_LIST.add(new ApiDocCollectionParser());
        OBJECT_PARSER_LIST.add(new ApiDocMapParser());
        OBJECT_PARSER_LIST.add(new ApiDocDefaultParser());
        //根据各自实现类的优先加载顺序来排序
        OBJECT_PARSER_LIST.sort(Comparator.comparing(ApiDocObjectParser::sort));
    }

    /**
     * 执行对象解析流程
     *
     * @param psiType       需要解析的对象类型
     * @param parseObjectBO 解析对象的所属父级对象
     * @return 解析后生成的ApiDoc数据对象
     */
    public static ApiDocObjectData execute(PsiType psiType, ParseObjectBO parseObjectBO) {
        if (Objects.nonNull(psiType) && Objects.nonNull(parseObjectBO) && Objects.nonNull(parseObjectBO.getApiDocContext())) {
            //泛型替换
            psiType = formatPsiType(psiType, parseObjectBO);
            for (ApiDocObjectParser apiDocObjectParser : ObjectParserExecutor.OBJECT_PARSER_LIST) {
                if (apiDocObjectParser.isParse(psiType)) {
                    return apiDocObjectParser.parse(psiType, parseObjectBO);
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
