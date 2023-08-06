package com.wdf.fudoc.test.action;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiField;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author wangdingfu
 * @date 2023-08-06 15:14:15
 */
@Getter
@Setter
@AllArgsConstructor
public class CopyBeanBO {
    private static final String setRegex = "set(\\w+)";
    private static final String getRegex = "get(\\w+)";
    private static final Pattern setMethod = Pattern.compile(setRegex);
    private static final Pattern getMethod = Pattern.compile(getRegex);

    private String variableName;

    private PsiClass psiClass;


    private final Map<String, CopyBeanMethodBO> fieldMethodMap = new ConcurrentHashMap<>();


    public Map<String, CopyBeanMethodBO> initFiled() {
        this.fieldMethodMap.clear();
        PsiField[] allFields = psiClass.getAllFields();
        Map<String, PsiField> psiFieldMap = ObjectUtils.listToMap(Lists.newArrayList(allFields), PsiField::getName);
        PsiMethod[] allMethods = psiClass.getAllMethods();
        for (PsiMethod psiMethod : allMethods) {
            String methodName = psiMethod.getName();
            if (Pattern.matches(getRegex, methodName)) {
                paddingMethodInfo(psiFieldMap, methodName, getMethod, data -> data.setGetMethod(methodName));
            } else if (Pattern.matches(setRegex, methodName)) {
                paddingMethodInfo(psiFieldMap, methodName, setMethod, data -> data.setSetMethod(methodName));
            }
        }
        return this.fieldMethodMap;
    }


    private void paddingMethodInfo(Map<String, PsiField> psiFieldMap, String methodName, Pattern pattern, Consumer<CopyBeanMethodBO> consumer) {
        String fieldName = pattern.matcher(methodName).replaceAll("$1").toLowerCase();
        PsiField psiField = psiFieldMap.get(fieldName);
        if (Objects.nonNull(psiField)) {
            FuDocPsiField fuDocPsiField = new FuDocPsiField(psiField);
            if (fuDocPsiField.hasProperty(FuDocConstants.ModifierProperty.STATIC)
                    || fuDocPsiField.hasProperty(FuDocConstants.ModifierProperty.FINAL)) {
                return;
            }
            CopyBeanMethodBO copyBeanMethodBO = fieldMethodMap.get(fieldName);
            if (Objects.isNull(copyBeanMethodBO)) {
                copyBeanMethodBO = new CopyBeanMethodBO();
                fieldMethodMap.put(fieldName, copyBeanMethodBO);
            }
            consumer.accept(copyBeanMethodBO);
        }
    }


}
