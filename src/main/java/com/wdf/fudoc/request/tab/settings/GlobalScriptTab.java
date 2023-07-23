package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.components.*;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.components.listener.FuStatusLabelListener;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmdType;
import com.wdf.fudoc.request.constants.enumtype.ScriptType;
import com.wdf.fudoc.request.js.JsExecutor;
import com.wdf.fudoc.request.js.context.FuContext;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalPreScriptPO;
import com.wdf.fudoc.request.pojo.BasePopupMenuItem;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpCmdView;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.fudoc.spring.SpringConfigFileConstants;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.ResourceUtils;
import com.wdf.fudoc.util.ToolBarUtils;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 前置操作tab
 *
 * @author wangdingfu
 * @date 2022-12-26 22:38:48
 */
@Slf4j
public class GlobalScriptTab implements FuDataTab<FuRequestConfigPO>, FuActionListener<ScriptCmd>, FuStatusLabelListener {
    private static final Logger logger = Logger.getInstance(GlobalScriptTab.class);

    private final JPanel rootPanel;

    private final Project project;

    private final JPanel leftPanel;
    private final JPanel rightPanel;


    private final FuEditorComponent fuEditorComponent;


    private boolean isEditor = false;

    private final AtomicBoolean isExecute = new AtomicBoolean(false);

    private ProgressIndicator progressIndicator;

    private HttpCmdView httpCmdView;

    private String application;

    private final ScriptType scriptType;

    public GlobalScriptTab(Project project, ScriptType scriptType, Disposable disposable) {
        this.project = project;
        this.scriptType = scriptType;
        this.rootPanel = new JPanel(new BorderLayout());
        this.fuEditorComponent = FuEditorComponent.create(JavaScriptFileType.INSTANCE, disposable);
        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.add(FuEditorEmptyTextPainter.createFramePreview(), BorderLayout.CENTER);
        this.rightPanel = new JPanel(new BorderLayout());
        Splitter splitter = new Splitter(false, 0.8F);
        splitter.setFirstComponent(this.leftPanel);
        splitter.setSecondComponent(this.rightPanel);
        this.application = getApplication();
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }

    private String getApplication() {
        if (StringUtils.isBlank(this.application)) {
            Set<String> applicationList = SpringBootEnvLoader.getApplication(project);
            return CollectionUtils.isEmpty(applicationList) ? SpringConfigFileConstants.APPLICATION : applicationList.iterator().next();
        }
        return this.application;
    }


