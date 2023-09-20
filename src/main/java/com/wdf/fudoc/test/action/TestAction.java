package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class TestAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(TestAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }


}
