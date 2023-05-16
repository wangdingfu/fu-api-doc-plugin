package com.wdf.fudoc.storage.handler;

import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.storage.enumtype.FuStorageType;

/**
 * 持久化http请求数据
 * <p>
 * 持久化文件目录：项目根目录/${FuDocDir}/${moduleName}/${controllerName}/${fileName}.json
 *
 * @author wangdingfu
 * @date 2023-05-15 21:23:46
 */
public class FuRequestStorage extends AbstractFuStorageHandler<FuHttpRequestData> {

    /**
     * http接口对应的java方法
     */
    private final PsiMethod psiMethod;

    private FuRequestStorage(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
    }

    public static FuRequestStorage getInstance(PsiMethod psiMethod) {
        return new FuRequestStorage(psiMethod);
    }

    @Override
    public FuStorageType getType() {
        return FuStorageType.FU_REQUEST;
    }


    @Override
    protected FuHttpRequestData loadDataFromDisk() {

        return null;
    }

    @Override
    protected void writeDataToDisk(FuHttpRequestData data) {

    }


}
