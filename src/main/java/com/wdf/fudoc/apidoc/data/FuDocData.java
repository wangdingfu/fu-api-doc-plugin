package com.wdf.fudoc.apidoc.data;

import com.intellij.openapi.actionSystem.AnActionEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @descption: FuDoc数据
 * @date 2022-05-30 23:34:25
 */
@Getter
@Setter
@Builder
public class FuDocData {

    /**
     * 当前点击的事件对象
     */
    private AnActionEvent event;
}
