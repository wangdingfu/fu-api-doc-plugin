package com.wdf.apidoc.parse.comment;

import com.intellij.psi.javadoc.PsiDocComment;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;

/**
 * @author wangdingfu
 * @Descption 抽象注释解析器
 * @Date 2022-04-21 21:08:53
 */
public abstract class AbstractCommentParser implements CommentParser {





    /**
     * 注释解析
     *
     * @param psiDocComment intellij 描述的注释对象
     * @return 结构化的注释对象
     */
    @Override
    public ApiDocCommentData parse(PsiDocComment psiDocComment) {
        return null;
    }
}
