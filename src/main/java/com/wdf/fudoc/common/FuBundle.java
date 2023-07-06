package com.wdf.fudoc.common;

import com.intellij.DynamicBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @descption:
 * @date 2022-05-30 23:51:37
 */
public class FuBundle extends DynamicBundle {

    private static final FuBundle INSTANCE = new FuBundle(MessageConstants.MESSAGE_BUNDLE);


    public FuBundle(@NotNull String pathToBundle) {
        super(pathToBundle);
    }


    public static String message(String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }
}
