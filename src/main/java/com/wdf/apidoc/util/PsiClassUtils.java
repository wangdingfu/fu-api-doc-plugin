package com.wdf.apidoc.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiTypeElement;

import java.util.Objects;

/**
 * @descption: 获取psiClass对象
 * @author wangdingfu
 * @date 2022-04-09 15:12:35
 */
public class PsiClassUtils {


    /**
     * 获取PsiClass
     *
     * @param project        当前项目
     * @param psiTypeElement 对象类型
     * @return 对象的PsiClass
     */
    public static PsiClass getPsiClass(Project project, PsiTypeElement psiTypeElement) {
        if (Objects.isNull(project) || Objects.isNull(psiTypeElement)) {
            return null;
        }
        return JavaPsiFacade.getInstance(project).findClass(psiTypeElement.getType().getCanonicalText(), psiTypeElement.getResolveScope());
    }
}
