package com.wdf.apidoc.bo;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption class对象
 * @Date 2022-04-18 14:26:09
 */
@Getter
@Setter
public class ConvertObjectTypeBO {

    /**
     * 需要转换对象所属的对象class
     */
    private PsiClass psiClass;

    /**
     * 需要转换对象的类型
     */
    private PsiType psiType;

}
