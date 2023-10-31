package com.wdf.fudoc.components.validator;

import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.NlsSafe;
import com.wdf.api.base.FuBundle;
import com.wdf.api.constants.MessageConstants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;

/**
 * 输入框校验器 校验当前输入框中的内容是否存在
 *
 * @author wangdingfu
 * @date 2023-01-07 02:32:50
 */
public class InputExistsValidator implements InputValidatorEx {
    private final Collection<String> itemList;
    private String errorTest;

    public InputExistsValidator(Collection<String> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String getErrorText(@NonNls String inputString) {
        return errorTest;
    }

    @Override
    public boolean checkInput(@NlsSafe String inputString) {
        if (StringUtils.isNotBlank(inputString) && itemList.contains(inputString)) {
            errorTest = FuBundle.message(MessageConstants.VALIDATOR_INPUT_REPEAT);
            return false;
        }
        errorTest = null;
        return true;
    }

}
