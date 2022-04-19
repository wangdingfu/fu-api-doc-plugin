package com.wdf.apidoc.execute;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ParseObjectParentBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.factory.ParseObjectHandlerFactory;
import com.wdf.apidoc.handler.ParseObjectHandler;
import org.apache.commons.collections.CollectionUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @author wangdingfu
 * @Descption 解析对象执行器
 * @Date 2022-04-18 21:09:26
 */
public class ParseObjectExecutor {


    /**
     * 执行对象解析流程
     *
     * @param psiType  需要解析的对象类型
     * @param parentBO 解析对象的所属父级对象
     * @return 解析后生成的ApiDoc数据对象
     */
    public static ApiDocObjectData execute(PsiType psiType, ParseObjectParentBO parentBO) {
        List<ParseObjectHandler> parseObjectHandlerList = ParseObjectHandlerFactory.getParseObjectHandlerList();
        if (CollectionUtils.isNotEmpty(parseObjectHandlerList)) {
            parseObjectHandlerList.sort(Comparator.comparing(ParseObjectHandler::sort));
            for (ParseObjectHandler parseObjectHandler : parseObjectHandlerList) {
                if (parseObjectHandler.isParse(psiType)) {
                    return parseObjectHandler.parse(psiType, parentBO);
                }
            }
        }
        //没有解析器可以执行 不支持该类型的对象
        return null;
    }
}
