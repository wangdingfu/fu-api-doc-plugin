package com.wdf.fudoc;

import com.intellij.DynamicBundle;
import com.wdf.fudoc.constant.MessageConstants;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @descption:
 * @date 2022-05-30 23:51:37
 */
public class FuDocMessageBundle extends DynamicBundle {

    private static final FuDocMessageBundle INSTANCE = new FuDocMessageBundle(MessageConstants.MESSAGE_BUNDLE);


    public FuDocMessageBundle(@NotNull String pathToBundle) {
        super(pathToBundle);
    }


    public static String message(String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }
}
