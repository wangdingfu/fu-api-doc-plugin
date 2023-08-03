package com.wdf.fudoc.util;

import com.wdf.fudoc.common.base.FuFunction;
import org.apache.commons.lang3.StringUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author wangdingfu
 * @date 2023-07-12 21:18:46
 */
public class LambdaUtils {


    /**
     * 根据lambda表达式获取字段名
     *
     * @param lambda 表达式
     * @return 字段名
     */
    public static String getPropertyName(FuFunction<?, ?> lambda) {
        try {
            Class<?> lambdaClass = lambda.getClass();
            Method method = lambdaClass.getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            String getterMethod = serializedLambda.getImplMethodName();
            return Introspector.decapitalize(getterMethod.replace("get", ""));
        } catch (ReflectiveOperationException e) {
            return StringUtils.EMPTY;
        }
    }

}
