package com.wdf.fudoc.apidoc.service;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.data.FuDocDataContent;
import com.wdf.fudoc.apidoc.helper.EnumParseHelper;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.util.FuStringUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 将枚举类生成接口文档
 *
 * @author wangdingfu
 * @date 2022-07-22 14:52:05
 */
@Slf4j
public class GenEnumFuDocServiceImpl implements FuDocService {


    @Override
    public String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass) {
        String selectedText = FuDocDataContent.getSelectedText();
        String name = psiClass.getName();
        int type = YesOrNo.NO.getCode();
        if (FuStringUtils.isNotBlank(name) && name.equals(selectedText)) {
            type = YesOrNo.YES.getCode();
        }
        return EnumParseHelper.parseEnum(psiClass, type);
    }
}
