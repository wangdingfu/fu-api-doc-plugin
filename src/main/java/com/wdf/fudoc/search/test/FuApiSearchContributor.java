package com.wdf.fudoc.search.test;

import com.intellij.ide.actions.SearchEverywherePsiRenderer;
import com.intellij.ide.actions.bigPopup.ShowFilterAction;
import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.ide.actions.searcheverywhere.PersistentSearchEverywhereContributorFilter;
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereManager;
import com.intellij.ide.actions.searcheverywhere.WeightedSearchEverywhereContributor;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider;
import com.intellij.idea.ActionsBundle;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.speedSearch.SpeedSearchUtil;
import com.intellij.util.Processor;
import com.intellij.util.PsiNavigateUtil;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.search.test.dto.FuApiNavigation;
import com.wdf.fudoc.search.test.state.MethodFilterConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-05-24 11:51:02
 */
@Slf4j
public class FuApiSearchContributor implements WeightedSearchEverywhereContributor<FuApiNavigation> {
    private final Project project;
    private final AnActionEvent event;
    private List<FuApiNavigation> navItemList;
    private PersistentSearchEverywhereContributorFilter<RequestType> myFilter;
    private DefaultChooseByNameItemProvider provider = new DefaultChooseByNameItemProvider(null);
    private RequestFilteringGotoByModel model;

    public FuApiSearchContributor(@NotNull AnActionEvent event) {
        this.project = event.getProject();
        this.event = event;
        ChooseByNameContributor[] contributors = {
                new GotoRequestContributor(event.getData(LangDataKeys.MODULE)),
        };

        model = new RequestFilteringGotoByModel(project, contributors);
        MethodFilterConfiguration methodFilterConfiguration = MethodFilterConfiguration.getInstance(project);
        if (methodFilterConfiguration != null) {
            myFilter = new PersistentSearchEverywhereContributorFilter<>(
                    Arrays.asList(RequestType.values()), methodFilterConfiguration, Enum::name, RequestType::getIcon);
        }
    }

    @Override
    public void fetchWeightedElements(@NotNull String pattern, @NotNull ProgressIndicator progressIndicator, @NotNull Processor<? super FoundItemDescriptor<FuApiNavigation>> consumer) {
        try {
            if (isDumbAware() || !shouldProvideElements(pattern)) {
                return;
            }
            ChooseByNamePopup popup = ChooseByNamePopup.createPopup(project, model, provider);
            provider.filterElements(popup, pattern, true, progressIndicator, o -> {
                if (progressIndicator.isCanceled()) return false;
                if (o == null) {
                    return true;
                }
                if (o instanceof FuApiNavigation) {
                    FuApiNavigation restItem = (FuApiNavigation) o;
                    log.info("根据接口地址【{}】匹配接口【{}】成功", pattern, restItem.getUrl());
                    if (!consumer.process(new FoundItemDescriptor<>(restItem, 0))) {
                        return false;
                    }
                }
                return true;
            });
//            Set<RequestType> httpMethodSet = new HashSet<>();
//            if (myFilter == null) {
//                httpMethodSet.addAll(Arrays.asList(RequestType.values()));
//            } else {
//                httpMethodSet.addAll(myFilter.getSelectedElements());
//            }
//            boolean selectAll = httpMethodSet.size() == RequestType.values().length;
//
//            log.info("fetchWeightedElements====>pattern:{}", pattern);
//
//            MinusculeMatcher matcher = NameUtil.buildMatcher("*" + pattern + "*", NameUtil.MatchingCaseSensitivity.NONE);
//
//            // 从ALL -> URL Tab或快捷键进入时列表为空
//            if (navItemList == null) {
//                // 必须从read线程访问，耗时不能过长
//                navItemList = ApplicationManager.getApplication().runReadAction(
//                        (ThrowableComputable<List<FuApiNavigation>, Throwable>) () ->
//                                FuSearchApiExecutor.getInstance(project).getFuApiNavigationList()
//                );
//            }
//            if (navItemList != null) {
//                for (FuApiNavigation restItem : navItemList) {
//                    if (selectAll || httpMethodSet.contains(restItem.getRequestType())) {
//                        String methodDesc = restItem.getMethodPathInfo().getMethodDesc();
//                        if (matcher.matches(restItem.getUrl()) || (StringUtils.isNotBlank(methodDesc) && matcher.matches(methodDesc))) {
//                            log.info("根据接口地址【{}】匹配接口【{}】成功", pattern, restItem.getUrl());
//                            if (!consumer.process(new FoundItemDescriptor<>(restItem, 0))) {
//                                return;
//                            }
//                        }
//                    }
//
//                }
//            }
        } catch (Throwable ex) {
        }
    }

    @Override
    public @NotNull String getSearchProviderId() {
        return FuDocConstants.SearchApi.TITLE;
    }

    @Override
    public @NotNull @Nls String getGroupName() {
        return FuDocConstants.SearchApi.TITLE;
    }

    @Override
    public @Nullable @Nls String getAdvertisement() {
        return DumbService.isDumb(project) ? "The project is being indexed..." : "type url to search";
    }

    @Override
    public int getSortWeight() {
        return 800;
    }

