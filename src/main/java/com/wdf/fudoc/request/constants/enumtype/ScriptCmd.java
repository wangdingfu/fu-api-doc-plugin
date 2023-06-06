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

    DEMO("fu.demo", "官网脚本示例", FuColor.GREEN.color(), ScriptCmdType.DEMO),
    PROJECT_PARAM("fu.projectEnv.setParam()", "设置项目全局参数", JBColor.PINK, ScriptCmdType.SCRIPT_DEMO),
    PROJECT_HEADER("fu.projectEnv.setHeader()", "设置项目全局请求头", FuColor.color1.color(), ScriptCmdType.SCRIPT_DEMO),
    GLOBAL_PARAM("fu.globalEnv.setParam()", "设置全局参数", FuColor.color2.color(), ScriptCmdType.SCRIPT_DEMO),
    GLOBAL_HEADER("fu.globalEnv.setHeader()", "设置全局请求头", FuColor.color3.color(), ScriptCmdType.SCRIPT_DEMO),
    RESPONSE("fu.result", "获取响应结果", FuColor.color4.color(), ScriptCmdType.SCRIPT_DEMO),
    SIGN("fu.sign()", "调用签名", FuColor.color5.color(), ScriptCmdType.SCRIPT_DEMO),
    CONSOLE("fu.log()", "打印日志", FuColor.color6.color(), ScriptCmdType.LOG),
    EXPIRE("fu.expireTime()", "设置过期时间", FuColor.color7.color(), ScriptCmdType.SCRIPT_DEMO),

    ;
    private final String cmd;

    private final String text;

    private final JBColor color;

    private final ScriptCmdType cmdType;

    public static List<ScriptCmd> getByCmdType(ScriptCmdType cmdType) {
        return Arrays.stream(ScriptCmd.values()).filter(f -> f.getCmdType().equals(cmdType)).collect(Collectors.toList());
    }


    public static void execute(BiConsumer<ScriptCmdType, List<ScriptCmd>> consumer) {
        execute(ScriptCmdType.DEMO, consumer);
        execute(ScriptCmdType.SCRIPT_DEMO, consumer);
        execute(ScriptCmdType.LOG, consumer);
    }

    public static void execute(ScriptCmdType cmdType, BiConsumer<ScriptCmdType, List<ScriptCmd>> consumer) {
        consumer.accept(cmdType, getByCmdType(cmdType));
    }
}
