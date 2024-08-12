package com.wdf.fudoc.futool.beantojson.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.common.AbstractClassAction;
import cn.fudoc.common.base.FuBundle;
import cn.fudoc.common.enumtype.FuDocAction;
import cn.fudoc.common.notification.FuDocNotification;
import cn.fudoc.common.constants.MessageConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.service.FuDocService;
import com.wdf.fudoc.futool.beantojson.service.GenObjectJsonServiceImpl;
import com.wdf.fudoc.util.ClipboardUtil;
import lombok.extern.slf4j.Slf4j;
import com.wdf.fudoc.util.FuStringUtils;

@Slf4j
public class BeanToJsonAction extends AbstractClassAction {

    @Override
    protected FuDocAction getAction() {
        return FuDocAction.BEAN_TO_JSON;
    }
    @Override
    protected boolean isShow(JavaClassType javaClassType) {
        return JavaClassType.OBJECT.equals(javaClassType);
    }

    @Override
    protected String exceptionMsg() {
        return MessageConstants.NOTIFY_TO_JSON_FAIL;
    }

    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        long start = System.currentTimeMillis();
        FuDocService fuDocService = new GenObjectJsonServiceImpl();
        String content = fuDocService.genFuDocContent(fuDocContext, psiClass);
        if (FuStringUtils.isBlank(content)) {
            content = "{\n}";
        }
        //将接口文档内容拷贝至剪贴板
        ClipboardUtil.copyToClipboard(content);
        log.info("【{}】--->【bean to json】完成. 共计耗时{}ms", psiClass.getName(), System.currentTimeMillis() - start);
        //通知接口文档已经拷贝至剪贴板
        FuDocNotification.notifyInfo(FuBundle.message(MessageConstants.NOTIFY_TO_JSON_OK, psiClass.getName()));
    }
}
