package com.wdf.fudoc.futool.beancopy.bo;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiField;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.FuPsiUtils;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-08-06 15:14:15
 */
@Getter
@Setter
@AllArgsConstructor
public class CopyBeanBO {
    private String variableName;

    private PsiClass psiClass;

    private PsiElement psiElement;

    private final Map<String, CopyBeanMethodBO> fieldMethodMap = new ConcurrentHashMap<>();


    public Map<String, CopyBeanMethodBO> initFiled() {
        this.fieldMethodMap.clear();
        PsiField[] allFields = psiClass.getAllFields();
        Map<String, PsiField> psiFieldMap = ObjectUtils.listToMap(Lists.newArrayList(allFields), PsiField::getName);
        PsiMethod[] allMethods = psiClass.getAllMethods();
        for (PsiMethod psiMethod : allMethods) {
            String methodName = psiMethod.getName();
            FuPsiMethod fuPsiMethod = FuPsiUtils.methodToFiled(methodName);
            if (Objects.isNull(fuPsiMethod) || !fuPsiMethod.isFieldMethod()) {
                continue;
            }
            String fieldName = fuPsiMethod.getFieldName();
            PsiField psiField = psiFieldMap.get(fieldName);
            if (Objects.isNull(psiField)) {
                continue;
            }
            FuDocPsiField fuDocPsiField = new FuDocPsiField(psiField);
            if (fuDocPsiField.hasProperty(FuDocConstants.ModifierProperty.STATIC) || fuDocPsiField.hasProperty(FuDocConstants.ModifierProperty.FINAL)) {
                continue;
            }
            CopyBeanMethodBO copyBeanMethodBO = fieldMethodMap.get(fieldName);
            if (Objects.isNull(copyBeanMethodBO)) {
                copyBeanMethodBO = new CopyBeanMethodBO();
                fieldMethodMap.put(fieldName, copyBeanMethodBO);
            }
            if (fuPsiMethod.isSetter()) {
                copyBeanMethodBO.setSetMethod(methodName);
            } else if (fuPsiMethod.isGetter()) {
                copyBeanMethodBO.setGetMethod(methodName);
            }
        }
        return this.fieldMethodMap;
    }

}
