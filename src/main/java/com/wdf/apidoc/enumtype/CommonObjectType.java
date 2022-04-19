package com.wdf.apidoc.enumtype;

import com.intellij.psi.CommonClassNames;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @descption: 公共对象枚举
 * @author wangdingfu
 * @date 2022-04-10 22:07:42
 */
@Getter
public enum CommonObjectType {
    STRING(CommonClassNames.JAVA_LANG_STRING, "string"),
    NUMBER(CommonClassNames.JAVA_LANG_NUMBER, "Number"),
    BOOLEAN(CommonClassNames.JAVA_LANG_BOOLEAN, "Boolean"),
    BYTE(CommonClassNames.JAVA_LANG_BYTE, "byte[]"),
    SHORT(CommonClassNames.JAVA_LANG_SHORT, "Short"),
    INTEGER(CommonClassNames.JAVA_LANG_INTEGER, "Integer"),
    LONG(CommonClassNames.JAVA_LANG_LONG, "Long"),
    FLOAT(CommonClassNames.JAVA_LANG_FLOAT, "Float"),
    DOUBLE(CommonClassNames.JAVA_LANG_DOUBLE, "Double"),
    CHARACTER(CommonClassNames.JAVA_LANG_CHARACTER, "char"),
    BUFFER(CommonClassNames.JAVA_LANG_STRING_BUFFER, "string"),
    BUILDER(CommonClassNames.JAVA_LANG_STRING_BUILDER, "string"),


    ;


    private final String objPkg;

    private final String name;

    CommonObjectType(String objPkg, String name) {
        this.objPkg = objPkg;
        this.name = name;
    }

    public static String getName(String objPkg) {
        if (StringUtils.isNotBlank(objPkg)) {
            for (CommonObjectType value : CommonObjectType.values()) {
                if (value.getObjPkg().equals(objPkg)) {
                    return value.getName();
                }
            }
        }
        return null;
    }
}
