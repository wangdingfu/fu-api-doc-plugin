//package com.wdf.fudoc.request.http.impl;
//
//
//import com.intellij.httpClient.http.request.psi.HttpRequest;
//import com.intellij.openapi.project.Project;
//import com.intellij.psi.PsiClass;
//import com.intellij.psi.PsiMethod;
//import com.wdf.fudoc.request.http.FuHttpClient;
//
///**
// * @author wangdingfu
// * @date 2023-05-21 23:10:52
// */
//public class FuHttpClientImpl implements FuHttpClient {
//
//
//    private final Project project;
//
//    private final HttpRequest httpRequest;
//
//    public FuHttpClientImpl(Project project, HttpRequest httpRequest) {
//        this.project = project;
//        this.httpRequest = httpRequest;
//    }
//
//
//    @Override
//    public Project getProject() {
//        return this.project;
//    }
//
//    @Override
//    public HttpRequest getHttpRequest() {
//        return this.httpRequest;
//    }
//}
