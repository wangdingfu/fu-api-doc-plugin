package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.TabActionBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.constants.enumtype.HeaderScope;
import com.wdf.fudoc.request.pojo.CommonHeader;
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import com.wdf.fudoc.util.ProjectUtils;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 全局请求头维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:47:14
 */
public class GlobalHeaderTab extends AbstractBulkEditTabLinkage<CommonHeader> implements FuTab {

    /**
     * table组件
     */
    private final FuTableComponent<CommonHeader> fuTableComponent;
    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;

    private FuTabComponent fuTabComponent;


    private static final String TITLE = "公共请求头";


    public GlobalHeaderTab() {
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.commonHeaders(), Lists.newArrayList(), CommonHeader.class);
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
    }

    @Override
    public TabInfo getTabInfo() {
        this.fuTabComponent = FuTabComponent.getInstance(TITLE, FuDocIcons.FU_REQUEST_HEADER, fuTableComponent.createPanel()).addBulkEditBar(fuEditorComponent.getMainPanel(), this);
        return this.fuTabComponent.builder();
    }

    @Override
    protected FuTableComponent<CommonHeader> getTableComponent(String title) {
        return this.fuTableComponent;
    }

    @Override
    protected FuEditorComponent getEditorComponent(String title) {
        return this.fuEditorComponent;
    }


    public void initData(List<CommonHeader> dataList) {
        fuTableComponent.setDataList(dataList);
    }


    public List<CommonHeader> getData() {
        TabActionBO tabActionBO = this.fuTabComponent.getDefaultAction();
        if(Objects.nonNull(tabActionBO) && tabActionBO.isSelect()){
            //如果当前是编辑器状态 则需要从编辑器组件同步数据到table组件
            bulkEditToTableData(TITLE);
        }
        //从table组件中获取请求头数据集合
        List<CommonHeader> dataList = fuTableComponent.getDataList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (CommonHeader commonHeader : dataList) {
                if (HeaderScope.CURRENT_PROJECT.getName().equals(commonHeader.getScope())) {
                    Project currProject = ProjectUtils.getCurrProject();
                    List<String> projectIdList = commonHeader.getProjectIdList();
                    if (Objects.isNull(projectIdList)) {
                        projectIdList = Lists.newArrayList();
                        commonHeader.setProjectIdList(projectIdList);
                    }
                    String projectId = currProject.getLocationHash();
                    projectIdList.remove(projectId);
                    Boolean select = commonHeader.getSelect();
                    if (Objects.nonNull(select) && select) {
                        projectIdList.add(projectId);
                    }
                }
            }
        }
        return dataList;
    }
}
