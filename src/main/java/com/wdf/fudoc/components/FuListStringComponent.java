package com.wdf.fudoc.components;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.wdf.fudoc.components.bo.BaseList;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.components.validator.InputExistsValidator;
import com.wdf.fudoc.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * List组件
 *
 * @author wangdingfu
 * @date 2023-03-11 17:32:44
 */
public class FuListStringComponent<T extends BaseList> extends DefaultListModel<String> {

    /**
     * 新增内容弹框label
     */
    private final String label;

    /**
     * 列表组件
     */
    private final JBList<String> jbList;

    /**
     * list组件展示名称的数据集合
     */
    private final List<String> nameList;

    /**
     * 存放实际数据集合
     */
    private final Map<String, T> dataMap;


    /**
     * 每一项的数据class类型
     */
    private final Class<T> clazz;

    /**
     * 选中每一项时触发的事件
     */
    private final FuActionListener<T> listener;


    /**
     * 当前选中项
     */
    private String currentItem;

    public FuListStringComponent(String label, FuActionListener<T> listener, Class<T> clazz) {
        this(label, Lists.newArrayList(), listener, clazz);
    }


    public FuListStringComponent(String label, List<T> dataList, FuActionListener<T> listener, Class<T> clazz) {
        this.label = label;
        this.listener = listener;
        this.jbList = new JBList<>(this);
        this.clazz = clazz;
        this.dataMap = ObjectUtils.listToMap(dataList, T::getName);
        this.nameList = Lists.newArrayList(this.dataMap.keySet());
        this.initListAction();
    }


    private void initListAction() {
        this.jbList.addListSelectionListener(e -> {
            if (StringUtils.isNotBlank(this.currentItem)) {
                T data = dataMap.get(this.currentItem);
                if (Objects.nonNull(data)) {
                    listener.doActionAfter(data);
                }
            }

            //选中列表中的指定项时 触发的事件
            String selectedValue = jbList.getSelectedValue();
            if (StringUtils.isEmpty(selectedValue)) {
                return;
            }
            this.currentItem = selectedValue;
            T data = dataMap.get(this.currentItem);
            if (Objects.nonNull(data)) {
                listener.doAction(data);
            }
        });
    }

    //添加一行数据
    private void addRow(T data) {
        this.nameList.add(data.getName());
        this.dataMap.put(data.getName(), data);
        addElement(data.getName());
        this.jbList.setSelectedValue(data.getName(), true);
    }


    @Override
    public String remove(int index) {
        String remove = super.remove(index);
        this.nameList.remove(index);
        if (StringUtils.isNotBlank(remove)) {
            this.listener.remove(this.dataMap.remove(remove));
        }
        return remove;
    }


    @Override
    public String set(int index, String element) {
        this.nameList.set(index, element);
        return super.set(index, element);
    }

    /**
     * 创建列表组件面板
     */
    public JPanel createPanel() {
        return ToolbarDecorator.createDecorator(this.jbList).setAddAction(anActionButton -> {
            //新增事件
            inputItemName(itemName -> addRow(newInstance(itemName)));
        }).createPanel();
    }


    private void inputItemName(Consumer<String> consumer) {
        String value = Messages.showInputDialog(label, "Input " + label, Messages.getQuestionIcon(), "前置请求", new InputExistsValidator(this.nameList));
        if (StringUtils.isEmpty(value)) {
            return;
        }
        consumer.accept(value);
    }


    /**
     * 实例化表格数据对象
     */
    private T newInstance(String itemName) {
        T data = ReflectUtil.newInstance(this.clazz);
        data.setName(itemName);
        return data;
    }


}
