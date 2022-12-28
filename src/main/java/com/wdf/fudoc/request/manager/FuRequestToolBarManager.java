package com.wdf.fudoc.request.manager;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.request.configurable.FuRequestSettingConfigurable;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.constants.enumtype.RequestDialog;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;
import com.wdf.fudoc.util.FuDocUtils;
import icons.FuDocIcons;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 【Fu Request】模块工具类管理类
 *
 * @author wangdingfu
 * @date 2022-10-14 18:15:30
 */
public class FuRequestToolBarManager {

    /**
     * 【Fu Request】弹窗
     */
    private HttpDialogView httpDialogView;

    /**
     * IDEA工具栏窗体
     */
    private FuRequestWindow fuRequestWindow;

    /**
     * 【Fu Request】工具类分组
     */
    private final DefaultActionGroup defaultActionGroup = new DefaultActionGroup();

    /**
     * pin 状态 默认pin住
     */
    @Getter
    public final AtomicBoolean pinStatus = new AtomicBoolean(true);

    /**
     * 请求窗体方式
     */
    private final RequestDialog requestDialog;

    private final PsiElement psiElement;


    public FuRequestToolBarManager(HttpDialogView httpDialogView) {
        this.httpDialogView = httpDialogView;
        this.requestDialog = RequestDialog.HTTP_DIALOG;
        this.psiElement = httpDialogView.getPsiElement();
    }

    public FuRequestToolBarManager(FuRequestWindow fuRequestWindow) {
        this.fuRequestWindow = fuRequestWindow;
        this.requestDialog = RequestDialog.TOOL_WINDOW;
        this.psiElement = fuRequestWindow.getPsiElement();
    }

    public static FuRequestToolBarManager getInstance(HttpDialogView httpDialogView) {
        return new FuRequestToolBarManager(httpDialogView);
    }

    public static FuRequestToolBarManager getInstance(FuRequestWindow fuRequestWindow) {
        return new FuRequestToolBarManager(fuRequestWindow);
    }

    /**
     * 初始化工具栏
     */
    public DefaultActionGroup initToolBar() {
        if (RequestDialog.HTTP_DIALOG.equals(this.requestDialog)) {
            addActionByHttpDialog();
        }
        if (RequestDialog.TOOL_WINDOW.equals(this.requestDialog)) {
            addCommonAction(fuRequestWindow.getRequestTabView());
        }
        return defaultActionGroup;
    }


    private void addActionByHttpDialog() {
        //添加公共动作
        addCommonAction(this.httpDialogView.getRequestTabView());


        defaultActionGroup.addSeparator();

        //添加pin
        defaultActionGroup.add(new ToggleAction("Pin", "Pin", AllIcons.General.Pin_tab) {
            @Override
            public boolean isSelected(@NotNull AnActionEvent e) {
                return pinStatus.get();
            }

            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                pinStatus.set(state);
            }
        });

        //添加关闭窗口事件
        defaultActionGroup.addAction(new AnAction("Close", "Close", AllIcons.Actions.Cancel) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                httpDialogView.close();
            }
        });

    }


    private void addCommonAction(RequestTabView requestTabView) {
        ActionManager actionManager = ActionManager.getInstance();
        //添加保存事件
        defaultActionGroup.add(new AnAction("Save", "Save", AllIcons.Actions.MenuSaveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
                requestTabView.doSendBefore(fuHttpRequestData);
                //保存当前请求
                FuRequestManager.saveRequest(requestTabView.getProject(), fuHttpRequestData);
            }
        });

        defaultActionGroup.addSeparator();

        //添加刷新事件
        defaultActionGroup.add(new AnAction("Refresh", "Refresh", AllIcons.Actions.Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                if (Objects.nonNull(psiElement)) {
                    PsiMethod psiMethod = (PsiMethod) psiElement.getParent();
                    PsiClass psiClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);
                    if (Objects.isNull(psiClass) || !FuDocUtils.isController(psiClass) || !FuDocUtils.isControllerMethod(psiMethod)) {
                        return;
                    }
                    FuDocContext fuDocContext = new FuDocContext();
                    fuDocContext.setSettingData(FuDocSetting.getSettingData());
                    fuDocContext.setTargetElement(psiElement);
                    FuHttpRequestData fuHttpRequestData = FuHttpRequestDataFactory.build(e.getProject(), psiClass, fuDocContext);
                    if (Objects.nonNull(fuHttpRequestData)) {
                        initData(fuHttpRequestData);
                    }
                }
            }
        });

        //添加请求状态显示按钮 支持终止请求
        AnAction stopAction = actionManager.getAction(RequestConstants.ACTION_REQUEST_TOOLBAR_STOP);
        if (Objects.nonNull(stopAction)) {
            defaultActionGroup.add(stopAction);
        }

        //添加设置按钮
        defaultActionGroup.add(new AnAction("Setting", "Setting", AllIcons.General.Settings) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                //展示设置界面
                ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), FuRequestSettingConfigurable.class);
            }
        });

        defaultActionGroup.addSeparator();


        //添加帮助文档按钮
        defaultActionGroup.add(new AnAction("帮助文档", "Help", FuDocIcons.FU_DOC) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BrowserUtil.browse(UrlConstants.DOCUMENT);
            }
        });

    }


    private void initData(FuHttpRequestData fuHttpRequestData) {
        if (RequestDialog.HTTP_DIALOG.equals(this.requestDialog) && Objects.nonNull(this.httpDialogView)) {
            this.httpDialogView.initData(fuHttpRequestData);
        } else if (RequestDialog.TOOL_WINDOW.equals(this.requestDialog) && Objects.nonNull(this.fuRequestWindow)) {
            this.fuRequestWindow.initData(fuHttpRequestData);
        }
    }


}
