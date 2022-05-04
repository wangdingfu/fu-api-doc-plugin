package com.wdf.apidoc.helper;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.tree.IElementType;
import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.apidoc.constant.enumtype.CommentTagType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 文档注释解析帮助类
 * @Date 2022-04-21 14:09:55
 */
public class DocCommentParseHelper {


    /**
     * 解析注释
     *
     * @param psiDocComment psi注释对象
     * @return 解析后的注释内容
     */
    public static ApiDocCommentData parseComment(PsiDocComment psiDocComment) {
        ApiDocCommentData apiDocCommentData = new ApiDocCommentData();
        if (Objects.nonNull(psiDocComment)) {
            //获取请求参数注释tag集合
            Map<String, String> paramMap = new HashMap<>();
            PsiDocTag[] tags = psiDocComment.findTagsByName(CommentTagType.PARAM.getName());
            for (PsiDocTag tag : tags) {
                paramMap.put(getParamName(tag), formatComment(tag, CommentTagType.PARAM));
            }
            apiDocCommentData.setParamCommentMap(paramMap);

            //获取返回结果参数注释tag
            PsiDocTag tag = psiDocComment.findTagByName(CommentTagType.RETURN.getName());
            apiDocCommentData.setReturnComment(formatComment(tag, CommentTagType.RETURN));

            //获取方法标题注释
            apiDocCommentData.setCommentTitle(getCommentContent(psiDocComment));
        }
        return apiDocCommentData;

    }


    /**
     * 获取注释的内容部分(即当前这段话为注释内容)
     *
     * @param psiDocComment 注释对象
     * @return 一个注释的内容（不包括参数和返回值的主注释）
     */
    public static String getCommentContent(PsiDocComment psiDocComment) {
        PsiElement[] descriptionElements = psiDocComment.getDescriptionElements();
        StringBuilder commentContent = new StringBuilder();
        for (PsiElement descriptionElement : descriptionElements) {
            ASTNode node = descriptionElement.getNode();
            IElementType elementType = node.getElementType();
            if (ApiDocConstants.Comment.PSI_COMMENT_DATA.equals(elementType.getDebugName())) {
                //每一行的主体注释内容
                commentContent.append(node.getText());
            }
        }
        return commentContent.toString();
    }


    /**
     * 格式化每个tag的注释
     *
     * @param tag     注释中以@开头的注释行
     * @param tagType 注释tag类型
     * @return 改行实际的注释内容 例如上面那一行的内容为(注释中以@开头的注释行)
     */
    private static String formatComment(PsiDocTag tag, CommentTagType tagType) {
        if (Objects.isNull(tag)) {
            return StringUtils.EMPTY;
        }
        String text = tag.getText();
        if (StringUtils.isNotBlank(text)) {
            text = text.replace("*", "");
            PsiElement nameElement = tag.getNameElement();
            if (Objects.nonNull(nameElement)) {
                text = text.replace(nameElement.getText(), "");
            }
            String paramName = getParamName(tag);
            //return tag 没有参数名 此处不替换
            if (StringUtils.isNotBlank(paramName) && !CommentTagType.RETURN.equals(tagType)) {
                text = text.replace(paramName, "");
            }
        }
        return text;
    }


    /**
     * 获取注释tag的参数名
     *
     * @param psiDocTag 每一行注释的tag对象
     * @return tag注释行的参数名
     */
    private static String getParamName(PsiDocTag psiDocTag) {
        PsiDocTagValue valueElement;
        if (Objects.nonNull(psiDocTag) && Objects.nonNull(valueElement = psiDocTag.getValueElement())) {
            return valueElement.getText();
        }
        return StringUtils.EMPTY;
    }


}
