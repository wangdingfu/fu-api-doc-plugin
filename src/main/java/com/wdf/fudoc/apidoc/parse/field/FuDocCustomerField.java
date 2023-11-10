package com.wdf.fudoc.apidoc.parse.field;

import com.intellij.psi.PsiAnnotation;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @Descption 自定义字段
 * @date 2022-06-08 22:20:43
 */
public class FuDocCustomerField extends AbstractFuDocField {

    private final String fieldName;

    private final String fieldComment;

    public FuDocCustomerField(String fieldName, String fieldComment) {
        this.fieldName = fieldName;
        this.fieldComment = fieldComment;
    }

    @Override
    public String getName() {
        return this.fieldName;
    }

    @Override
    public String getComment() {
        return this.fieldComment;
    }

    @Override
    public PsiAnnotation[] getAnnotations() {
        return null;
    }

    @Override
    public boolean hasProperty(String name) {
        return false;
    }

    @Override
    public String getParamType() {
        return StringUtils.EMPTY;
    }
}
