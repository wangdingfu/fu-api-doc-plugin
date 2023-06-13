package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorEmptyTextPainter;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.LeftRightComponent;
import com.wdf.fudoc.request.po.FuRequestAuthConfigPO;
import icons.FuDocIcons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局鉴权配置tab
 *
 * @author wangdingfu
 * @date 2023-06-07 23:58:29
 */
public class GlobalAuthConfigTab extends DefaultListModel<FuRequestAuthConfigPO> implements FuTab {

    private static final String TITLE = "鉴权配置";


    private final Project project;

    private final JPanel rootPanel;

    private final JPanel emptyPanel;

    private final JComponent rightPanel;

    private final JBList<FuRequestAuthConfigPO> leftComponent;

    private final List<String> itemNameList = Lists.newArrayList();

    private final Map<String, FuRequestAuthConfigPO> dataMap = new ConcurrentHashMap<>();

    public GlobalAuthConfigTab(Project project) {
        this.project = project;
        this.leftComponent = new JBList<>(this);
        this.rightPanel = new JPanel(new BorderLayout());
        this.emptyPanel = FuEditorEmptyTextPainter.createFramePreview();
        this.rightPanel.add(this.emptyPanel, BorderLayout.CENTER);
        this.rootPanel = new LeftRightComponent(this.createPanel(), this.rightPanel).getRootPanel();
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TITLE, FuDocIcons.FU_AUTH, this.rootPanel).builder();
    }


    /**
     * 新增一个节点
     *
     * @param fuRequestAuthConfigPO the component to be added
     */
    @Override
    public void addElement(FuRequestAuthConfigPO fuRequestAuthConfigPO) {
        this.itemNameList.add(fuRequestAuthConfigPO.getName());
        this.dataMap.put(fuRequestAuthConfigPO.getName(), fuRequestAuthConfigPO);
        this.leftComponent.setSelectedValue(fuRequestAuthConfigPO, true);
        super.addElement(fuRequestAuthConfigPO);
    }

    /**
     * 移除鉴权配置
     *
     * @param index the index of the element to removed
     * @return 移除的配置数据
     */
    @Override
    public FuRequestAuthConfigPO remove(int index) {
        if (this.itemNameList.size() >= index) {
            String remove = this.itemNameList.remove(index);
            this.dataMap.remove(remove);
        }
        FuRequestAuthConfigPO remove = super.remove(index);
        if (getSize() == 0) {
            //全部删除了 则需要展示空面板
            switchRightPanel(this.emptyPanel);
        }
        return remove;
    }


    /**
     * 切换右侧面板
     *
     * @param panel 右侧展示的面板
     */
    private void switchRightPanel(JPanel panel) {
        this.rightPanel.removeAll();
        this.rightPanel.repaint();
        this.rightPanel.add(panel, BorderLayout.CENTER);
        this.rightPanel.revalidate();
    }

    /**
     * 创建列表组件面板
     */
    public JPanel createPanel() {
        initAction();
        return ToolbarDecorator.createDecorator(this.leftComponent).setAddAction(anActionButton -> {
            //弹框新增鉴权配置信息

        }).setEditAction(anActionButton -> {
            //弹框编辑鉴权配置信息

        }).createPanel();
    }


    private void initAction() {
        this.leftComponent.addListSelectionListener(e -> {
            //当选中列表中指定项时触发
        });
    }
}
