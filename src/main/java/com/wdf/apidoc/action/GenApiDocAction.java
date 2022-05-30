package com.wdf.apidoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.wdf.apidoc.FuDocNotification;
import com.wdf.apidoc.FuDocRender;
import com.wdf.apidoc.assemble.AssembleServiceExecutor;
import com.wdf.apidoc.constant.MessageConstants;
import com.wdf.apidoc.data.FuDocDataContent;
import com.wdf.apidoc.helper.ServiceHelper;
import com.wdf.apidoc.parse.ApiDocClassParser;
import com.wdf.apidoc.parse.ApiDocClassParserImpl;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.apidoc.util.ClipboardUtil;
import com.wdf.apidoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
        //向全局上下文中添加Project内容
        FuDocDataContent.consumerData(fuDocData -> fuDocData.setProject(e.getProject()));

        //解析java类
        ApiDocClassParser apiDocClassParser = ServiceHelper.getService(ApiDocClassParserImpl.class);
        ClassInfoDesc classInfoDesc = apiDocClassParser.parse(apiDocContext, psiClass, null);

        //组装ApiDocData对象
        List<FuApiDocItemData> resultList = AssembleServiceExecutor.execute(classInfoDesc);


        //将接口文档数据渲染成markdown格式接口文档
        String content = FuDocRender.markdownRender(resultList);

        //将接口文档内容拷贝至剪贴板
        ClipboardUtil.copyToClipboard(content);

        //通知接口文档已经拷贝至剪贴板
        FuDocNotification.notifyInfo(MessageConstants.NOTIFY_COPY_OK);

    }
}
