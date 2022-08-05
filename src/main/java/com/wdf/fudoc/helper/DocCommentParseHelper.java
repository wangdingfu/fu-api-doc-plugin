package com.wdf.fudoc.helper;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.javadoc.PsiDocMethodOrFieldRef;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.tree.IElementType;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.constant.enumtype.CommentTagType;
import com.wdf.fudoc.pojo.data.CommentTagData;
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
            Map<String, Map<String, CommentTagData>> tagMap = new HashMap<>();
            for (PsiDocTag tag : psiDocComment.getTags()) {
                Map<String, CommentTagData> tagParamMap = tagMap.get(tag.getName());
                if (Objects.isNull(tagParamMap)) {
                    tagParamMap = new HashMap<>();
                    tagMap.put(tag.getName(), tagParamMap);
                }
                CommentTagData commentTagData = parsePsiCommentTag(tag);
                tagParamMap.put(commentTagData.getName(), commentTagData);
            }
            apiDocCommentData.setTagMap(tagMap);
            apiDocCommentData.setCommentTitle(getCommentContent(psiDocComment));
        }
        return apiDocCommentData;

    }


    private static CommentTagData parsePsiCommentTag(PsiDocTag psiDocTag) {
        CommentTagType commentTagType = CommentTagType.getEnum(psiDocTag.getName());
        CommentTagData commentTagData = new CommentTagData(getParamName(psiDocTag), getTagCommentValue(psiDocTag));
        if (Objects.nonNull(commentTagType)) {
            switch (commentTagType) {
                case SEE:
                case LINK:
                    PsiElement elementFromTag = getElementFromTag(psiDocTag);
                    if (Objects.nonNull(elementFromTag)) {
                        commentTagData.setPsiElement(elementFromTag.getNode().getPsi());
                    }
                default:
            }
        }
        return commentTagData;
    }


    private static PsiElement getElementFromTag(PsiDocTag psiDocTag) {
        for (PsiElement dataElement : psiDocTag.getDataElements()) {
            if (dataElement instanceof PsiDocMethodOrFieldRef && Objects.nonNull(dataElement.getReference())) {
                return dataElement.getReference().resolve();
            }
            for (PsiElement child : dataElement.getChildren()) {
                PsiReference reference = child.getReference();
                if (Objects.nonNull(reference)) {
                    return reference.resolve();
                }
            }
        }
        return null;
    }


    /**
     * PsiDocMethodOrFieldRef
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
            if (FuDocConstants.Comment.PSI_COMMENT_DATA.equals(elementType.toString())) {
                //每一行的主体注释内容
                String text = node.getText();
                if (StringUtils.isNotBlank(text)) {
                    text = text.replace("\n", "");
                    commentContent.append(text);
                }
            }
        }
        return commentContent.toString();
    }


    /**
     * 获取tag注释的内容
     *
     * @param psiDocTag tag对象
     * @return tag注释内容
     */
    private static String getTagCommentValue(PsiDocTag psiDocTag) {
        PsiDocTagValue valueElement;
        if (Objects.isNull(psiDocTag) || Objects.isNull(valueElement = psiDocTag.getValueElement())) {
            return StringUtils.EMPTY;
        }
        String valueText = valueElement.getText();
        if (StringUtils.isNotBlank(valueText)) {
            return valueText.replace("*", "").replace("\n", "");
        }
        return valueText;
    }

    /**
     * 格式化每个tag的注释
     *
     * @param tag     注释中以@开头的注释行
     * @param tagType 注释tag类型
     * @return 改行实际的注释内容 例如上面那一行的内容为(注释中以@开头的注释行)
     */
    private static String parseTagCommentValue(PsiDocTag tag, CommentTagType tagType) {
        if (Objects.isNull(tag)) {
            return StringUtils.EMPTY;
        }
        String text = tag.getText();
        if (StringUtils.isNotBlank(text)) {
            text = text.replace("*", "").replace("\n", "");
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
