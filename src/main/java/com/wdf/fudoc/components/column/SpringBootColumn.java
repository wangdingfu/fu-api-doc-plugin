package com.wdf.fudoc.components.column;

import com.wdf.fudoc.common.base.FuFunction;
import com.wdf.fudoc.spring.SpringConfigManager;
import com.wdf.fudoc.util.ProjectUtils;
import icons.FuDocIcons;
import jdk.jfr.StackTrace;
import lombok.Getter;

import java.util.function.BiConsumer;

/**
 * @author wangdingfu
 * @date 2023-07-20 22:16:39
 */
@Getter
@StackTrace
public class SpringBootColumn<T> extends ComboBoxColumn<T> {

    public SpringBootColumn(String name, FuFunction<T, String> getFun, BiConsumer<T, String> setFun) {
        super(name, FuDocIcons.SPRING, getFun, setFun, SpringConfigManager.getApplicationList(ProjectUtils.getCurrProject()));
    }
}
