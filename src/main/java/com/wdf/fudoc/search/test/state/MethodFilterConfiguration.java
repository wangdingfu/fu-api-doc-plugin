package com.wdf.fudoc.search.test.state;

import com.intellij.ide.util.gotoByName.ChooseByNameFilterConfiguration;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;

/**
 * MethodFilterConfiguration
 * @author wangdingfu
 */
@State(name = "FuSearchFilter", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class MethodFilterConfiguration extends ChooseByNameFilterConfiguration<RequestType> {

    public static MethodFilterConfiguration getInstance(Project project) {
        return project.getService(MethodFilterConfiguration.class);
    }

    @Override
    protected String nameForElement(RequestType type) {
        return type.name();
    }

}
