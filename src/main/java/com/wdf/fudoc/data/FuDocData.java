package com.wdf.fudoc.data;

import com.intellij.openapi.actionSystem.AnActionEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @descption: FuDoc数据
 * @date 2022-05-30 23:34:25
 */
@Getter
@Setter
public class FuDocData {

    /**
     * 当前点击的事件对象
     */
    private AnActionEvent event;
}
