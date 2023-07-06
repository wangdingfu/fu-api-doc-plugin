package com.wdf.fudoc.request.js;

import com.wdf.fudoc.components.FuConsole;
import com.wdf.fudoc.request.js.context.FuContext;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * 执行JS脚本
 *
 * @author wangdingfu
 * @date 2023-05-31 10:18:41
 */
@Slf4j
public class JsExecutor {


    /**
     * 执行javaScript脚本
     *
     * @param fuContext 脚本中的上下文对象
     */
    public static void execute(FuContext fuContext) {
        Context cx = Context.enter();
        try {
            // 将Java对象绑定到Rhino执行上下文中
            ScriptableObject scriptableObject = cx.initStandardObjects();
            ScriptableObject.putProperty(scriptableObject, "fu", fuContext);
            ScriptableObject.putProperty(scriptableObject, "console", new FuConsole(fuContext.getProject()));
            cx.evaluateString(scriptableObject, fuContext.getScript(), "<cmd>", 1, null);
        } catch (Exception e) {
            log.error("执行脚本【{}】异常", fuContext.getPreScriptPO().getTitle(), e);
        } finally {
            Context.exit();
        }
    }
}
