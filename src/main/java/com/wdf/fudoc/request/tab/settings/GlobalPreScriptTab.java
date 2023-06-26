package com.wdf.fudoc.request.tab.settings;

import com.intellij.icons.AllIcons;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.components.FuCmdComponent;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuEditorEmptyTextPainter;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.action.FuFiltersAction;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.components.listener.FuFiltersListener;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import com.wdf.fudoc.request.js.JsExecutor;
import com.wdf.fudoc.request.js.context.FuContext;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalPreScriptPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpCmdView;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.ResourceUtils;
import groovy.util.logging.Slf4j;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 前置操作tab
 *
 * @author wangdingfu
 * @date 2022-12-26 22:38:48
 */
@Slf4j
public class GlobalPreScriptTab implements FuDataTab<FuRequestConfigPO>, FuActionListener<ScriptCmd>, FuFiltersListener<String> {
    private static final Logger logger = Logger.getInstance(GlobalPreScriptTab.class);

    public static final String TITLE = "前置脚本";

    private final JPanel rootPanel;

    private final Project project;

    private final JPanel leftPanel;


    private final FuEditorComponent fuEditorComponent;


    private boolean isEditor = false;


    private List<String> scopeModuleList;

    private final String title;

    private final FuFiltersAction fuFiltersAction;

    private final AtomicBoolean isExecute = new AtomicBoolean(false);

    private ProgressIndicator progressIndicator;

    private HttpCmdView httpCmdView;

    public GlobalPreScriptTab(Project project) {
        this(project, TITLE, null);
    }

