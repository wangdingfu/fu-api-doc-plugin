package com.wdf.fudoc.components.validator;

import com.intellij.openapi.ui.InputValidatorEx;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.api.base.FuBundle;
import com.wdf.api.constants.MessageConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreateCategoryValidator implements InputValidatorEx {
    //当前选中的项目
    private final List<ApiCategoryDTO> categoryList;
    //错误消息
    private String myErrorText;

    public CreateCategoryValidator(List<ApiCategoryDTO> apiCategoryDTOList) {
        this.categoryList = apiCategoryDTOList;
    }

    @Override
    public @Nullable String getErrorText(String inputString) {
        return myErrorText;
    }

    @Override
    public boolean checkInput(String inputString) {
        if (StringUtils.isNotBlank(inputString)) {
            //校验输入的内容
            if (CollectionUtils.isNotEmpty(categoryList) && categoryList.stream().anyMatch(a -> a.getCategoryName().equals(inputString))) {
                //当前项目中存在 则不创建
                myErrorText = FuBundle.message(MessageConstants.SYNC_API_CREATE_CATEGORY_REPEAT);
                return false;
            }
        }
        myErrorText = null;
        return true;
    }

    @Override
    public boolean canClose(String inputString) {
        return true;
    }
}