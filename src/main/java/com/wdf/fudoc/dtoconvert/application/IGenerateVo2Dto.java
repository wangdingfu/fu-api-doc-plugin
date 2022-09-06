package com.wdf.fudoc.dtoconvert.application;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public interface IGenerateVo2Dto {

    void doGenerate(Project project, DataContext dataContext, PsiFile psiFile);

}
