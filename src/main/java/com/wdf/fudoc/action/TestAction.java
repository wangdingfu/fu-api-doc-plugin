package com.wdf.fudoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.wdf.fudoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 测试入口
 * @Author wangdingfu
 * @Description
 * @Date 2022-08-05 22:41:02
 */
public class TestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiElement targetElement = PsiClassUtils.getTargetElement(e);
        if (Objects.isNull(targetElement)) {
            return;
        }
        //获取当前操作的类
        PsiClass psiClass = PsiClassUtils.getPsiClass(targetElement);
        for (PsiField allField : psiClass.getAllFields()) {
            ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(allField.getDocComment());
            System.out.println(apiDocCommentData.getCommentTitle());
        }

    }
}
