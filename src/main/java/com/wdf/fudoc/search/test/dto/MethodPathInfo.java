package com.wdf.fudoc.search.test.dto;

import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import lombok.Data;

import java.util.List;

/**
 * @author wq
 * @date 2022/5/28
 */
@Data
public class MethodPathInfo {

    private PsiMethod psiMethod;

    private RequestType requestType;

    private String methodPath;

    private String location;

    private String methodDesc;

    private List<String> params;

    public MethodPathInfo(PsiMethod psiMethod, RequestType requestType, String methodPath, String location, String methodDesc, List<String> params) {
        this.psiMethod = psiMethod;
        this.requestType = requestType;
        this.methodPath = methodPath;
        this.location = location;
        this.methodDesc = methodDesc;
        this.params = params;
    }
}
