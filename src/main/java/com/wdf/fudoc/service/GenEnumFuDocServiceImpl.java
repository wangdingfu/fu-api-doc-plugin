package com.wdf.fudoc.service;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.pojo.context.FuDocContext;
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
        //1、先获取属性集合


        log.info("解析枚举类");
        return null;
    }
}
