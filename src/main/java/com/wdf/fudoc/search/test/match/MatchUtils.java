package com.wdf.fudoc.search.test.match;

import com.google.common.collect.Lists;
import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.ide.util.gotoByName.*;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.proximity.PsiProximityComparator;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.FList;
import com.intellij.util.indexing.FindSymbolParameters;
import com.intellij.util.indexing.IdFilter;
import com.wdf.fudoc.search.test.dto.FuApiNavigation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-05-25 17:59:59
 */
public class MatchUtils {

    private static final String UNIVERSAL_SEPARATOR = "\u0000";


    public boolean matchUrl(String pattern, PsiElement psiElement) {
        //权重比较器
        Comparator<Pair<Object, MatchResult>> weightComparator = new Comparator<>() {
            @SuppressWarnings("unchecked")
            final
            Comparator<Object> modelComparator = new PathProximityComparator(psiElement);

            @Override
            public int compare(Pair<Object, MatchResult> o1, Pair<Object, MatchResult> o2) {
                int result = modelComparator.compare(o1.first, o2.first);
                return result != 0 ? result : o1.second.compareTo(o2.second);
            }
        };
        List<Pair<Object, MatchResult>> sameNameElements = new SmartList<>();
        FindSymbolParameters parameters = createParameters(null, pattern, true);
        MinusculeMatcher fullMatcher = getFullMatcher(parameters, null);

        List<FuApiNavigation> apiList = Lists.newArrayList();
        for (FuApiNavigation fuApiNavigation : apiList) {
            MatchResult qualifiedResult = matchQualifiedName(null, fullMatcher, psiElement);
            if (qualifiedResult != null) {
                sameNameElements.add(Pair.create(psiElement, qualifiedResult));
            }
        }
        sameNameElements.sort(weightComparator);
        List<FoundItemDescriptor<?>> processedItems =
                ContainerUtil.map(sameNameElements, p -> new FoundItemDescriptor<>(p.first, 0));
        if (!ContainerUtil.process(processedItems, null)) {
            return false;
        }
        return true;

    }


    @NotNull
    public static FindSymbolParameters createParameters(@NotNull ChooseByNameViewModel base, @NotNull String pattern, boolean everywhere) {
        ChooseByNameModel model = base.getModel();
        IdFilter idFilter = model instanceof ContributorsBasedGotoByModel ? IdFilter.getProjectIdFilter(
                ((ContributorsBasedGotoByModel) model).getProject(), everywhere) : null;
        GlobalSearchScope searchScope = FindSymbolParameters.searchScopeFor(base.getProject(), everywhere);
        return new FindSymbolParameters(pattern, getNamePattern(base, pattern), searchScope, idFilter);
    }

    @NotNull
    private static String getNamePattern(@NotNull ChooseByNameViewModel base, @NotNull String pattern) {
        String transformedPattern = base.transformPattern(pattern);
        return getNamePattern(base.getModel(), transformedPattern);
    }

    @NotNull
    private static String getNamePattern(@NotNull ChooseByNameModel model, @NotNull String pattern) {
        final String[] separators = model.getSeparators();
        int lastSeparatorOccurrence = 0;
        for (String separator : separators) {
            int idx = pattern.lastIndexOf(separator);
            if (idx == pattern.length() - 1) {  // avoid empty name
                idx = pattern.lastIndexOf(separator, idx - 1);
            }
            lastSeparatorOccurrence = Math.max(lastSeparatorOccurrence, idx == -1 ? idx : idx + separator.length());
        }

        return pattern.substring(lastSeparatorOccurrence);
    }

    @Nullable
    public static MatchResult matchQualifiedName(@NotNull ChooseByNameModel model, @NotNull MinusculeMatcher fullMatcher, @NotNull Object element) {
        String fullName = model.getFullName(element);
        if (fullName == null) return null;

        for (String separator : model.getSeparators()) {
            fullName = StringUtil.replace(fullName, separator, UNIVERSAL_SEPARATOR);
        }
        return matchName(fullMatcher, fullName);
    }


    @Nullable
    private static MatchResult matchName(@NotNull MinusculeMatcher matcher, @NotNull String name) {
        FList<TextRange> fragments = matcher.matchingFragments(name);
        return fragments != null ? new MatchResult(name, matcher.matchingDegree(name, false, fragments), MinusculeMatcher.isStartMatch(fragments)) : null;
    }


    @NotNull
    public static MinusculeMatcher getFullMatcher(@NotNull FindSymbolParameters parameters, @NotNull ChooseByNameViewModel base) {
        String fullRawPattern = buildFullPattern(base, parameters.getCompletePattern());
        String fullNamePattern = buildFullPattern(base, base.transformPattern(parameters.getCompletePattern()));

        return NameUtil.buildMatcherWithFallback(fullRawPattern, fullNamePattern, NameUtil.MatchingCaseSensitivity.NONE);
    }

    @NotNull
    private static String buildFullPattern(@NotNull ChooseByNameViewModel base, @NotNull String pattern) {
        String fullPattern = "*" + removeModelSpecificMarkup(base.getModel(), pattern);
        for (String separator : base.getModel().getSeparators()) {
            fullPattern = StringUtil.replace(fullPattern, separator, "*" + UNIVERSAL_SEPARATOR + "*");
        }
        return fullPattern;
    }


    @NotNull
    private static String removeModelSpecificMarkup(@NotNull ChooseByNameModel model, @NotNull String pattern) {
        if (model instanceof ContributorsBasedGotoByModel) {
            pattern = ((ContributorsBasedGotoByModel) model).removeModelSpecificMarkup(pattern);
        }
        return pattern;
    }

    protected static final class PathProximityComparator implements Comparator<Object> {
        @NotNull
        private final PsiProximityComparator myProximityComparator;

        private PathProximityComparator(@Nullable final PsiElement context) {
            myProximityComparator = new PsiProximityComparator(context);
        }

        private static boolean isCompiledWithoutSource(Object o) {
            return o instanceof PsiCompiledElement && ((PsiCompiledElement) o).getNavigationElement() == o;
        }

        @Override
        public int compare(final Object o1, final Object o2) {
            int rc = myProximityComparator.compare(o1, o2);
            if (rc != 0) return rc;

            int o1Weight = isCompiledWithoutSource(o1) ? 1 : 0;
            int o2Weight = isCompiledWithoutSource(o2) ? 1 : 0;
            return o1Weight - o2Weight;
        }
    }

}