    public GlobalPreScriptTab(Project project, String title, FuRequestConfigPO configPO) {
        this.project = project;
        this.title = title;
        this.rootPanel = new JPanel(new BorderLayout());
        this.fuEditorComponent = FuEditorComponent.create(JavaScriptFileType.INSTANCE);
        //当前脚本针对以下module所有的接口生效
        this.scopeModuleList = FuDocUtils.getAllModuleNameList(project);
        this.fuFiltersAction = new FuFiltersAction<>("配置生效Module", this, () -> {
        });
        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.add(FuEditorEmptyTextPainter.createFramePreview(), BorderLayout.CENTER);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createRightPanel(), BorderLayout.CENTER);
        Splitter splitter = new Splitter(false, 0.7F);
        splitter.setFirstComponent(this.leftPanel);
        splitter.setSecondComponent(rightPanel);
        this.rootPanel.add(splitter, BorderLayout.CENTER);
        if (Objects.nonNull(configPO)) {
            initData(configPO);
        }
    }


    /**
     * 右侧指令面板
     */
    private JPanel createRightPanel() {
        FuCmdComponent fuCmdComponent = FuCmdComponent.getInstance(this);
        ScriptCmd.execute((cmdType, list) -> fuCmdComponent.addCmd(cmdType.getDesc(), list));
        fuCmdComponent.addStrut(20);
        JPanel cmdRootPanel = fuCmdComponent.getRootPanel();
        this.httpCmdView = new HttpCmdView(cmdRootPanel, project, "Http请求配置");
        return cmdRootPanel;
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(this.title, FuDocIcons.FU_SCRIPT, this.rootPanel)
                .addAction(new DumbAwareAction("执行脚本", "", AllIcons.Actions.Execute) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        ProgressManager.getInstance().run(new Task.Backgroundable(project, title) {
                            @Override
                            public void run(@NotNull ProgressIndicator indicator) {
                                progressIndicator = indicator;
                                FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(project);
                                FuRequestConfigPO configPO = fuRequestConfigStorage.readData();
                                saveData(configPO);
                                GlobalPreScriptPO globalPreScriptPO = configPO.getPreScriptMap().get(title);
                                String script = globalPreScriptPO.getScript();
                                if (StringUtils.isBlank(script)) {
                                    FuDocNotification.notifyWarn(FuDocMessageBundle.message(MessageConstants.REQUEST_SCRIPT_NO));
                                }
                                //执行脚本
                                try {
                                    JsExecutor.execute(new FuContext(project, configPO, globalPreScriptPO));
                                } catch (Exception e) {
                                    logger.error("执行脚本失败", e);
                                    FuDocNotification.notifyError(FuDocMessageBundle.message(MessageConstants.REQUEST_SCRIPT_EXECUTE_FAIL));
                                }
                                progressIndicator = null;
                                fuRequestConfigStorage.saveData(configPO);
                            }
                        });
                    }
                })
                .addAction(new DumbAwareAction("Stop Execute" + title, "", AllIcons.Actions.Suspend) {
                    @Override
                    public @NotNull ActionUpdateThread getActionUpdateThread() {
                        return ActionUpdateThread.BGT;
                    }

                    @Override
                    public void update(@NotNull AnActionEvent e) {
                        Presentation presentation = e.getPresentation();
                        presentation.setEnabled(isExecute.get());
                    }

                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        //停止执行脚本
                        if (Objects.nonNull(progressIndicator)) {
                            progressIndicator.stop();
                            isExecute.set(false);
                        }
                    }
                })
                .addAction(this.fuFiltersAction).builder();
    }

    @Override
    public void doAction(ScriptCmd scriptCmd) {
        switchPanel();
        if (ScriptCmd.HTTP_CONFIG.equals(scriptCmd)) {
            return;
        }
        String cmd = scriptCmd.getCmd();
        String content = ResourceUtils.readResource("template/auth/" + cmd);
        if (scriptCmd.isReset()) {
            fuEditorComponent.setContent(content);
        } else {
            fuEditorComponent.append(content);
        }
    }


    private void switchPanel() {
        if (!isEditor) {
            isEditor = true;
            switchPanel(this.leftPanel, this.fuEditorComponent.getMainPanel());
        }
    }


    /**
     * 切换面板
     *
     * @param panel 右侧展示的面板
     */
    private void switchPanel(JPanel panel, JPanel switchPanel) {
        panel.removeAll();
        panel.repaint();
        panel.add(switchPanel, BorderLayout.CENTER);
        panel.revalidate();
    }

    /**
     * 初始化数据
     *
     * @param data 数据对象
     */
    @Override
    public void initData(FuRequestConfigPO data) {
        Map<String, GlobalPreScriptPO> preScriptMap = data.getPreScriptMap();
        GlobalPreScriptPO globalPreScriptPO = preScriptMap.get(this.title);
        if (Objects.nonNull(globalPreScriptPO)) {
            List<String> scope = globalPreScriptPO.getScope();
            if (CollectionUtils.isNotEmpty(scope)) {
                this.scopeModuleList = scope;
            }
            String script = globalPreScriptPO.getScript();
            if (StringUtils.isNotBlank(script)) {
                switchPanel();
                this.fuEditorComponent.setContent(script);
            }
            Map<String, FuHttpRequestData> httpRequestDataMap = globalPreScriptPO.getFuHttpRequestDataMap();
            if (MapUtils.isNotEmpty(httpRequestDataMap)) {
                httpRequestDataMap.forEach((key, value) -> this.httpCmdView.addHttp(value));
            }
        }
    }


    /**
     * 将内存数据持久化到硬盘上
     *
     * @param data 持久化对象
     */
    @Override
    public void saveData(FuRequestConfigPO data) {
        Map<String, GlobalPreScriptPO> preScriptMap = data.getPreScriptMap();
        GlobalPreScriptPO globalPreScriptPO = preScriptMap.get(this.title);
        if (Objects.isNull(globalPreScriptPO)) {
            globalPreScriptPO = new GlobalPreScriptPO();
            preScriptMap.put(this.title, globalPreScriptPO);
        }
        globalPreScriptPO.setScript(this.fuEditorComponent.getContent());
        globalPreScriptPO.setScope(this.scopeModuleList);
        globalPreScriptPO.setTitle(this.title);
        globalPreScriptPO.setFuHttpRequestDataMap(this.httpCmdView.getFuHttpRequestDataMap());
    }


    @Override
    public List<String> getAllElements() {
        return FuDocUtils.getAllModuleNameList(project);
    }

    @Override
    public List<String> getSelectedElements() {
        return this.scopeModuleList;
    }

    @Override
    public String getElementText(String data) {
        return data;
    }

    @Override
    public Icon getElementIcon(String data) {
        return AllIcons.Nodes.Module;
    }

    @Override
    public void setSelected(String data, boolean isMark) {
        if (isMark) {
            this.scopeModuleList.add(data);
        } else {
            this.scopeModuleList.remove(data);
        }
    }


    @Override
    public void moveOff() {
        this.fuFiltersAction.exit();
    }
}
