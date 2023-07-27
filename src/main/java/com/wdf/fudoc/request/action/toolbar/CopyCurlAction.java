package com.wdf.fudoc.request.action.toolbar;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.request.action.AbstractRequestAction;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.manager.FuCurlManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.util.ClipboardUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 复制curl动作
 *
 * @author wangdingfu
 * @date 2023-07-27 21:07:42
 */
@Slf4j
public class CopyCurlAction extends AbstractRequestAction {

    private FuRequestCallback fuRequestCallback;

    public CopyCurlAction(FuRequestCallback fuRequestCallback) {
        super("Copy Curl", "", AllIcons.Actions.Copy);
        this.fuRequestCallback = fuRequestCallback;
    }

    public CopyCurlAction() {
    }

    @Override
    protected String exceptionMsg() {
        return MessageConstants.NOTIFY_GEN_CURL_FAIL;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (Objects.isNull(this.fuRequestCallback)) {
            super.actionPerformed(e);
        } else {
            genCurl(e.getProject(), getRequestData());
        }
    }

    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        genCurl(e.getProject(), getRequestData(e, psiClass, fuDocContext));
    }

    private void genCurl(Project project, FuHttpRequestData fuHttpRequestData) {
        String curl = FuCurlManager.toCurl(project, fuHttpRequestData);
        //将接口文档内容拷贝至剪贴板
        ClipboardUtil.copyToClipboard(curl);
        //通知curl已经拷贝至剪贴板
        FuDocNotification.notifyInfo(FuBundle.message(MessageConstants.NOTIFY_COPY_CURL_OK));
    }


    private FuHttpRequestData getRequestData() {
        RequestTabView requestTabView = this.fuRequestCallback.getRequestTabView();
        FuHttpRequestData requestData;
        if (Objects.isNull(requestTabView) || Objects.isNull(requestData = requestTabView.getFuHttpRequestData())) {
            return null;
        }
        return requestData;
    }


}
