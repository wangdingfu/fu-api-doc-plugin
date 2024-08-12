package com.wdf.fudoc.request.constants.enumtype;

import com.intellij.ui.JBColor;
import cn.fudoc.common.base.FuBundle;
import cn.fudoc.common.enumtype.FuColor;
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

    DEMO("auth_config.js", FuBundle.message("fudoc.script.study.demo.text"), FuColor.GREEN.color(), ScriptCmdType.DEMO, true),
    DOC("http://www.fudoc.cn/pages/4427f1/", FuBundle.message("fudoc.script.study.demo.doc"), FuColor.console_debug.color(), ScriptCmdType.DEMO, true),
    PROJECT_SET_PARAM("code_set_variable.js", FuBundle.message("fudoc.script.study.set.variable"), FuColor.console_verbose.color(), ScriptCmdType.SCRIPT_DEMO, false),
    PROJECT_GET_PARAM("code_get_variable.js", FuBundle.message("fudoc.script.study.get.variable"), FuColor.console_user_info.color(), ScriptCmdType.SCRIPT_DEMO, false),
    PROJECT_SET_HEADER("code_set_header.js", FuBundle.message("fudoc.script.study.set.header"), FuColor.color6.color(), ScriptCmdType.SCRIPT_DEMO, false),
    PROJECT_GET_HEADER("code_get_header.js", FuBundle.message("fudoc.script.study.get.header"), FuColor.console_user_info.color(), ScriptCmdType.SCRIPT_DEMO, false),
    ADD_HTTP_CONFIG("", FuBundle.message("fudoc.script.study.http.add"), FuColor.RED.color(), ScriptCmdType.HTTP, false),
    HTTP_REQUEST("code_http.js", FuBundle.message("fudoc.script.study.http.exec"), FuColor.color4.color(), ScriptCmdType.HTTP, false),
    CONSOLE_ERROR("console.error('这是一条{}级别日志','error');", FuBundle.message("fudoc.script.console.error.title"), FuColor.console_error.color(), ScriptCmdType.LOG, false),
    CONSOLE_INFO("console.info('这是一条{}级别日志','info');", FuBundle.message("fudoc.script.console.info.title"), FuColor.console_info.color(), ScriptCmdType.LOG, false),
    CONSOLE_DEBUG("console.debug('这是一条{}级别日志','debug');", FuBundle.message("fudoc.script.console.debug.title"), FuColor.console_verbose.color(), ScriptCmdType.LOG, false),
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
