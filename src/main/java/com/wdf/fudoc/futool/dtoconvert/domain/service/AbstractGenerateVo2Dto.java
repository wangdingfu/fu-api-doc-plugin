package com.wdf.fudoc.futool.dtoconvert.domain.service;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaShortClassNameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiField;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.exception.FuDocException;
import com.wdf.fudoc.futool.dtoconvert.application.IGenerateVo2Dto;
import com.wdf.fudoc.futool.dtoconvert.domain.model.GenerateContext;
import com.wdf.fudoc.futool.dtoconvert.domain.model.GetObjConfigDO;
import com.wdf.fudoc.futool.dtoconvert.domain.model.SetObjConfigDO;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractGenerateVo2Dto implements IGenerateVo2Dto {

    protected final String setRegex = "set(\\w+)";
    protected final String getRegex = "get(\\w+)";

    @Override
    public void doGenerate(Project project, DataContext dataContext, PsiFile psiFile) {
        // 1. 获取上下文
        GenerateContext generateContext = this.getGenerateContext(project, dataContext, psiFile);

        // 2. 获取对象的 set 方法集合
        SetObjConfigDO setObjConfigDO = this.getSetObjConfigDO(generateContext);

        // 3. 获取对的的 get 方法集合 【从剪切板获取】
        GetObjConfigDO getObjConfigDO = this.getObjConfigDOByClipboardText(generateContext);

        // 4. 织入代码 set->get
        this.weavingSetGetCode(generateContext, setObjConfigDO, getObjConfigDO);
    }

    protected abstract GenerateContext getGenerateContext(Project project, DataContext dataContext, PsiFile psiFile);

    protected abstract SetObjConfigDO getSetObjConfigDO(GenerateContext generateContext);

    protected abstract GetObjConfigDO getObjConfigDOByClipboardText(GenerateContext generateContext);

    protected abstract void weavingSetGetCode(GenerateContext generateContext, SetObjConfigDO setObjConfigDO, GetObjConfigDO getObjConfigDO);

    protected List<PsiClass> getPsiClassLinkList(PsiClass psiClass) {
        List<PsiClass> psiClassList = new ArrayList<>();
        PsiClass currentClass = psiClass;
        while (null != currentClass && !"Object".equals(currentClass.getName())) {
            psiClassList.add(currentClass);
            currentClass = currentClass.getSuperClass();
        }
        Collections.reverse(psiClassList);
        return psiClassList;
    }

    protected List<String> getMethods(PsiClass psiClass, String regex, String typeStr) {
        PsiMethod[] methods = psiClass.getMethods();
        List<String> methodList = new ArrayList<>();

        // 判断使用了 lombok，需要补全生成 get、set
        if (isUsedLombok(psiClass)) {
            PsiField[] fields = psiClass.getFields();
            for (PsiField psiField : fields) {
                String name = psiField.getName();
                FuDocPsiField fuDocPsiField = new FuDocPsiField(psiField);
                if (fuDocPsiField.hasProperty(FuDocConstants.ModifierProperty.STATIC)
                        || fuDocPsiField.hasProperty(FuDocConstants.ModifierProperty.FINAL)) {
                    continue;
                }
                methodList.add(typeStr + name.substring(0, 1).toUpperCase() + name.substring(1));
            }

            for (PsiMethod method : methods) {
                String methodName = method.getName();
                if (Pattern.matches(regex, methodName) && !methodList.contains(methodName)) {
                    methodList.add(methodName);
                }
            }

            return methodList;
        }

        for (PsiMethod method : methods) {
            String methodName = method.getName();
            if (Pattern.matches(regex, methodName)) {
                methodList.add(methodName);
            }
        }

        return methodList;
    }

    private boolean isUsedLombok(PsiClass psiClass) {
        return null != psiClass.getAnnotation("lombok.Data");
    }


    protected PsiClass searchPsiClass(GenerateContext generateContext, String clazzName) {
        String className = clazzName;
        String parentClassName = FuStringUtils.EMPTY;
        if (clazzName.contains(".")) {
            className = FuStringUtils.substringAfterLast(clazzName, ".");
            parentClassName = FuStringUtils.substringBeforeLast(clazzName, ".");
        }
        Collection<PsiClass> psiClasses = JavaShortClassNameIndex.getInstance().get(className, generateContext.getProject(), GlobalSearchScope.allScope(generateContext.getProject()));
        if (CollectionUtils.isEmpty(psiClasses)) {
            throw new FuDocException("无法获取到目标类【" + clazzName + "】");
        }
        if (psiClasses.size() == 1) {
            return psiClasses.iterator().next();
        }
        //TODO 内部类处理
        List<String> psiClassNameList = findPsiClassName(generateContext, className);
        if (CollectionUtils.isEmpty(psiClassNameList)) {
            return psiClasses.iterator().next();
        }
        for (PsiClass psiClass : psiClasses) {
            if (psiClassNameList.contains(psiClass.getQualifiedName())) {
                return psiClass;
            }
        }
        return psiClasses.iterator().next();
    }

    private List<String> findPsiClassName(GenerateContext generateContext, String clazzName) {
        List<String> importList = generateContext.getImportList();
        List<String> pkgList = importList.stream().filter(f -> f.endsWith("." + clazzName + ";"))
                .map(m -> m.replace("import", "").replace(";", "").trim())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(pkgList)) {
            return pkgList;
        }
        return importList.stream().filter(f -> f.endsWith(".*;")).map(m -> m.replace("import", "").replace("*;", clazzName).trim()).collect(Collectors.toList());
    }


}
