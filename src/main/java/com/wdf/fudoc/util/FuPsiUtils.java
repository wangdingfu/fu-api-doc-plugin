package com.wdf.fudoc.util;

import com.wdf.fudoc.futool.beancopy.bo.FuPsiMethod;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @date 2023-08-07 11:13:00
 */
public class FuPsiUtils {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String IS = "is";


    public static FuPsiMethod methodToFiled(String methodName) {
        if (methodName.startsWith(GET)) {
            return buildFuPsiMethod(methodName, GET);
        } else if (methodName.startsWith(IS)) {
            return buildFuPsiMethod(methodName, IS);
        } else if (methodName.startsWith(SET)) {
            return buildFuPsiMethod(methodName, SET);
        }
        return null;
    }


    private static FuPsiMethod buildFuPsiMethod(String methodName, String prefix) {
        FuPsiMethod fuPsiMethod = new FuPsiMethod();
        String fieldName = StringUtils.substringAfter(methodName, prefix);
        char[] chars = fieldName.toCharArray();
        if (chars.length > 0) {
            chars[0] = Character.toLowerCase(chars[0]);
        }
        fuPsiMethod.setFieldName(new String(chars));
        fuPsiMethod.setSetter(SET.equals(prefix));
        fuPsiMethod.setGetter(GET.equals(prefix) || IS.equals(prefix));
        return fuPsiMethod;
    }

}
