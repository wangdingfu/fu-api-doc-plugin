/*
  Copyright (C), 2018-2020, ZhangYuanSheng
  FileName: GotoRequest
  Author:   ZhangYuanSheng
  Date:     2020/5/12 16:58
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.wdf.fudoc.search.test;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ThrowableComputable;
import com.wdf.fudoc.search.test.dto.FuApiNavigation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangYuanSheng
 * @version 1.0
 */
@Slf4j
public class GotoRequestContributor implements ChooseByNameContributor {

    private final Module module;

    private List<FuApiNavigation> itemList;

    public GotoRequestContributor(Module module) {
        this.module = module;
    }

    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        log.info("getNames-start");
        long start = System.currentTimeMillis();
        if (itemList == null) {
            try {
                // 必须从read线程访问，耗时不能过长
                itemList = ApplicationManager.getApplication().runReadAction(
                        (ThrowableComputable<List<FuApiNavigation>, Throwable>) () ->
                                FuSearchApiExecutor.getInstance(project).getFuApiNavigationList()
                );
            } catch (Throwable e) {

            }

        }
        List<String> nameList = Lists.newArrayList();
        for (FuApiNavigation fuApiNavigation : itemList) {
            nameList.add(fuApiNavigation.getUrl());
        }
        log.info("getNames-end time:{}ms", System.currentTimeMillis() - start);

        return nameList.toArray(new String[0]);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        log.info("getItemsByName====>name:{},pattern:{}", name, pattern);
        List<NavigationItem> list = new ArrayList<>();
        itemList.stream()
                .filter(item -> item.getUrl() != null && item.getUrl().equals(name))
                .forEach(list::add);
        log.info("getItemsByName===>list:{}", JSONUtil.toJsonStr(list));
        return list.toArray(new NavigationItem[0]);
    }
}
