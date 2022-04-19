package com.wdf.apidoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @descption: 一键生成API接口文档入口类
 * @author wangdingfu
 * @date 2022-04-16 18:53:05
 */
public class GenApiDocAction extends AnAction {


    /**
     * 在点击右键显示操作栏时 会调用该方法判断是否显示生成接口按钮
     *
     * @param e 当前点击事件
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }


    /**
     * 点击按钮或按下快捷键触发生成API接口文档方法
     *
     * @param e 点击事件
     */
    @Override
    public void actionPerformed(AnActionEvent e) {



    }
}
