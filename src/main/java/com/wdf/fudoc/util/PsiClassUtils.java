package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaShortClassNameIndex;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.pojo.bo.PsiClassTypeBO;
import com.wdf.fudoc.request.manager.FuRequestManager;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 获取psiClass对象
 * @date 2022-04-09 15:12:35
 */
public class PsiClassUtils {


    /**
     * 获取当前光标所在的节点
     *
     * @param e 事件对象
     * @return 光标所在的节点
     */
    public static PsiElement getTargetElement(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        return psiFile.findElementAt(offset);
    }


    /**
     * 获取用户光标当前所在的类
     */
    public static PsiClass getPsiClass(PsiElement psiElement) {
        return PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
    }

    /**
     * 获取当前光标所在的方法
     *
     * @param psiElement 光标所在的节点
     * @return 光标所在的方法对象
     */
    public static PsiMethod getTargetMethod(PsiElement psiElement) {
        if (Objects.nonNull(psiElement)) {
            // 当前方法
            PsiMethod target = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
            return target instanceof SyntheticElement ? null : target;
        }
        return null;
    }

    public static PsiMethod findTargetMethod(PsiClass psiClass, String methodName, String mappingUrl) {
        for (PsiMethod psiMethod : psiClass.getAllMethods()) {
            if (!psiMethod.getName().equals(methodName)) {
                continue;
            }
            //获取方法体标识mapping注解上的url 例如GetMapping PostMapping RequestMapping
            List<String> urlList = findMappingUrlList(psiMethod);
            if (CollectionUtils.isNotEmpty(urlList) && MatchUrlUtils.matchUrl(urlList, mappingUrl)) {
                return psiMethod;
            }
        }
        return null;
    }


    public static List<String> findMappingUrlList(PsiMethod psiMethod) {
        String mapping = FuRequestUtils.getMapping(psiMethod);
        if (FuStringUtils.isBlank(mapping)) {
            return null;
        }
        AnnotationData annotationData = AnnotationUtils.parse(psiMethod.getAnnotation(mapping));
        if (Objects.isNull(annotationData)) {
            return null;
        }
        return annotationData.array().constant().stringValue();
    }


    /**
     * 查找java文件
     *
     * @param project 项目对象
     * @param pkg     java对象路径
     * @return java文件class
     */
    public static PsiClass findJavaFile(Project project, String pkg) {
        return JavaPsiFacade.getInstance(project).findClass(pkg, GlobalSearchScope.allScope(project));
    }


    /**
     * 获取PsiClass
     *
     * @param project        当前项目
     * @param psiTypeElement 对象类型
     * @return 对象的PsiClass
     */
    public static PsiClass getPsiClass(Project project, PsiTypeElement psiTypeElement) {
        if (Objects.isNull(project) || Objects.isNull(psiTypeElement)) {
            return null;
        }
        return JavaPsiFacade.getInstance(project).findClass(psiTypeElement.getType().getCanonicalText(), psiTypeElement.getResolveScope());
    }


    /**
     * 获取指定类的父类的类型
     *
     * @param psiClass 指定类
     * @return 父类的类型
     */
    public static PsiClassTypeBO getSuperClassType(PsiClass psiClass) {
        if (isClass(psiClass) && isClass(psiClass.getSuperClass())) {
            for (PsiClassType superType : psiClass.getSuperTypes()) {
                PsiClass superClass = PsiUtil.resolveClassInType(superType);
                if (Objects.nonNull(superClass) && !superClass.isInterface() && !superClass.isEnum()) {
                    return new PsiClassTypeBO(superClass, superType);
                }
            }
        }
        return null;
    }


    public static PsiClass getController(Project project, String controllerName) {
        Collection<PsiClass> psiClasses = JavaShortClassNameIndex.getInstance().get(controllerName, project, GlobalSearchScope.allScope(project));
        if (CollectionUtils.isNotEmpty(psiClasses)) {
            for (PsiClass psiClass : psiClasses) {
                if (FuDocUtils.isController(psiClass)) {
                    return psiClass;
                }
            }
        }
        return null;
    }


    /**
     * 判断是否为一个java类（不是接口或者枚举或者注解等）
     *
     * @param psiClass class对象
     * @return true-是一个类
     */
    public static boolean isClass(PsiClass psiClass) {
        return !(Objects.isNull(psiClass) || psiClass.isEnum() || psiClass.isInterface() || psiClass.isAnnotationType());
    }


    /**
     * 判断PsiType是否为Void
     *
     * @param psiType java类型
     * @return true 是Void
     */
    public static boolean isVoid(PsiType psiType) {
        if (Objects.isNull(psiType)) {
            return false;
        }
        String canonicalText = psiType.getCanonicalText();
        if (CommonClassNames.JAVA_LANG_VOID.equals(canonicalText)) {
            return true;
        }
        return psiType instanceof PsiPrimitiveType && FuDocConstants.ModifierProperty.VOID.equals(canonicalText);
    }


    /**
     * 获取method的唯一标识
     *
     * @param psiMethod 方法对象
     * @return 定位该方法的唯一标识
     */
    public static String getMethodId(PsiMethod psiMethod) {
        String psiClassName = ((PsiClassImpl) psiMethod.getParent()).getQualifiedName();
        String name = psiMethod.getName();
        List<String> paramTypes = Lists.newArrayList();
        PsiParameterList parameterList = psiMethod.getParameterList();
        for (PsiParameter parameter : parameterList.getParameters()) {
            paramTypes.add(parameter.getType().getCanonicalText());
        }
        return FuRequestManager.getMethodId(psiMethod.getProject(), psiClassName + "#" + name + "(" + FuStringUtils.join(paramTypes, ",") + ")");
    }

    public static String getMethodName(PsiMethod psiMethod) {
        String psiClassName = ((PsiClassImpl) psiMethod.getParent()).getName();
        String name = psiMethod.getName();
        return psiClassName + "#" + name;
    }
}
