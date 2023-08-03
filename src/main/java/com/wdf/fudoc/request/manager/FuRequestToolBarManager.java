package com.wdf.fudoc.request.manager;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory;
import com.intellij.openapi.project.Project;
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
import com.wdf.fudoc.components.action.FuRequestViewModeAction;
import com.wdf.fudoc.components.action.IssueAction;
import com.wdf.fudoc.request.action.toolbar.CopyCurlAction;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.constants.enumtype.IssueSource;
import com.wdf.fudoc.request.constants.enumtype.ViewMode;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
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
     * 【Fu Request】工具类分组
     */
    private final DefaultActionGroup defaultActionGroup;

    private final FuRequestCallback fuRequestCallback;


    public FuRequestToolBarManager(FuRequestCallback fuRequestCallback) {
        this.fuRequestCallback = fuRequestCallback;
        this.defaultActionGroup = new DefaultActionGroup();
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
        addCommonAction(this.defaultActionGroup);
        return this.defaultActionGroup;
    }


    private void addCommonAction(DefaultActionGroup defaultActionGroup) {

        defaultActionGroup.addSeparator();

        addSyncAction(defaultActionGroup);
        //复制curl命令
        defaultActionGroup.add(new CopyCurlAction(fuRequestCallback));

        if (fuRequestCallback.isWindow()) {
            addConfigServerPortAction(defaultActionGroup);
        }


        //添加保存事件
        defaultActionGroup.add(new AnAction("Save", "Save", AllIcons.Actions.Menu_saveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                RequestTabView requestTabView = fuRequestCallback.getRequestTabView();
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
                        fuRequestCallback.initData(fuHttpRequestData);
                    }
                });
            }
        });
        defaultActionGroup.addSeparator();

        //添加请求状态显示按钮 支持终止请求
        defaultActionGroup.add(new AnAction("Stop", "Stop", AllIcons.Actions.Suspend) {

            @Override
            public void update(@NotNull AnActionEvent e) {
                Presentation presentation = e.getPresentation();
                presentation.setEnabled(fuRequestCallback.getSendStatus());
            }

            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                Presentation presentation = e.getPresentation();
                presentation.setEnabled(false);
                fuRequestCallback.stopHttp();
            }
        });


        //添加设置按钮
        defaultActionGroup.add(new AnAction("Config", "全局配置", AllIcons.General.Settings) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                //展示设置界面
                FuRequestSettingView fuRequestSettingView = new FuRequestSettingView(e.getProject(), fuRequestCallback);
                fuRequestSettingView.setSize(900, 800);
                fuRequestSettingView.show();
            }
        });


        //添加保存事件
        defaultActionGroup.add(new AnAction("Navigation Api", "(Alt+Q)", AllIcons.General.Locate) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                PsiElement psiElement = fuRequestCallback.getPsiElement();
                if (Objects.isNull(psiElement)) {
                    return;
                }
                PsiMethod targetMethod = PsiClassUtils.getTargetMethod(psiElement);
                if (Objects.nonNull(targetMethod)) {
                    //跳转到该方法
                    targetMethod.navigate(true);
                }
            }
        });


        if (!fuRequestCallback.isWindow()) {
            //添加设置按钮
            defaultActionGroup.add(new AnAction("Settings", "Setting", AllIcons.Actions.More) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    DefaultActionGroup viewModeGroup = DefaultActionGroup.createPopupGroup(() -> "窗体展示方式");
                    for (ViewMode value : ViewMode.values()) {
                        viewModeGroup.add(new FuRequestViewModeAction(value));
                    }
                    DefaultActionGroup actionGroup = new DefaultActionGroup();
                    //新增view mode
                    actionGroup.add(viewModeGroup);
                    //新增配置是否自动同步端口号
                    addConfigServerPortAction(actionGroup);
                    //新增提交issue mode
                    addIssueAction(actionGroup);
                    int x = 0, y = 0;
                    InputEvent inputEvent = e.getInputEvent();
                    if (inputEvent instanceof MouseEvent) {
                        MouseEvent mouseEvent = (MouseEvent) inputEvent;
                        x = mouseEvent.getX();
                        y = mouseEvent.getY();
                    }
                    ActionPopupMenu popupMenu =
                            ((ActionManagerImpl) ActionManager.getInstance())
                                    .createActionPopupMenu("fudoc.request.settings", actionGroup, new MenuItemPresentationFactory());
                    popupMenu.getComponent().show(e.getInputEvent().getComponent(), x, y);
                }
            });
        }

        defaultActionGroup.addSeparator();


        //添加帮助文档按钮
        defaultActionGroup.add(new AnAction("Help", "Help", FuDocIcons.FU_DOC) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BrowserUtil.browse(UrlConstants.DOCUMENT);
            }
        });


    }


    private void addIssueAction(DefaultActionGroup defaultActionGroup) {
        DefaultActionGroup issueActionGroup = DefaultActionGroup.createPopupGroup(() -> "提交Issue");
        for (IssueSource value : IssueSource.values()) {
            issueActionGroup.add(new IssueAction(value));
        }
        defaultActionGroup.add(issueActionGroup);
    }

    private void addConfigServerPortAction(DefaultActionGroup defaultActionGroup) {
        //添加同步接口文档事件
        defaultActionGroup.add(new ToggleAction("自动读取Spring环境配置", "", FuDocIcons.SPRING_BOOT) {
            @Override
            public boolean isSelected(@NotNull AnActionEvent e) {
                Project project = e.getProject();
                if (project == null || project.isDisposed()) {
                    return false;
                }
                return FuRequestConfigStorage.get(e.getProject()).readData().isAutoPort();
            }

            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorage.get(e.getProject());
                FuRequestConfigPO fuRequestConfigPO = fuRequestConfigStorage.readData();
                fuRequestConfigPO.setAutoPort(state);
                fuRequestConfigStorage.saveData(fuRequestConfigPO);
            }
        });
    }


    private void addSyncAction(DefaultActionGroup defaultActionGroup) {
        //添加同步接口文档事件
        defaultActionGroup.add(new AnAction("上传接口文档", "", AllIcons.Actions.Upload) {
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
    }


    private void execute(BiConsumer<FuDocContext, PsiClass> consumer) {
        PsiElement psiElement = fuRequestCallback.getPsiElement();
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


}
