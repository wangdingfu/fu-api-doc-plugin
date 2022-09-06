package com.wdf.fudoc.dtoconvert.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.wdf.fudoc.dtoconvert.application.IGenerateVo2Dto;
import com.wdf.fudoc.dtoconvert.domain.service.impl.GenerateVo2DtoImpl;
import org.jetbrains.annotations.NotNull;

public class Vo2DtoGenerateAction extends AnAction {

    private IGenerateVo2Dto generateVo2Dto = new GenerateVo2DtoImpl();

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            // 织入代码
            generateVo2Dto.doGenerate(event.getProject(), event.getDataContext(), event.getData(LangDataKeys.PSI_FILE));
        } catch (Exception e) {
            Messages.showErrorDialog(event.getProject(), "请按规：先复制对象后，例如：A a，再光标放到需要织入的对象上，例如：B b！", "错误提示");
        }
    }

}
