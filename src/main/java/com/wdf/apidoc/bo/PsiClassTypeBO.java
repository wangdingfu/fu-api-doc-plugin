package com.wdf.apidoc.bo;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangdingfu
 * @description
 * @Date 2022-04-19 20:51:25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PsiClassTypeBO {

    private PsiClass psiClass;

    private PsiType psiType;
}
