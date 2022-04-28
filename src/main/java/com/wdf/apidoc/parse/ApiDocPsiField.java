package com.wdf.apidoc.parse;

import com.intellij.psi.PsiField;
import com.intellij.psi.javadoc.PsiDocComment;
import com.wdf.apidoc.helper.DocCommentParseHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption
 * @Date 2022-04-28 22:20:47
 */
public class ApiDocPsiField extends AbstractApiDocField {

    private final PsiField psiField;

    public ApiDocPsiField(PsiField psiField) {
        this.psiField = psiField;
    }

    @Override
    public String getName() {
        return psiField.getName();
    }

    @Override
    public String getComment() {
        PsiDocComment docComment = psiField.getDocComment();
        if (Objects.nonNull(docComment)) {
            return DocCommentParseHelper.getCommentContent(docComment);
        }
        return StringUtils.EMPTY;
    }
}