    @Override
    public TabInfo getTabInfo() {
        //构建右侧环境组件
        JPanel slidePanel = new JPanel(new BorderLayout());
        DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
        defaultActionGroup.addAction(new DumbAwareAction(FuBundle.message("fudoc.script.execute.title"), "", AllIcons.Actions.Execute) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ProgressManager.getInstance().run(new Task.Backgroundable(project, scriptType.getView()) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorage.get(project);
                        FuRequestConfigPO configPO = fuRequestConfigStorage.readData();
                        saveData(configPO);
                        GlobalPreScriptPO globalPreScriptPO = configPO.getScript(scriptType).get(application);
                        if (Objects.isNull(globalPreScriptPO)) {
                            FuDocNotification.notifyWarn(FuBundle.message(MessageConstants.REQUEST_SCRIPT_NO));
                            return;
                        }
                        String script = globalPreScriptPO.getScript();
                        if (StringUtils.isBlank(script)) {
                            FuDocNotification.notifyWarn(FuBundle.message(MessageConstants.REQUEST_SCRIPT_NO));
                            return;
                        }
                        progressIndicator = indicator;
                        isExecute.set(true);
                        FuConsole fuConsole = FuConsoleManager.get(project);
                        //执行脚本
                        try {
                            JsExecutor.execute(new FuContext(project, configPO, globalPreScriptPO), fuConsole);
                        } catch (Exception e) {
                            logger.error("执行脚本失败", e);
                            FuDocNotification.notifyError(FuBundle.message(MessageConstants.REQUEST_SCRIPT_EXECUTE_FAIL));
                        } finally {
                            progressIndicator = null;
                            isExecute.set(false);
                        }
                    }
                });
            }
        });
        defaultActionGroup.addAction(new DumbAwareAction("Stop Execute" + scriptType.getView(), "", AllIcons.Actions.Suspend) {
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
        });
        ToolBarUtils.addActionToToolBar(slidePanel, "fudoc.request.config.script", defaultActionGroup, BorderLayout.CENTER);
        FuStatusLabel fuStatusLabel = new FuStatusLabel(null, FuDocIcons.SPRING_BOOT, this);
        fuStatusLabel.setText(getList().size() > 1 ? application : null);
        slidePanel.add(fuStatusLabel.getLabel(), BorderLayout.EAST);
        return FuTabComponent.getInstance(this.scriptType.getView(), FuDocIcons.FU_SCRIPT, this.rootPanel).builder(slidePanel);
    }

    @Override
    public void doAction(ScriptCmd scriptCmd) {
        switchPanel();
        if (ScriptCmd.ADD_HTTP_CONFIG.equals(scriptCmd)) {
            httpCmdView.addHttp();
            return;
        }
        String cmd = scriptCmd.getCmd();
        if (StringUtils.isBlank(cmd)) {
            return;
        }
        String content = ScriptCmdType.LOG.equals(scriptCmd.getCmdType())
                ? cmd
                : ResourceUtils.readResource("template/auth/" + cmd);
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
    private void switchPanel(JPanel panel, JComponent switchPanel) {
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
        initScript(data);
    }


    /**
     * 初始化脚本配置
     *
     * @param fuRequestConfigPO 配置数据
     */
    private void initScript(FuRequestConfigPO fuRequestConfigPO) {
        if (Objects.isNull(fuRequestConfigPO)) {
            fuRequestConfigPO = FuRequestConfigStorage.get(project).readData();
        }
        this.httpCmdView = new HttpCmdView(this.project);
        if (StringUtils.isBlank(this.application)) {
            this.application = getApplication();
        }

        Map<String, GlobalPreScriptPO> preScriptMap = fuRequestConfigPO.getScript(scriptType);
        GlobalPreScriptPO globalPreScriptPO = preScriptMap.get(this.application);
        if (Objects.isNull(globalPreScriptPO)) {
            globalPreScriptPO = new GlobalPreScriptPO();
            globalPreScriptPO.setApplication(this.application);
            preScriptMap.put(this.application, globalPreScriptPO);
        }

        Map<String, FuHttpRequestData> httpRequestDataMap = globalPreScriptPO.getFuHttpRequestDataMap();
        if (MapUtils.isNotEmpty(httpRequestDataMap)) {
            httpRequestDataMap.keySet().stream().sorted().forEach(f -> this.httpCmdView.addHttp(f, httpRequestDataMap.get(f)));
        } else {
            this.httpCmdView.addHttp();
        }

        String script = globalPreScriptPO.getScript();
        if (StringUtils.isNotBlank(script)) {
            this.isEditor = false;
            switchPanel();
            this.fuEditorComponent.setContent(script);
        } else {
            switchPanel(this.leftPanel, FuEditorEmptyTextPainter.createFramePreview());
        }

        switchPanel(this.rightPanel, createRightPanel());
    }


    /**
     * 右侧指令面板
     */
    private JComponent createRightPanel() {
        FuCmdComponent fuCmdComponent = FuCmdComponent.getInstance(this);
        ScriptCmd.execute((cmdType, list) -> fuCmdComponent.addCmd(cmdType.getDesc(), list, ScriptCmdType.HTTP.equals(cmdType) ? httpCmdView.getVerticalBox() : null));
        fuCmdComponent.addStrut(20);
        return fuCmdComponent.getVerticalBox();
    }


    /**
     * 将内存数据持久化到硬盘上
     *
     * @param data 持久化对象
     */
    @Override
    public void saveData(FuRequestConfigPO data) {
        Map<String, GlobalPreScriptPO> preScriptMap = data.getScript(this.scriptType);
        GlobalPreScriptPO globalPreScriptPO = preScriptMap.get(this.application);
        if (Objects.isNull(globalPreScriptPO)) {
            globalPreScriptPO = new GlobalPreScriptPO();
            preScriptMap.put(this.application, globalPreScriptPO);
        }
        globalPreScriptPO.setScript(this.fuEditorComponent.getContent());
        globalPreScriptPO.setApplication(this.application);
        globalPreScriptPO.setFuHttpRequestDataMap(this.httpCmdView.getFuHttpRequestDataMap());
    }


    @Override
    public List<BasePopupMenuItem> getList() {
        Set<String> application = SpringBootEnvLoader.getApplication(project);
        if (CollectionUtils.isEmpty(application)) {
            return Lists.newArrayList();
        }
        return application.stream().map(m -> new BasePopupMenuItem(FuDocIcons.SPRING_BOOT, m)).collect(Collectors.toList());
    }

    @Override
    public void select(String text) {
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(project).readData();
        saveData(fuRequestConfigPO);
        this.application = text;
        initScript(fuRequestConfigPO);
    }

}
