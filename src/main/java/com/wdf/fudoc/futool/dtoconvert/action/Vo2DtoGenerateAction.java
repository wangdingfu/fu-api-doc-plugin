package com.wdf.fudoc.futool.dtoconvert.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.ui.Messages;
import com.wdf.fudoc.common.exception.FuDocException;
import cn.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.futool.dtoconvert.domain.service.impl.GenerateVo2DtoImpl;
import com.wdf.fudoc.futool.dtoconvert.application.IGenerateVo2Dto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Vo2DtoGenerateAction extends AnAction {

    private IGenerateVo2Dto generateVo2Dto = new GenerateVo2DtoImpl();

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!DumbService.isDumb(e.getProject()));
        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            // 织入代码
            generateVo2Dto.doGenerate(event.getProject(), event.getDataContext(), event.getData(LangDataKeys.PSI_FILE));
        } catch (FuDocException fuDocException) {
            log.info("拷贝对象异常", fuDocException);
            FuDocNotification.notifyError(fuDocException.getMessage());
        } catch (Exception e) {
            log.info("拷贝对象异常", e);
            Messages.showErrorDialog(event.getProject(), "请按规：先复制对象后，例如：A a，再光标放到需要织入的对象上，例如：B b！", "错误提示");
        }
    }

}
