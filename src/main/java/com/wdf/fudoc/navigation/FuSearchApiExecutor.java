//package com.wdf.fudoc.navigation;
//
//import com.google.common.collect.Lists;
//import com.intellij.openapi.project.Project;
//import com.intellij.psi.*;
//import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
//import com.intellij.psi.search.GlobalSearchScope;
//import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
//import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
//import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
//import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
//import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
//import com.wdf.fudoc.common.exception.FuDocException;
//import com.wdf.fudoc.navigation.dto.FuApiNavigation;
//import com.wdf.fudoc.navigation.dto.MethodPathInfo;
//import com.wdf.fudoc.util.AnnotationUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author wangdingfu
// * @date 2023-05-24 12:22:55
// */
//@Slf4j
//public class FuSearchApiExecutor {
//
//    private final Project project;
//
//    private FuSearchApiExecutor(Project project) {
//        this.project = project;
//    }
//
//    public static FuSearchApiExecutor getInstance(Project project) {
//        return new FuSearchApiExecutor(project);
//    }
//
//    public List<ApiNavigationItem> getApiList() {
//        List<FuApiNavigation> fuApiNavigationList = getFuApiNavigationList();
//        if (CollectionUtils.isNotEmpty(fuApiNavigationList)) {
//            return fuApiNavigationList.stream().map(this::convertTo).collect(Collectors.toList());
//        }
//        return Lists.newArrayList();
//    }
//
//
//    private ApiNavigationItem convertTo(FuApiNavigation fuApiNavigation) {
//        return new ApiNavigationItem(fuApiNavigation.getPsiElement(), fuApiNavigation.getRequestType(), fuApiNavigation.getUrl(), fuApiNavigation.getMethodPathInfo());
//
//    }
//
//    @NotNull
//    public List<FuApiNavigation> getFuApiNavigationList() {
//        long start = System.currentTimeMillis();
//        List<FuApiNavigation> navigationItemList = new ArrayList<>();
//        String[] supportAnnotations = new String[]{"Controller", "RestController"};
//        Map<String, PsiClass> psiClassMap = new HashMap<>();
//        for (String supportAnnotation : supportAnnotations) {
//            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(supportAnnotation, project, GlobalSearchScope.projectScope(project));
//            for (PsiAnnotation psiAnnotation : psiAnnotations) {
//                PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
//                PsiElement psiElement = psiModifierList.getParent();
//                if (psiElement == null) {
//                    continue;
//                }
//                PsiClass psiClass = (PsiClass) psiElement;
//                psiClassMap.put(psiClass.getQualifiedName(), psiClass);
//            }
//        }
//        for (PsiClass psiClass : psiClassMap.values()) {
//            try {
//                navigationItemList.addAll(getServiceItemList(psiClass));
//            } catch (Exception e) {
//                String classQname = psiClass.getQualifiedName();
//                throw new FuDocException(String.format("扫描接口时出错, 接口类全限定名为: %s; 错误信息为: %s", classQname, e.getMessage()));
//            }
//        }
//        log.info("加载url共计耗时:{}ms", System.currentTimeMillis() - start);
//        return navigationItemList;
//    }
//
//    protected List<FuApiNavigation> getServiceItemList(@NotNull PsiClass psiClass) {
//        List<FuApiNavigation> navigationItemList = new ArrayList<>(2);
//        List<MethodPathInfo> methodPathList = new ArrayList<>(32);
//
//        List<String> classParams = new ArrayList<>();
//        Set<String> classPathSet = new HashSet<>(32);
//
//        PsiAnnotation classRequestMappingAnnotation = psiClass.getAnnotation(AnnotationConstants.REQUEST_MAPPING);
//        AnnotationData annotationData = AnnotationUtils.parse(classRequestMappingAnnotation);
//        // 可能类上不加 @RequestMapping, 则代表 path = "/"
//        if (annotationData != null) {
//            classPathSet.addAll(annotationData.array().constant().stringValue());
//        } else {
//            classPathSet.add("/");
//        }
//
//        String classQname = psiClass.getQualifiedName();
//        PsiMethod[] psiMethods = psiClass.getMethods();
//        for (PsiMethod psiMethod : psiMethods) {
//            try {
//                methodPathList.addAll(getMethodList0(psiMethod, AnnotationConstants.REQUEST_MAPPING, RequestType.ALL, psiClass));
//                methodPathList.addAll(getMethodList0(psiMethod, AnnotationConstants.GET_MAPPING, RequestType.GET, psiClass));
//                methodPathList.addAll(getMethodList0(psiMethod, AnnotationConstants.POST_MAPPING, RequestType.POST, psiClass));
//                methodPathList.addAll(getMethodList0(psiMethod, AnnotationConstants.PUT_MAPPING, RequestType.PUT, psiClass));
//                methodPathList.addAll(getMethodList0(psiMethod, AnnotationConstants.DELETE_MAPPING, RequestType.DELETE, psiClass));
//            } catch (Exception e) {
//                String methodName = psiMethod.getName();
//                String methodQname = classQname + "#" + methodName;
//                throw new FuDocException(String.format("扫描接口时出错, 方法为: %s; 错误信息为: %s", methodQname, e.getMessage()));
//            }
//        }
//
//        for (String classPath : classPathSet) {
//            for (MethodPathInfo methodPathInfo : methodPathList) {
//                PsiMethod psiMethod = methodPathInfo.getPsiMethod();
//                RequestType httpMethod = methodPathInfo.getRequestType();
//                String methodPath = methodPathInfo.getMethodPath();
//                List<String> methodParams = methodPathInfo.getParams();
//
//                if (!classPath.startsWith("/")) {
//                    classPath = "/".concat(classPath);
//                }
//                if (!classPath.endsWith("/")) {
//                    classPath = classPath.concat("/");
//                }
//                if (methodPath.startsWith("/")) {
//                    methodPath = methodPath.substring(1);
//                }
//                // 获取 params 信息, 先从方法的注解取, 取不到则尝试类的注解
//                String param = "";
//                if (CollectionUtils.isNotEmpty(methodParams)) {
//                    param = "?" + String.join("&", methodParams);
//                } else if (CollectionUtils.isNotEmpty(classParams)) {
//                    param = "?" + String.join("&", classParams);
//                }
//                String fullPath = classPath + methodPath + param;
//                navigationItemList.add(new FuApiNavigation(psiMethod, httpMethod, fullPath, methodPathInfo));
//            }
//        }
//        return navigationItemList;
//    }
//
//    @NotNull
//    private List<MethodPathInfo> getMethodList0(@NotNull PsiMethod psiMethod, String qnameOfMapping, RequestType requestType, PsiClass psiClass) {
//        List<MethodPathInfo> methodPathList = new ArrayList<>(8);
//        PsiAnnotation methodMappingAnnotation = psiMethod.getAnnotation(qnameOfMapping);
//        AnnotationData annotationData;
//        if (methodMappingAnnotation != null && Objects.nonNull(annotationData = AnnotationUtils.parse(methodMappingAnnotation))) {
//            List<String> pathList = annotationData.array().constant().stringValue();
//            if (CollectionUtils.isNotEmpty(pathList)) {
//                for (String methodPath : pathList) {
//                    String psiClassName = psiClass.getName();
//                    String psiMethodName = psiMethod.getName();
//                    String location = psiClassName + "#" + psiMethodName;
//                    ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(psiMethod.getDocComment());
//                    String description = apiDocCommentData.getCommentTitle();
//                    methodPathList.add(new MethodPathInfo(psiMethod, requestType, methodPath, location, description, Lists.newArrayList()));
//                }
//            }
//        }
//        return methodPathList;
//    }
//
//
//}
