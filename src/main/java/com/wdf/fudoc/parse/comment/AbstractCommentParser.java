package com.wdf.fudoc.parse.comment;

import com.intellij.psi.javadoc.PsiDocComment;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;

/**
 * @author wangdingfu
 * @Descption 抽象注释解析器
 * @date 2022-04-21 21:08:53
 */
public abstract class AbstractCommentParser implements CommentParser {





    /**
     * 注释解析
     *
     * 解析规则
     * 1) 第一行必须以/** 开头
     * 2) 最后一行必须以 *\\/ 结束
     * 3) 第一行/** 之后如果有数据 则需要读取
     * 4) 中间行如果以*开头则需要替换掉* 没有则将内容前的空格替换掉 内容后的空格也需要替换掉
     * 5) 需要将{}中的内容清空掉
     * 6) 最后一行将*\\/替换掉 如果结尾还存在* 则需要将结尾的*都清掉
     *
     * @param psiDocComment intellij 描述的注释对象
     * @return 结构化的注释对象
     */
    @Override
    public ApiDocCommentData parse(PsiDocComment psiDocComment) {
        //注释内容
        String text = psiDocComment.getText();
        // 解析规则



        return null;
    }




}
