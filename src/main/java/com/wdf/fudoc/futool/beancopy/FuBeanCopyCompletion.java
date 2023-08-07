package com.wdf.fudoc.futool.beancopy;

import cn.hutool.core.text.StrFormatter;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.search.PsiSearchScopeUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.futool.beancopy.bo.CopyBeanBO;
import com.wdf.fudoc.futool.beancopy.bo.CopyBeanMethodBO;
import com.wdf.fudoc.futool.beancopy.bo.FuCompletion;
import icons.FuDocIcons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-08-06 19:51:08
 */
@Slf4j
public class FuBeanCopyCompletion extends CompletionContributor {

    private static final String TYPE_TEXT = "[Fu Doc]";
    private static final String BEAN_COPY = "beanCopy";
    private static final String BEAN_COPY_CN = " 拷贝对象";

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        PsiElement position = parameters.getPosition();
        PsiReference reference = position.getParent().getFirstChild().getReference();
        if (Objects.isNull(reference)) {
            return;
        }
        boolean isShowBeanCopy = true;
        PsiElement resolve = reference.resolve();
        if (Objects.isNull(resolve) && BEAN_COPY.equals(((PsiReferenceExpressionImpl) reference).getLastChild().getText())) {
            isShowBeanCopy = false;
            resolve = ((PsiReferenceExpressionImpl) ((PsiReferenceExpressionImpl) reference).getFirstChild()).resolve();
        }
        PsiClass psiClass;
        String variableName;
        if (Objects.isNull(resolve) || StringUtils.isBlank(variableName = getVariableName(resolve)) || Objects.isNull(psiClass = findPsiClass(resolve))) {
            return;
        }
        //判断是否为Spring容器对象或则一些固定配置的对象无需copy
        if (!isNeedCopyBean(psiClass)) {
            log.info("对象【{}】无需支持beanCopy", psiClass.getQualifiedName());
            return;
        }
        PrefixMatcher originalMatcher = result.getPrefixMatcher();

