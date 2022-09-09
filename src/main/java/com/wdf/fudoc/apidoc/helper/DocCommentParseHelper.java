package com.wdf.fudoc.apidoc.helper;

import com.google.common.collect.Lists;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.javadoc.PsiDocMethodOrFieldRef;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.tree.IElementType;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.constant.enumtype.CommentTagType;
import com.wdf.fudoc.apidoc.pojo.data.CommentTagData;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 文档注释解析帮助类
 * @date 2022-04-21 14:09:55
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
            Map<String, List<CommentTagData>> tagMap = new HashMap<>();
            for (PsiDocTag tag : psiDocComment.getTags()) {
                List<CommentTagData> commentTagDataList = tagMap.get(tag.getName());
                if (Objects.isNull(commentTagDataList)) {
                    commentTagDataList = Lists.newArrayList();
                    tagMap.put(tag.getName(), commentTagDataList);
                }
                commentTagDataList.add(parsePsiCommentTag(tag));
            }
            apiDocCommentData.setTagMap(tagMap);
            apiDocCommentData.setCommentTitle(getCommentContent(psiDocComment));
        }
        return apiDocCommentData;
    }


    private static CommentTagData parsePsiCommentTag(PsiDocTag psiDocTag) {
        String tagName = psiDocTag.getName();
        CommentTagData commentTagData = buildCommentTagData(psiDocTag);
        if (CommentTagType.SEE.getName().equals(tagName) || CommentTagType.LINK.getName().equals(tagName)) {
            //获取引用
            PsiElement elementFromTag = getElementFromTag(psiDocTag);
            if (Objects.nonNull(elementFromTag)) {
                commentTagData.setPsiElement(elementFromTag.getNode().getPsi());
            }
        }
        return commentTagData;
    }

    public static CommentTagData buildCommentTagData(PsiDocTag psiDocTag) {
        CommentTagData commentTagData = new CommentTagData();
        PsiElement[] dataElements = psiDocTag.getDataElements();
        for (PsiElement dataElement : dataElements) {
            String elementType = getElementType(dataElement);
            if (FuDocConstants.Comment.PSI_PARAMETER_REF.equals(elementType)) {
                //设置param的key
                commentTagData.setName(formatText(dataElement));
            } else {
                String tagDataValue = commentTagData.getValue();
                String comment = getComment(elementType, dataElement);
                commentTagData.setValue(StringUtils.isBlank(tagDataValue) ? comment : tagDataValue + " " + comment);
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
     * 获取注释的内容部分(即当前这段话为注释内容)
     *
     * @param psiDocComment 注释对象
     * @return 一个注释的内容（不包括参数和返回值的主注释）
     */
    public static String getCommentContent(PsiDocComment psiDocComment) {
        PsiElement[] descriptionElements = psiDocComment.getDescriptionElements();
        StringBuilder commentContent = new StringBuilder();
        for (PsiElement element : descriptionElements) {
            commentContent.append(getComment(getElementType(element), element));
        }
        return commentContent.toString();
    }

    private static String getComment(String elementType, PsiElement element) {
        switch (elementType) {
            case FuDocConstants.Comment.PSI_COMMENT_TAG_VALUE:
            case FuDocConstants.Comment.PSI_COMMENT_DATA:
                return formatText(element);
            default:
        }
        return StringUtils.EMPTY;
    }

    private static String formatText(PsiElement psiElement) {
        String text = psiElement.getText();
        if (StringUtils.isNotBlank(text)) {
            return text.replace("*", "").replace("\n", "");
        }
        return StringUtils.EMPTY;
    }


    private static String getElementType(PsiElement psiElement) {
        ASTNode node = psiElement.getNode();
        IElementType elementType = node.getElementType();
        return elementType.toString();
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
