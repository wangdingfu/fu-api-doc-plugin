package com.wdf.apidoc.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.wdf.apidoc.bo.PsiClassTypeBO;

import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 获取psiClass对象
 * @date 2022-04-09 15:12:35
 */
public class PsiClassUtils {


    public static PsiClass getPsiClass(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

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


    /**
     * 获取指定类的父类的类型
     *
     * @param psiClass 指定类
     * @return 父类的类型
     */
    public static PsiClassTypeBO getSuperClassType(PsiClass psiClass) {
        if(isClass(psiClass) && isClass(psiClass.getSuperClass())){
            for (PsiClassType superType : psiClass.getSuperTypes()) {
                PsiClass superClass = PsiUtil.resolveClassInType(superType);
                if (Objects.nonNull(superClass) && !superClass.isInterface() && !superClass.isEnum()) {
                    return new PsiClassTypeBO(superClass, superType);
                }
            }
        }
        return null;
    }


    public static boolean isClass(PsiClass psiClass) {
        return !(Objects.isNull(psiClass) || psiClass.isEnum() || psiClass.isInterface() || psiClass.isAnnotationType() || psiClass.isRecord());
    }
}
