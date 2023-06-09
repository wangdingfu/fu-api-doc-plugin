package com.wdf.fudoc.request.manager;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.sync.SyncFuDocExecutor;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.request.configurable.FuRequestSettingConfigurable;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.constants.enumtype.RequestDialog;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import com.wdf.fudoc.util.ShowSettingUtils;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;

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

    public RequestTabView getRequestTabView() {
        return RequestDialog.HTTP_DIALOG.equals(this.requestDialog)
                ? this.httpDialogView.getRequestTabView()
                : fuRequestWindow.getRequestTabView();
    }

    /**
     * 初始化工具栏
     */
    public DefaultActionGroup initToolBar() {
        if (RequestDialog.HTTP_DIALOG.equals(this.requestDialog)) {
            addCommonAction(this.defaultActionGroup);

        }
        if (RequestDialog.TOOL_WINDOW.equals(this.requestDialog)) {
            addCommonAction(this.defaultActionGroup);
        }
        return defaultActionGroup;
    }


    private void addCommonAction(DefaultActionGroup defaultActionGroup) {
        ActionManager actionManager = ActionManager.getInstance();


        defaultActionGroup.addSeparator();

        //添加同步接口文档事件
        defaultActionGroup.add(new AnAction("同步接口文档-确认弹框", "", FuDocIcons.FU_API_SYNC_DIALOG) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                //获取同步接口文档配置
                FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
                BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
                ApiDocSystem apiDocSystem = ApiDocSystem.getInstance(settingData.getEnable());
                execute((fuDocContext, psiClass) -> {
                    fuDocContext.setSyncDialog(false);
                    //调用同步接口
                    SyncFuDocExecutor.sync(apiDocSystem, enableConfigData, fuDocContext, psiClass);
                    //加载配置
                    FuDocSyncSetting.getInstance().loadState(settingData);
                });
            }
        });

        //添加同步接口文档事件
        defaultActionGroup.add(new AnAction("同步接口文档", "", FuDocIcons.FU_API_SYNC) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                //获取同步接口文档配置
                FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
                BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
                ApiDocSystem apiDocSystem = ApiDocSystem.getInstance(settingData.getEnable());
                execute((fuDocContext, psiClass) -> {
                    //调用同步接口
                    SyncFuDocExecutor.sync(apiDocSystem, enableConfigData, fuDocContext, psiClass);
                    //加载配置
                    FuDocSyncSetting.getInstance().loadState(settingData);
                });
            }
        });

        defaultActionGroup.addSeparator();

        //添加保存事件
        defaultActionGroup.add(new AnAction("Save", "Save", AllIcons.Actions.MenuSaveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                RequestTabView requestTabView = getRequestTabView();
                if (Objects.nonNull(requestTabView)) {
                    FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
                    requestTabView.doSendBefore(fuHttpRequestData);
                    //保存当前请求
                    FuRequestManager.saveRequest(requestTabView.getProject(), fuHttpRequestData);
                }
            }
        });


        //添加刷新事件
        defaultActionGroup.add(new AnAction("Refresh", "Refresh", AllIcons.Actions.Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                execute((fuDocContext, psiClass) -> {
                    FuHttpRequestData fuHttpRequestData = FuHttpRequestDataFactory.build(fuDocContext, psiClass);
                    if (Objects.nonNull(fuHttpRequestData)) {
                        initData(fuHttpRequestData);
                    }
                });
            }
        });
        defaultActionGroup.addSeparator();

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
                FuRequestSettingView fuRequestSettingView = new FuRequestSettingView(e.getProject());
                fuRequestSettingView.setSize(700, 600);
                fuRequestSettingView.show();
            }
        });


        //添加保存事件
        defaultActionGroup.add(new AnAction("定位到该方法", "(Alt+Q)", AllIcons.General.Locate) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                PsiMethod targetMethod = PsiClassUtils.getTargetMethod(psiElement);
                if (Objects.nonNull(targetMethod)) {
                    //跳转到该方法
                    targetMethod.navigate(true);
                }
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


    private void execute(BiConsumer<FuDocContext, PsiClass> consumer) {
        if (Objects.nonNull(psiElement) && Objects.nonNull(consumer)) {
            PsiMethod psiMethod = PsiClassUtils.getTargetMethod(psiElement);
            PsiClass psiClass = PsiClassUtils.getPsiClass(psiElement);
            if (Objects.isNull(psiClass) || !FuDocUtils.isController(psiClass) || !FuDocUtils.isControllerMethod(psiMethod)) {
                return;
            }
            FuDocContext fuDocContext = new FuDocContext();
            fuDocContext.setSettingData(FuDocSetting.getSettingData());
            fuDocContext.setTargetElement(psiElement);
            consumer.accept(fuDocContext, psiClass);
        }
    }


    private void initData(FuHttpRequestData fuHttpRequestData) {
        if (RequestDialog.HTTP_DIALOG.equals(this.requestDialog) && Objects.nonNull(this.httpDialogView)) {
            this.httpDialogView.initData(fuHttpRequestData);
        } else if (RequestDialog.TOOL_WINDOW.equals(this.requestDialog) && Objects.nonNull(this.fuRequestWindow)) {
            this.fuRequestWindow.initData(fuHttpRequestData);
        }
    }


}
