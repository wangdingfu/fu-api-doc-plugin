//package com.wdf.fudoc.request.http.action;
//
//import com.intellij.openapi.actionSystem.ActionUpdateThread;
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.wdf.fudoc.request.http.FuRequest;
//import com.wdf.fudoc.request.http.helper.FuRequestFactory;
//import com.wdf.fudoc.request.http.view.FuRequestView;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Objects;
//
///**
// * 发起http请求
// *
// * @author wangdingfu
// * @date 2023-05-19 22:41:12
// */
//public class RequestHttpAction extends AnAction {
//
//    @Override
//    public @NotNull ActionUpdateThread getActionUpdateThread() {
//        return ActionUpdateThread.BGT;
//    }
//
//
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//        FuRequest fuRequest = FuRequestFactory.createFuRequest(e);
//        if (Objects.isNull(fuRequest)) {
//            return;
//        }
//        //开始弹框展示http请求窗体
//        FuRequestView.getInstance(e.getProject(), fuRequest).show();
//    }
//
//
//}
