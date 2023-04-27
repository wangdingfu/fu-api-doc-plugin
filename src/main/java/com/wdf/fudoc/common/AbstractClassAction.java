package com.wdf.fudoc.common;

import com.intellij.openapi.actionSystem.*;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.data.FuDocData;
import com.wdf.fudoc.apidoc.data.FuDocDataContent;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.util.PsiClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-08-18 16:13:45
 */
@Slf4j
public abstract class AbstractClassAction extends AnAction {

    protected boolean isShow(JavaClassType javaClassType) {
        return true;
    }

    protected String exceptionMsg() {
        return MessageConstants.NOTIFY_GEN_FAIL;
    }

    /**
     * 执行动作
     *
     * @param e            动作事件
     * @param psiClass     光标所在的类
     * @param fuDocContext 全局上下文数据对象
     */
    protected abstract void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext);

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
    /**
     * 在点击右键显示操作栏时 会调用该方法判断是否显示生成接口按钮
     *
     * @param e 当前点击事件
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        PsiElement targetElement = PsiClassUtils.getTargetElement(e);
        PsiClass psiClass = PsiClassUtils.getPsiClass(targetElement);
        JavaClassType javaClassType = JavaClassType.get(psiClass);
        if (JavaClassType.isNone(javaClassType)) {
            presentation.setEnabledAndVisible(false);
            return;
        }
        presentation.setEnabledAndVisible(Objects.nonNull(javaClassType) && isShow(javaClassType));
        super.update(e);
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
        FuDocContext fuDocContext = new FuDocContext();
        fuDocContext.setSettingData(FuDocSetting.getSettingData());
        fuDocContext.setTargetElement(targetElement);
        try {
            //执行业务动作
            execute(e, psiClass, fuDocContext);
        } catch (Exception exception) {
            //发送失败通知
            log.info("【Fu Doc】执行动作失败", exception);
            FuDocNotification.notifyError(FuDocMessageBundle.message(exceptionMsg()));
        } finally {
            log.info("【Fu Doc】执行动作完成. 耗时:{}ms", System.currentTimeMillis() - start);
            FuDocDataContent.remove();
        }
    }
}
