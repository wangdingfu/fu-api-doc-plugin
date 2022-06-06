package com.wdf.apidoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wdf.apidoc.FuDocMessageBundle;
import com.wdf.apidoc.FuDocNotification;
import com.wdf.apidoc.FuDocRender;
import com.wdf.apidoc.assemble.AssembleServiceExecutor;
import com.wdf.apidoc.constant.MessageConstants;
import com.wdf.apidoc.data.FuDocDataContent;
import com.wdf.apidoc.helper.ServiceHelper;
import com.wdf.apidoc.parse.ApiDocClassParser;
import com.wdf.apidoc.parse.ApiDocClassParserImpl;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.AnnotationDataMap;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.apidoc.util.AnnotationUtils;
import com.wdf.apidoc.util.ClipboardUtil;
import com.wdf.apidoc.util.ObjectUtils;
import com.wdf.apidoc.util.PsiClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 一键生成API接口文档入口类
 * @date 2022-04-16 18:53:05
 */
@Slf4j
public class GenApiDocAction extends AnAction {


    /**
     * 在点击右键显示操作栏时 会调用该方法判断是否显示生成接口按钮
     *
     * @param e 当前点击事件
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();

        PsiElement targetElement = PsiClassUtils.getTargetElement(e);
        AnnotationDataMap parse = AnnotationUtils.parse(PsiClassUtils.getPsiClass(targetElement));
        //判断当前类是否为Controller
        if (parse.isController()) {
            //目前FuDoc只支持Controller生成接口文档 后续将开发Feign|Dubbo
            presentation.setEnabledAndVisible(true);
            return;
        }
        presentation.setEnabledAndVisible(false);
    }

    /**
     * 点击按钮或按下快捷键触发生成API接口文档方法
     *
     * @param e 点击事件
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiElement targetElement = PsiClassUtils.getTargetElement(e);
        if (Objects.isNull(targetElement)) {
            FuDocNotification.notifyWarn(FuDocMessageBundle.message(MessageConstants.NOTIFY_NOT_FUND_CLASS));
            return;
        }
        //获取当前操作的类
        PsiClass psiClass = PsiClassUtils.getPsiClass(targetElement);
        ApiDocContext apiDocContext = new ApiDocContext();
        apiDocContext.setProject(e.getProject());
        //向全局上下文中添加Project内容
        FuDocDataContent.consumerData(fuDocData -> fuDocData.setEvent(e));

        try {
            //获取当前操作的方法
            PsiMethod targetMethod = PsiClassUtils.getTargetMethod(targetElement);
            //解析java类
            ApiDocClassParser apiDocClassParser = ServiceHelper.getService(ApiDocClassParserImpl.class);
            ClassInfoDesc classInfoDesc = apiDocClassParser.parse(apiDocContext, psiClass, ObjectUtils.newArrayList(targetMethod));

            //组装ApiDocData对象
            List<FuApiDocItemData> resultList = AssembleServiceExecutor.execute(classInfoDesc);

            //将接口文档数据渲染成markdown格式接口文档
            String content = FuDocRender.markdownRender(resultList);

            //将接口文档内容拷贝至剪贴板
            ClipboardUtil.copyToClipboard(content);

            //通知接口文档已经拷贝至剪贴板
            FuDocNotification.notifyInfo(FuDocMessageBundle.message(MessageConstants.NOTIFY_COPY_OK, psiClass.getName()));
        } catch (Throwable exception) {
            //发送失败通知
            log.error("【Fu Doc】解析生成接口文档失败", exception);
            FuDocNotification.notifyError(FuDocMessageBundle.message(MessageConstants.NOTIFY_GEN_FAIL));
        }

    }
}
