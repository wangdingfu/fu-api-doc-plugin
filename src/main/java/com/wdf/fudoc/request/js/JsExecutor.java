package com.wdf.fudoc.request.js;

import com.wdf.fudoc.request.js.context.FuContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * 执行JS脚本
 * @author wangdingfu
 * @date 2023-05-31 10:18:41
 */
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
            cx.evaluateString(scriptableObject, fuContext.getScript(), "<cmd>", 1, null);
        } finally {
            Context.exit();
        }
    }
}
