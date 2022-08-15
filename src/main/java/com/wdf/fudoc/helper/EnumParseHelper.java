package com.wdf.fudoc.helper;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiEnumConstantImpl;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.impl.source.tree.java.PsiBinaryExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.wdf.fudoc.FuDocRender;
import com.wdf.fudoc.config.EnumSettingConfig;
import com.wdf.fudoc.pojo.bo.EnumParseBO;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.pojo.data.FuDocEnumData;
import com.wdf.fudoc.pojo.data.FuDocEnumItemData;
import com.wdf.fudoc.util.MapListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 枚举解析
 *
 * @author wangdingfu
 * @Description
 * @date 2022-08-05 23:41:11
 */
public class EnumParseHelper {


    public static String parseEnum(EnumSettingConfig enumSettingConfig, PsiClass psiClass, Integer type) {
        List<FuDocEnumItemData> fuDocEnumItemDataList = Lists.newArrayList();
        for (PsiField field : psiClass.getFields()) {
            PsiElement resolve;
            if (field instanceof PsiEnumConstantImpl && Objects.nonNull(field.getReference()) && (resolve = field.getReference().resolve()) instanceof PsiMethodImpl) {
                PsiParameter[] parameters = ((PsiMethodImpl) resolve).getParameterList().getParameters();
                if (parameters.length <= 0) {
                    //枚举的构造方法为空 则不解析
                    continue;
                }
                PsiExpressionList argumentList = ((PsiEnumConstantImpl) field).getArgumentList();
                PsiExpression[] expressions;
                if (Objects.nonNull(argumentList) && (expressions = argumentList.getExpressions()).length == parameters.length) {
                    List<EnumParseBO> enumParseBOList = Lists.newArrayList();
                    for (int i = 0; i < expressions.length; i++) {
                        enumParseBOList.add(new EnumParseBO(parameters[i], parseExpression(expressions[i])));
                    }
                    MapListUtil<String, EnumParseBO> instance = MapListUtil.getInstance(enumParseBOList, EnumParseBO::getType);
                    //获取code 优先选取Integer类型
                    String code = selectValue(instance, enumSettingConfig.getCodeNameList(), enumSettingConfig.getCodeTypeList());
                    //获取msg 优先选取String类型
                    String msg = selectValue(instance, enumSettingConfig.getValueNameList(), enumSettingConfig.getValueTypeList());
                    fuDocEnumItemDataList.add(new FuDocEnumItemData(code, msg));
                }
            }
        }
        FuDocEnumData fuDocEnumData = new FuDocEnumData();
        fuDocEnumData.setEnumName(psiClass.getName());
        ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(psiClass.getDocComment());
        String commentTitle = apiDocCommentData.getCommentTitle();
        fuDocEnumData.setTitle(StringUtils.isBlank(commentTitle) ? psiClass.getName() : apiDocCommentData.getCommentTitle());
        //移除code为空或则描述为空的枚举项
        fuDocEnumItemDataList.removeIf(a -> StringUtils.isBlank(a.getCode()) || StringUtils.isBlank(a.getMsg()));
        if (CollectionUtils.isNotEmpty(fuDocEnumItemDataList)) {
            fuDocEnumData.setItemList(fuDocEnumItemDataList);
            //将枚举数据渲染成markdown格式接口文档
            return FuDocRender.enumRender(fuDocEnumData, type);
        }
        return StringUtils.EMPTY;
    }


    private static String selectValue(MapListUtil<String, EnumParseBO> instance, List<String> codeList, List<String> keys) {
        //精准选取
        for (String key : keys) {
            String value = selectValue(codeList, instance.get(key), false);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        //如果还是没有取到 则随机选取
        for (String key : keys) {
            String value = selectValue(codeList, instance.get(key), true);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }


    private static String selectValue(List<String> codeList, List<EnumParseBO> enumParseBOList, boolean random) {
        Optional<EnumParseBO> first = random ? enumParseBOList.stream().findFirst()
                : enumParseBOList.stream().filter(f -> codeList.contains(f.getName())).findFirst();
        return first.map(EnumParseBO::getValue).orElse(null);
    }


    private static String parseExpression(PsiExpression psiExpression) {
        if (psiExpression instanceof PsiBinaryExpressionImpl) {
            PsiBinaryExpressionImpl psiBinaryExpression = (PsiBinaryExpressionImpl) psiExpression;
            PsiExpression[] operands = psiBinaryExpression.getOperands();
            StringBuilder sb = new StringBuilder();
            for (PsiExpression operand : operands) {
                sb.append(parseExpression(operand));
            }
            return sb.toString();
        } else if (psiExpression instanceof PsiLiteralExpressionImpl) {
            PsiLiteralExpressionImpl expression = (PsiLiteralExpressionImpl) psiExpression;
            Object value = expression.getValue();
            if (Objects.nonNull(value)) {
                return value.toString();
            }
        } else if (psiExpression instanceof PsiReferenceExpressionImpl) {
            PsiReference reference = psiExpression.getReference();
            PsiElement psiElement;
            if (Objects.nonNull(reference) && Objects.nonNull(psiElement = reference.resolve()) && psiElement instanceof PsiFieldImpl) {
                PsiFieldImpl psiField = (PsiFieldImpl) psiElement;
                return parseExpression(psiField.getInitializer());
            }
        }
        return StringUtils.EMPTY;
    }
}
