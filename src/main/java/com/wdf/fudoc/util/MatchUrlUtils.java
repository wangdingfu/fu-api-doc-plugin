package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.ide.util.gotoByName.MatchResult;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.containers.FList;
import com.intellij.util.indexing.FindSymbolParameters;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-27 15:30:48
 */
public class MatchUrlUtils {

    private static final String UNIVERSAL_SEPARATOR = "\u0000";


    public static MatchResult match(Project project, String pattern, String matchText) {
        MinusculeMatcher fullMatcher = getFullMatcher(project, pattern);
        return matchName(fullMatcher, matchText);
    }

    public static MinusculeMatcher getFullMatcher(Project project, String pattern) {
        return getFullMatcher(createParameters(project, pattern));
    }

    public static FindSymbolParameters createParameters(Project project, String matchText) {
        GlobalSearchScope searchScope = FindSymbolParameters.searchScopeFor(project, true);
        return new FindSymbolParameters(matchText, matchText, searchScope, null);
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
    public static MatchResult matchName(@NotNull MinusculeMatcher matcher, @NotNull String name) {
        FList<TextRange> fragments = matcher.matchingFragments(name);
        return fragments != null ? new MatchResult(name, matcher.matchingDegree(name, false, fragments), MinusculeMatcher.isStartMatch(fragments)) : null;
    }


    public static boolean matchUrl(List<String> mappingUrlList, String httpUrl) {
        List<String> urlList = Lists.newArrayList(httpUrl.split("/"));
        for (String mappingUrl : mappingUrlList) {
            if (mappingUrl.equals(httpUrl)) {
                return true;
            }
            if (isVar(mappingUrl)) {
                String[] split = mappingUrl.split("/");
                if (split.length == urlList.size()) {
                    List<String> mappings = Lists.newArrayList();
                    List<Integer> indexList = Lists.newArrayList();
                    for (int i = 0; i < split.length; i++) {
                        String str = split[i];
                        if (isVar(str)) {
                            indexList.add(i);
                            continue;
                        }
                        mappings.add(str);
                    }
                    String formatMappingUrl = StringUtils.join(mappings, "/");
                    List<String> urls = Lists.newArrayList();
                    for (int i = 0; i < urlList.size(); i++) {
                        if (indexList.contains(i)) {
                            continue;
                        }
                        urls.add(urlList.get(i));
                    }
                    String formatUrl = StringUtils.join(urls, "/");
                    if (formatMappingUrl.equals(formatUrl)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }


    private static boolean isVar(String text) {
        return text.contains("{") && text.contains("}");
    }

    public static String @NotNull [] getSeparators() {
        return new String[]{"/", "?"};
    }

}
