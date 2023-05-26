package com.wdf.fudoc.navigation.match;

import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.ide.util.gotoByName.MatchResult;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.FList;
import com.intellij.util.indexing.FindSymbolParameters;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-25 20:24:08
 */
public class FuApiMatcher {
    private static final String UNIVERSAL_SEPARATOR = "\u0000";

    private final Project project;

    private final List<ApiNavigationItem> apiList;

    public FuApiMatcher(Project project, List<ApiNavigationItem> apiList) {
        this.project = project;
        this.apiList = apiList;
    }

    public boolean matchApi(String matchText, ProgressIndicator progressIndicator, Processor<? super FoundItemDescriptor<ApiNavigationItem>> consumer) {
        List<Pair<ApiNavigationItem, MatchResult>> matchApiList = new SmartList<>();
        MinusculeMatcher fullMatcher = getFullMatcher(createParameters(matchText));

        for (ApiNavigationItem apiNavigationItem : apiList) {
            progressIndicator.checkCanceled();
            MatchResult qualifiedResult = matchApi(fullMatcher, apiNavigationItem);
            if (qualifiedResult != null) {
                matchApiList.add(Pair.create(apiNavigationItem, qualifiedResult));
            }
        }
        if (CollectionUtils.isEmpty(matchApiList)) {
            return false;
        }
        if (matchApiList.size() == 1) {
            Pair<ApiNavigationItem, MatchResult> pair = matchApiList.get(0);
            return consumer.process(new FoundItemDescriptor<>(pair.first, 0));
        }
        //根据匹配结果排序
        matchApiList.sort(Comparator.comparing(o -> o.second));
        //将匹配结果按顺序添加到结果列表
        List<FoundItemDescriptor<ApiNavigationItem>> processedItems = ContainerUtil.map(matchApiList, p -> new FoundItemDescriptor<>(p.first, 0));
        return ContainerUtil.process(processedItems, consumer);
    }


    public FindSymbolParameters createParameters(String matchText) {
        GlobalSearchScope searchScope = FindSymbolParameters.searchScopeFor(project, true);
        return new FindSymbolParameters(matchText, matchText, searchScope);
    }


    public static MinusculeMatcher getFullMatcher(FindSymbolParameters parameters) {
        return NameUtil.buildMatcher(buildFullPattern(parameters.getCompletePattern()), NameUtil.MatchingCaseSensitivity.NONE);
    }


    private static String buildFullPattern(String matchText) {
        String fullMatchText = "*" + matchText;
        for (String separator : getSeparators()) {
            fullMatchText = StringUtil.replace(fullMatchText, separator, "*" + UNIVERSAL_SEPARATOR + "*");
        }
        return fullMatchText;
    }


    public static MatchResult matchApi(MinusculeMatcher fullMatcher, @NotNull ApiNavigationItem item) {
        String url = item.getUrl();
        if (StringUtils.isBlank(url)) {
            return null;
        }
        for (String separator : getSeparators()) {
            url = StringUtil.replace(url, separator, UNIVERSAL_SEPARATOR);
        }
        MatchResult matchResult = matchName(fullMatcher, url);
        String rightText = item.getRightText();
        if (Objects.isNull(matchResult) && StringUtils.isNotBlank(rightText)) {
            matchResult = matchName(fullMatcher, rightText);
        }
        return matchResult;
    }


    @Nullable
    private static MatchResult matchName(@NotNull MinusculeMatcher matcher, @NotNull String name) {
        FList<TextRange> fragments = matcher.matchingFragments(name);
        return fragments != null ? new MatchResult(name, matcher.matchingDegree(name, false, fragments), MinusculeMatcher.isStartMatch(fragments)) : null;
    }

    public static String @NotNull [] getSeparators() {
        return new String[]{"/", "?"};
    }
}
