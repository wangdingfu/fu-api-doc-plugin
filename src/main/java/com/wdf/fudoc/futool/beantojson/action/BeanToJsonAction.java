package com.wdf.fudoc.futool.beantojson.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.apidoc.action.AbstractClassAction;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.data.FuDocData;
import com.wdf.fudoc.apidoc.data.FuDocDataContent;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.service.FuDocService;
import com.wdf.fudoc.futool.beantojson.service.GenObjectJsonServiceImpl;
import com.wdf.fudoc.util.ClipboardUtil;
import com.wdf.fudoc.util.PsiClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Slf4j
public class BeanToJsonAction extends AbstractClassAction {


    @Override
    protected boolean isShow(JavaClassType javaClassType) {
        return JavaClassType.OBJECT.equals(javaClassType);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        long start = System.currentTimeMillis();
        PsiElement targetElement = PsiClassUtils.getTargetElement(e);
        if (Objects.isNull(targetElement)) {
            FuDocNotification.notifyWarn(FuDocMessageBundle.message(MessageConstants.NOTIFY_NOT_FUND_CLASS));
            return;
        }
        //获取当前操作的类
        PsiClass psiClass = PsiClassUtils.getPsiClass(targetElement);
        //向全局上下文中添加Project内容
        FuDocDataContent.setData(FuDocData.builder().event(e).build());

        try {
            FuDocContext fuDocContext = new FuDocContext();
            fuDocContext.setSettingData(FuDocSetting.getSettingData());
            fuDocContext.setTargetElement(targetElement);

            FuDocService fuDocService = new GenObjectJsonServiceImpl();
            String content = fuDocService.genFuDocContent(fuDocContext, psiClass);
            if (StringUtils.isBlank(content)) {
                content = "{\n}";
            }
            //将接口文档内容拷贝至剪贴板
            ClipboardUtil.copyToClipboard(content);
            log.info("【{}】--->【bean to json】完成. 共计耗时{}ms", psiClass.getName(), System.currentTimeMillis() - start);
            //通知接口文档已经拷贝至剪贴板
            FuDocNotification.notifyInfo(FuDocMessageBundle.message(MessageConstants.NOTIFY_TO_JSON_OK, psiClass.getName()));
        } catch (Throwable exception) {
            //发送失败通知
            log.error("【Fu Doc】--->【bean to json】失败", exception);
            FuDocNotification.notifyError(FuDocMessageBundle.message(MessageConstants.NOTIFY_TO_JSON_FAIL));
        } finally {
            FuDocDataContent.remove();
        }
    }
}
