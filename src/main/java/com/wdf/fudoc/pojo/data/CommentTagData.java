package com.wdf.fudoc.pojo.data;

import com.intellij.psi.PsiElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author wangdingfu
 * @Description 注释tag内容对象
 * @Date 2022-08-05 22:16:06
 */
@Getter
@Setter
public class CommentTagData {

    /**
     * 参数名
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 节点对象
     */
    private PsiElement psiElement;

    public CommentTagData() {
    }

    public CommentTagData(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
