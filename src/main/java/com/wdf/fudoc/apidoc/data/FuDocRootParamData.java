package com.wdf.fudoc.apidoc.data;

import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.pojo.bo.RootParamBO;
import com.wdf.fudoc.apidoc.pojo.data.CommonItemData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-09-27 17:15:32
 */
@Getter
@Setter
public class FuDocRootParamData extends CommonItemData {

    /**
     * 定位api的唯一标识
     */
    private String apiId;

    /**
     * api的方法节点（用于跳转到对应的接口代码中）
     */
    private PsiMethod psiMethod;

    /**
     * 根节点参数集合
     */
    private List<RootParamBO> rootParamBOList;
}