        CompletionResultSet completionResultSet = result.withRelevanceSorter(CompletionSorter.defaultSorter(parameters, originalMatcher).weighAfter("fudoc", new FuBeanCopyWeigher()));
        completionResultSet.restartCompletionOnAnyPrefixChange();
        //添加需要拷贝的变量到提示列表中
        if (isShowBeanCopy) {
            result.addElement(buildBeanCopy());
        } else {
            addNeedCopyVariable(parameters, result, new CopyBeanBO(variableName, psiClass, resolve));
        }
        super.fillCompletionVariants(parameters, result);
    }

    private LookupElementBuilder buildBeanCopy() {
        return LookupElementBuilder.create(BEAN_COPY).withIcon(FuDocIcons.FU_DOC).withPresentableText(BEAN_COPY).withItemTextForeground(FuColor.GREEN.color()).appendTailText(BEAN_COPY_CN, false).withTypeText(TYPE_TEXT).bold();
    }


    private boolean isNeedCopyBean(PsiClass psiClass) {
        String qualifiedName = psiClass.getQualifiedName();
        if (StringUtils.isBlank(qualifiedName)) {
            return false;
        }
        if (qualifiedName.startsWith("java.util") || qualifiedName.startsWith("java.lang")) {
            return false;
        }
        return true;
    }


    private void addNeedCopyVariable(CompletionParameters parameters, CompletionResultSet result, CopyBeanBO copyBean) {
        PsiElement position = parameters.getPosition();
        int offset = parameters.getOffset();
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(position, PsiMethod.class);
        if (Objects.isNull(psiMethod)) {
            return;
        }
        List<LookupElement> elementList = Lists.newArrayList();
        //添加当前方法的请求参数变量
        PsiParameterList parameterList = psiMethod.getParameterList();
        PsiParameter[] psiParameters = parameterList.getParameters();
        elementList.addAll(buildLookupElement(Lists.newArrayList(psiParameters), copyBean, offset, false));

        //添加当前方法体的变量
        Collection<PsiVariable> psiVariables = PsiTreeUtil.collectElementsOfType(psiMethod.getBody(), PsiVariable.class);
        elementList.addAll(buildLookupElement(Lists.newArrayList(psiVariables), copyBean, offset, true));
        result.addAllElements(elementList);
    }


    private List<LookupElement> buildLookupElement(List<PsiVariable> variableList, CopyBeanBO copyBeanBO, int offset, boolean isScope) {
        List<LookupElement> variableElementList = Lists.newArrayList();
        for (PsiVariable psiVariable : variableList) {
            if (psiVariable.getTextOffset() > offset) {
                continue;
            }
            if (isScope && !isScope(copyBeanBO.getPsiElement(), psiVariable)) {
                continue;
            }
            PsiClass psiClass = findPsiClass(psiVariable);
            if (Objects.nonNull(psiClass) && isNeedCopyBean(psiClass)) {
                String name = psiVariable.getName();
                variableElementList.add(buildLookupElement(name, psiClass.getName(), new FuCompletion(copyBeanBO, new CopyBeanBO(name, psiClass, psiVariable), 0)));
            }
        }
        return variableElementList;
    }


    private LookupElementBuilder buildLookupElement(String name, String tailText, FuCompletion fuCompletion) {
        return LookupElementBuilder.create(fuCompletion, name).withRenderer(new LookupElementRenderer<>() {
                    @Override
                    public void renderElement(LookupElement element, LookupElementPresentation presentation) {
                        presentation.setIcon(FuDocIcons.FU_DOC);
                        presentation.setItemText(name);
                        presentation.appendTailText("  " + tailText, true);
                        presentation.setTypeText("[Fu Doc]");
                        presentation.setItemTextBold(true);
                    }
                })
                //大小写不敏感
                .withCaseSensitivity(true).withInsertHandler((context, item) -> {
                    Object object = item.getObject();
                    if (object instanceof FuCompletion) {
                        List<String> codeList = copyBean((FuCompletion) object);
                        int offset = context.getTailOffset();
                        int lineNumberCurrent = context.getDocument().getLineNumber(offset);
                        String variableName = fuCompletion.getCopyBean().getVariableName();
                        int startOffset = context.getDocument().getLineStartOffset(lineNumberCurrent);
                        int currentLineStartOffset = offset - variableName.length() - BEAN_COPY.length() - name.length() - 2;
                        int diffOffset = currentLineStartOffset - startOffset;
                        context.getDocument().deleteString(currentLineStartOffset, offset);
                        if (CollectionUtils.isEmpty(codeList)) {
                            FuDocNotification.notifyWarn("没有可以拷贝的字段");
                            return;
                        }
                        int lineStartOffset = context.getDocument().getLineStartOffset(lineNumberCurrent);
                        context.getDocument().insertString(lineStartOffset + diffOffset, StringUtils.join(codeList, fillEmptyString(diffOffset)));
                    }
                });
    }


    private static final String FORMAT = "{}.{}({}.{}());\n";

    private List<String> copyBean(FuCompletion fuCompletionData) {
        List<String> codeList = Lists.newArrayList();
        if (Objects.isNull(fuCompletionData)) {
            return null;
        }
        CopyBeanBO copyBean = fuCompletionData.getCopyBean();
        CopyBeanBO toBean = fuCompletionData.getToBean();
        if (Objects.isNull(copyBean) || Objects.isNull(toBean)) {
            return null;
        }
        Map<String, CopyBeanMethodBO> copyMap = copyBean.initFiled();
        Map<String, CopyBeanMethodBO> toMap = toBean.initFiled();
        String copyVar = copyBean.getVariableName();
        String toVar = toBean.getVariableName();
        toMap.forEach((key, value) -> {
            CopyBeanMethodBO copyMethodBO = copyMap.get(key);
            if (Objects.nonNull(copyMethodBO)) {
                codeList.add(StrFormatter.format(FORMAT, toVar, value.getSetMethod(), copyVar, copyMethodBO.getGetMethod()));
            }
        });
        return codeList;
    }


    private PsiClass findPsiClass(PsiElement psiElement) {
        if (psiElement instanceof PsiParameter) {
            PsiParameter psiParameter = (PsiParameter) psiElement;
            PsiType psiType = psiParameter.getType();
            if (psiType instanceof PsiClassType) {
                return ((PsiClassType) psiType).resolve();
            }
        }
        @NotNull PsiElement[] children = psiElement.getChildren();
        for (PsiElement child : children) {
            if (child instanceof PsiTypeElement) {
                PsiTypeElement psiTypeElement = (PsiTypeElement) child;
                PsiElement firstChild = psiTypeElement.getFirstChild();
                PsiReference reference = firstChild.getReference();
                if (Objects.isNull(reference)) {
                    continue;
                }
                PsiElement resolve = reference.resolve();
                if (resolve instanceof PsiClass) {
                    return (PsiClass) resolve;
                }
            }
        }
        return null;
    }

    private String getVariableName(PsiElement psiElement) {
        if (psiElement instanceof PsiLocalVariable) {
            return ((PsiLocalVariable) psiElement).getName();
        }
        if (psiElement instanceof PsiParameter) {
            return ((PsiParameter) psiElement).getName();
        }
        if (psiElement instanceof PsiField) {
            return ((PsiField) psiElement).getName();
        }
        return null;
    }

    private static String fillEmptyString(int size) {
        return fillString(StringUtils.EMPTY, ' ', size);
    }

    public static String fillString(String string, char character, int size) {
        StringBuilder sb = new StringBuilder(size);
        sb.append(string);
        while (sb.length() < size) {
            sb.insert(0, character);
        }
        return sb.toString();
    }

    public static boolean isScope(PsiElement psiElement, PsiVariable psiVariable) {
        return PsiSearchScopeUtil.isInScope(psiElement.getUseScope(), psiVariable);
    }


}
