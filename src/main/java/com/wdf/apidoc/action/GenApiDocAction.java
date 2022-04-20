package com.wdf.apidoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.wdf.apidoc.context.ApiDocContext;
import com.wdf.apidoc.data.ApiDocData;
import com.wdf.apidoc.parse.ApiDocParse;
import com.wdf.apidoc.parse.ControllerApiDocParse;
import com.wdf.apidoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 一键生成API接口文档入口类
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
        PsiClass psiClass = PsiClassUtils.getPsiClass(e);
        if (Objects.isNull(psiClass)) {
            return;
        }

        ApiDocContext apiDocContext = new ApiDocContext();
        apiDocContext.setProject(e.getProject());
        ApiDocParse apiDocParse = new ControllerApiDocParse();
        ApiDocData parse = apiDocParse.parse(apiDocContext, psiClass, null);
        System.out.println(parse.toString());
    }
}
