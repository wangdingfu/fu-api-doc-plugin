package com.wdf.fudoc.request.constants.enumtype;

import com.intellij.ui.JBColor;
import com.wdf.fudoc.common.enumtype.FuColor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-06-04 22:02:53
 */
@AllArgsConstructor
@Getter
public enum ScriptCmd {

    DEMO("auth_config.js", "使用示例脚本", FuColor.GREEN.color(), ScriptCmdType.DEMO, true),
    DOC("", "查看使用文档", FuColor.GREEN.color(), ScriptCmdType.DEMO, true),
    PROJECT_SET_PARAM("code_set_variable.js", "设置全局变量", JBColor.PINK, ScriptCmdType.SCRIPT_DEMO, false),
    PROJECT_GET_PARAM("code_get_variable.js", "获取全局变量", JBColor.PINK, ScriptCmdType.SCRIPT_DEMO, false),
    PROJECT_SET_HEADER("code_set_header.js", "设置全局请求头", FuColor.color1.color(), ScriptCmdType.SCRIPT_DEMO, false),
    PROJECT_GET_HEADER("code_get_header.js", "获取全局请求头", FuColor.color1.color(), ScriptCmdType.SCRIPT_DEMO, false),
    ADD_HTTP_CONFIG("", "新增http请求配置", FuColor.ORANGE.color(), ScriptCmdType.HTTP, false),
    HTTP_REQUEST("code_http.js", "调用http接口", FuColor.color4.color(), ScriptCmdType.HTTP, false),
    ;
    private final String cmd;

    private final String text;

    private final JBColor color;

    private final ScriptCmdType cmdType;

    private final boolean isReset;

    public static List<ScriptCmd> getByCmdType(ScriptCmdType cmdType) {
        return Arrays.stream(ScriptCmd.values()).filter(f -> f.getCmdType().equals(cmdType)).collect(Collectors.toList());
    }


    public static void execute(BiConsumer<ScriptCmdType, List<ScriptCmd>> consumer) {
        Arrays.stream(ScriptCmdType.values()).sorted(Comparator.comparing(ScriptCmdType::getSort)).forEach(f -> execute(f, consumer));
    }

    public static void execute(ScriptCmdType cmdType, BiConsumer<ScriptCmdType, List<ScriptCmd>> consumer) {
        consumer.accept(cmdType, getByCmdType(cmdType));
    }
}
