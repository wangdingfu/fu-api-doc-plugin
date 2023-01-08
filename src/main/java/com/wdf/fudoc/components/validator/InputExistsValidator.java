package com.wdf.fudoc.components.validator;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.NlsSafe;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * 输入框校验器 校验当前输入框中的内容是否存在
 *
 * @author wangdingfu
 * @date 2023-01-07 02:32:50
 */
public class InputExistsValidator implements InputValidator {
    private Collection<String> itemList;

    public InputExistsValidator(Collection<String> itemList) {
        this.itemList = itemList;
    }

    @Override
    public boolean checkInput(@NlsSafe String inputString) {
        return !StringUtils.isEmpty(inputString) && !itemList.contains(inputString);
    }

    @Override
    public boolean canClose(@NlsSafe String inputString) {
        return this.checkInput(inputString);
    }
}
