package com.wdf.fudoc.request.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.common.AbstractClassAction;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.util.GenFuDocUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 弹出http请求窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:01:04
 */
public class RequestAction extends AbstractClassAction {

    @Override
    protected boolean isShow(JavaClassType javaClassType) {
        return JavaClassType.CONTROLLER.equals(javaClassType);
    }

    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        List<FuDocItemData> fuDocItemDataList = GenFuDocUtils.gen(fuDocContext, psiClass);
        if (CollectionUtils.isEmpty(fuDocItemDataList)) {
            //没有可以请求的方法
            return;
        }
        FuDocItemData fuDocItemData = fuDocItemDataList.get(0);
        //获取当前所属模块
        FuHttpRequestData fuHttpRequestData = FuHttpRequestDataFactory.build(fuDocItemData, psiClass);

        HttpDialogView.popup(e.getProject(), fuHttpRequestData);

    }
}
