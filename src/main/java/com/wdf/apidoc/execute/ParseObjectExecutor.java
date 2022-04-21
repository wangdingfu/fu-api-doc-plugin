package com.wdf.apidoc.execute;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import com.wdf.apidoc.factory.ParseObjectHandlerFactory;
import com.wdf.apidoc.parse.object.ApiDocObjectParser;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 解析对象执行器
 * @Date 2022-04-18 21:09:26
 */
public class ParseObjectExecutor {


    /**
     * 执行对象解析流程
     *
     * @param psiType       需要解析的对象类型
     * @param parseObjectBO 解析对象的所属父级对象
     * @return 解析后生成的ApiDoc数据对象
     */
    public static ApiDocObjectData execute(PsiType psiType, ParseObjectBO parseObjectBO) {
        List<ApiDocObjectParser> apiDocObjectParserList = ParseObjectHandlerFactory.getOBJECT_PARSER_LIST();
        if (CollectionUtils.isNotEmpty(apiDocObjectParserList)) {
            //泛型替换
            psiType = formatPsiType(psiType, parseObjectBO);
            for (ApiDocObjectParser apiDocObjectParser : apiDocObjectParserList) {
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
