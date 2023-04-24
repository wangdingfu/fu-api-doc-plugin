package com.wdf.fudoc.components;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.EditableModel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.components.bo.BaseTemplate;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.components.validator.InputExistsValidator;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-02-20 22:35:30
 */
public class EditorListComponent<T extends BaseTemplate> extends DefaultListModel<String>{

    /**
     * 根面板
     */
    @Getter
    private JPanel rootPanel;

    /**
     * 选中时需要执行的事件
     */
    private final FuActionListener<T> listener;

    /**
     * 新增时弹框的label
     */
    private final String label;

    /**
     * 数据集合
     */
    private List<T> dataList;

    /**
     * 列表展示集合
     */
    private JBList<String> jbList;

    /**
     * 和列表绑定的数据对象
     */
    private final Class<T> clazz;

    /**
     * 当前选中
     */
    private String currentItem;

    /**
     * 是否刷新
     */
    private boolean refresh;

    public EditorListComponent(FuActionListener<T> listener, String label, List<T> dataList, Class<T> clazz) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.listener = listener;
        this.label = label;
        this.clazz = clazz;
        this.dataList = Objects.isNull(dataList) ? Lists.newArrayList() : dataList;
        this.init();
    }


    private void init() {
        this.rootPanel.setBorder(JBUI.Borders.emptyBottom(1));
        //初始化toolbar
//        initToolBar();
        //初始化展示的列表
        initList();
    }

    /**
     * 创建面板
     */
    public JPanel createPanel() {
        if (Objects.isNull(this.jbList)) {
            return new JPanel();
        }
        return ToolbarDecorator.createDecorator(this.jbList).setAddAction(anActionButton -> inputItemName("demo", itemName -> {
            T data = newInstance();
            data.setIdentify(itemName);
            dataList.add(data);
            setDataList(dataList);
            setCurrentItem(itemName);
            listener.doAction(data);
        })).createPanel();
    }


    private void initToolBar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        // 复制操作
        actionGroup.add(createCopyAction());
        // 新增操作
        actionGroup.add(createAddAction());
        // 删除动作
        actionGroup.add(createRemoveAction());
        // 向上移动
        actionGroup.add(createMoveUpAction());
        // 向下移动
        actionGroup.add(createMoveDownAction());
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("fudoc.common.edit.list.action", actionGroup, true);
        this.rootPanel.add(actionToolbar.getComponent(), BorderLayout.NORTH);
    }


    private void initList() {
        this.jbList = new JBList<>(this);
        this.jbList.setBackground(UIUtil.getTextFieldBackground());
        this.rootPanel.add(this.jbList, BorderLayout.CENTER);
        this.jbList.addListSelectionListener(e -> {
            if (this.refresh) {
                return;
            }
            String selectedValue = jbList.getSelectedValue();
            if (StringUtils.isEmpty(selectedValue)) {
                return;
            }
            this.currentItem = selectedValue;
            listener.doAction(findByIdentify(selectedValue));
        });
        initData();
    }

    @Override
    public String remove(int index) {
        T remove = this.dataList.remove(index);
        return remove.getIdentify();
    }

    @Override
    public void addElement(String element) {
        T t = newInstance();
        t.setIdentify(element);
        this.dataList.add(t);
    }

    @Override
    public String set(int index, String element) {
        System.out.println("set:"+index+":"+element);
        return super.set(index, element);
    }

    private void initData(){
        this.dataList.forEach(f->add(getSize(),f.getIdentify()));
    }

    private List<String> getAllItem() {
        return this.dataList.stream().map(BaseTemplate::getIdentify).collect(Collectors.toList());
    }


    public T findByIdentify(String identify) {
        return this.dataList.stream().filter(f -> f.getIdentify().equals(identify)).findFirst().orElse(null);
    }


    private AnAction createCopyAction() {
        return new AnAction(AllIcons.Actions.Copy) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                String selectedValue = jbList.getSelectedValue();
                inputItemName(selectedValue + "Copy", itemName -> dataList.stream().filter(item -> item.getIdentify().equals(selectedValue)).findFirst().ifPresent(item -> {
                    T data = BeanUtil.copyProperties(item, clazz);
                    data.setIdentify(itemName);
                    dataList.add(data);
                    setDataList(dataList);
                    setCurrentItem(data.getIdentify());
                    listener.doAction(data);
                }));
            }

            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(!CollectionUtil.isEmpty(dataList) && !StringUtils.isEmpty(currentItem));
            }
        };
    }

    private AnAction createAddAction() {
        return new AnAction(AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                inputItemName("demo", itemName -> {
                    T data = newInstance();
                    data.setIdentify(itemName);
                    dataList.add(data);
                    setDataList(dataList);
                    setCurrentItem(itemName);
                    listener.doAction(data);
                });
            }
        };
    }

    private AnAction createRemoveAction() {
        return new AnAction(AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                dataList.removeIf(item -> item.getIdentify().equals(jbList.getSelectedValue()));
                listener.doAction(dataList.stream().findFirst().orElse(null));
            }

            @Override
            public void update(@NotNull AnActionEvent e) {
                e.getPresentation().setEnabled(!CollectionUtil.isEmpty(dataList) && !StringUtils.isEmpty(currentItem));
            }
        };
    }

    private AnAction createMoveUpAction() {
        return new AnAction(AllIcons.Actions.MoveUp) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                T selectItem = findByIdentify(currentItem);
                int index = dataList.indexOf(selectItem);
                if (index <= 0) {
                    return;
                }
                T target = dataList.remove(index);
                listener.doAction(target);
            }

            @Override
            public void update(@NotNull AnActionEvent e) {
                T selectItem = findByIdentify(currentItem);
                boolean enabled = selectItem != null && dataList.indexOf(selectItem) > 0;
                e.getPresentation().setEnabled(enabled);
            }
        };
    }

    private AnAction createMoveDownAction() {
        return new AnAction(AllIcons.Actions.MoveDown) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                T selectItem = findByIdentify(currentItem);
                int index = dataList.indexOf(selectItem);
                if (index < 0 || index >= dataList.size() - 1) {
                    return;
                }
                T target = dataList.remove(index);
                dataList.add(index + 1, target);
                listener.doAction(target);
            }

            @Override
            public void update(@NotNull AnActionEvent e) {
                T selectItem = findByIdentify(currentItem);
                boolean enabled = selectItem != null && dataList.indexOf(selectItem) < dataList.size() - 1;
                e.getPresentation().setEnabled(enabled);
            }
        };
    }


    private void inputItemName(String initValue, Consumer<String> consumer) {
        String value = Messages.showInputDialog(label, "Input " + label, Messages.getQuestionIcon(), initValue, new InputExistsValidator(getAllItem()));
        if (StringUtils.isEmpty(value)) {
            return;
        }
        consumer.accept(value);
    }


    /**
     * 实例化表格数据对象
     */
    private T newInstance() {
        return ReflectUtil.newInstance(this.clazz);
    }


    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        try {
            this.refresh = true;
            this.jbList.setModel(new CollectionListModel<>(getAllItem()));
        } finally {
            this.refresh = false;
        }
        if (StringUtils.isEmpty(this.currentItem) && CollectionUtils.isNotEmpty(this.dataList)) {
            T data = this.dataList.get(0);
            setCurrentItem(data.getIdentify());
            listener.doAction(data);
        }
    }

    public void setCurrentItem(String currentItem) {
        this.currentItem = currentItem;
        T data = findByIdentify(this.currentItem);
        int index = this.dataList.indexOf(data);
        if (index >= 0) {
            try {
                this.refresh = true;
                this.jbList.setSelectedIndex(index);
            } finally {
                this.refresh = false;
            }
        }
    }


}
