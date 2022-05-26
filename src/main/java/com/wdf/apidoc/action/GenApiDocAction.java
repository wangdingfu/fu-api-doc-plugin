package com.wdf.apidoc.action;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.wdf.apidoc.assemble.ApiDocAssembleService;
import com.wdf.apidoc.assemble.ControllerAssembleService;
import com.wdf.apidoc.config.FreeMarkerConfig;
import com.wdf.apidoc.parse.ApiDocClassParser;
import com.wdf.apidoc.parse.ApiDocClassParserImpl;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.apidoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        ApiDocClassParser apiDocClassParser = new ApiDocClassParserImpl();
        //解析
        ClassInfoDesc classInfoDesc = apiDocClassParser.parse(apiDocContext, psiClass, null);

        //组装ApiDocData对象
        ApiDocAssembleService assembleService = new ControllerAssembleService();
        boolean assemble = assembleService.isAssemble(classInfoDesc);
        System.out.println(assemble);
        List<FuApiDocItemData> resultList = assembleService.assemble(classInfoDesc);
        System.out.println(JSON.toJSONString(resultList));
        for (FuApiDocItemData fuApiDocItemData : resultList) {
            String content = FreeMarkerConfig.generateContent(fuApiDocItemData, "api_doc.ftl");
            System.out.println(content + "\r\n");
        }

    }
}
