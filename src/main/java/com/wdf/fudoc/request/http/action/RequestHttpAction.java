package com.wdf.fudoc.request.http.action;

import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.request.http.FuRequest;
import com.wdf.fudoc.request.http.dto.HttpRecentDTO;
import com.wdf.fudoc.request.http.helper.FuRequestFactory;
import com.wdf.fudoc.request.http.impl.FuHttpClientImpl;
import com.wdf.fudoc.request.http.impl.FuRequestImpl;
import com.wdf.fudoc.storage.FuRequestStorage;
import com.wdf.fudoc.util.FuRequestUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 发起http请求
 *
 * @author wangdingfu
 * @date 2023-05-19 22:41:12
 */
public class RequestHttpAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FuRequest fuRequest = FuRequestFactory.createFuRequest(e);
        if (Objects.isNull(fuRequest)) {
            return;
        }
        //开始弹框展示http请求窗体
    }


}
