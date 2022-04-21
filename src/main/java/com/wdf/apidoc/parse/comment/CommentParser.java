package com.wdf.apidoc.parse.comment;

import com.intellij.psi.javadoc.PsiDocComment;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;

/**
 * @author wangdingfu
 * @Descption java注释解析器
 * @Date 2022-04-21 21:06:33
 */
public interface CommentParser {


    /**
     * java注释内容解析器
     *
     * @param psiDocComment intellij 描述的注释对象
     * @return 解析成结构化的注释对象
     */
    ApiDocCommentData parse(PsiDocComment psiDocComment);
}