    @Override
    public boolean showInFindResults() {
        return false;
    }

    @Override
    public boolean processSelectedItem(@NotNull FuApiNavigation selected, int modifiers, @NotNull String searchText) {
        PsiNavigateUtil.navigate(selected.getPsiElement());
        return true;
    }

    @Override
    public boolean isDumbAware() {
        return DumbService.isDumb(project);
    }

    @Override
    public @NotNull ListCellRenderer<? super FuApiNavigation> getElementsRenderer() {
        return new SearchEverywherePsiRenderer(this) {

            @Override
            protected boolean customizeNonPsiElementLeftRenderer(ColoredListCellRenderer renderer, JList list, Object value, int index, boolean selected, boolean hasFocus) {
                try {
                    Color fgColor = list.getForeground();
                    Color bgColor = UIUtil.getListBackground();
                    SimpleTextAttributes nameAttributes = new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, fgColor);

                    ItemMatchers itemMatchers = getItemMatchers(list, value);
                    FuApiNavigation fuApiNavigation = (FuApiNavigation) value;
                    String name = fuApiNavigation.getUrl();
                    String locationString = " " + fuApiNavigation.getRightText();

                    SpeedSearchUtil.appendColoredFragmentForMatcher(name, renderer, nameAttributes, itemMatchers.nameMatcher, bgColor, selected);
                    renderer.setIcon(fuApiNavigation.getRequestType().getIcon());

                    if (StringUtils.isNotEmpty(locationString)) {
                        FontMetrics fm = list.getFontMetrics(list.getFont());
                        int maxWidth = list.getWidth() - fm.stringWidth(name) - myRightComponentWidth - 36;
                        int fullWidth = fm.stringWidth(locationString);
                        if (fullWidth < maxWidth) {
                            SpeedSearchUtil.appendColoredFragmentForMatcher(locationString, renderer, SimpleTextAttributes.GRAYED_ATTRIBUTES, itemMatchers.nameMatcher, bgColor, selected);
                        } else {
                            int adjustedWidth = Math.max(locationString.length() * maxWidth / fullWidth - 1, 3);
                            locationString = StringUtil.trimMiddle(locationString, adjustedWidth);
                            SpeedSearchUtil.appendColoredFragmentForMatcher(locationString, renderer, SimpleTextAttributes.GRAYED_ATTRIBUTES, itemMatchers.nameMatcher, bgColor, selected);
                        }
                    }
                    return true;
                } catch (Throwable ex) {
                    log.error("Search Api", ex);
                    return false;
                }
            }
        };
    }

    @Override
    public @Nullable Object getDataForItem(@NotNull FuApiNavigation element, @NotNull String dataId) {
        return null;
    }

    @Override
    public boolean isEmptyPatternSupported() {
        return true;
    }

    @Override
    public boolean isShownInSeparateTab() {
        return true;
    }

    @Override
    public @NotNull List<AnAction> getActions(@NotNull Runnable onChanged) {
        // 获取 Filter 的 action
        if (project == null || myFilter == null) {
            return Collections.emptyList();
        }
        ArrayList<AnAction> result = new ArrayList<>();
        result.add(new FiltersAction(myFilter, onChanged));
        return result;
    }

    /**
     * 判断是否应该返回列表元素
     *
     * @param pattern 搜索词
     */
    private boolean shouldProvideElements(String pattern) {
        if (StringUtils.isNotBlank(pattern)) {
            return true;
        }
        SearchEverywhereManager seManager = SearchEverywhereManager.getInstance(project);
        if (seManager.isShown()) {
            // 非 All Tab
            return getSearchProviderId().equals(seManager.getSelectedTabID());
        } else {
            // ALL Tab
            return !ActionsBundle.message("action.SearchEverywhere.text").equals(event.getPresentation().getText());
        }
    }

    static class FiltersAction extends ShowFilterAction {

        final PersistentSearchEverywhereContributorFilter<?> filter;
        final Runnable rebuildRunnable;

        FiltersAction(@NotNull PersistentSearchEverywhereContributorFilter<?> filter, @NotNull Runnable rebuildRunnable) {
            this.filter = filter;
            this.rebuildRunnable = rebuildRunnable;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        protected boolean isActive() {
            return filter.getAllElements().size() != filter.getSelectedElements().size();
        }

        @Override
        protected ElementsChooser<?> createChooser() {
            return createChooser(filter, rebuildRunnable);
        }

        private static <T> ElementsChooser<T> createChooser(@NotNull PersistentSearchEverywhereContributorFilter<T> filter, @NotNull Runnable rebuildRunnable) {
            ElementsChooser<T> res = new ElementsChooser<T>(filter.getAllElements(), false) {
                @Override
                protected String getItemText(@NotNull T value) {
                    return filter.getElementText(value);
                }

                @Nullable
                @Override
                protected Icon getItemIcon(@NotNull T value) {
                    return filter.getElementIcon(value);
                }
            };
            res.markElements(filter.getSelectedElements());
            ElementsChooser.ElementsMarkListener<T> listener = (element, isMarked) -> {
                filter.setSelected(element, isMarked);
                rebuildRunnable.run();
            };
            res.addElementsMarkListener(listener);
            return res;
        }

    }


}
