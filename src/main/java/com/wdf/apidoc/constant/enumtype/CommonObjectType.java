package com.wdf.apidoc.constant.enumtype;

import com.intellij.psi.CommonClassNames;
import com.wdf.apidoc.constant.ApiDocConstants;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangdingfu
 * @descption: 公共对象枚举
 * @date 2022-04-10 22:07:42
 */
@Getter
public enum CommonObjectType {


    /**
     * java 8中基本数据类型
     */
    PRIMITIVE_INT("int", "int", int.class),
    PRIMITIVE_BOOLEAN("boolean", "boolean", boolean.class),
    PRIMITIVE_CHAR("char", "char", char.class),
    PRIMITIVE_SHORT("short", "short", short.class),
    PRIMITIVE_BYTE("byte", "byte", byte.class),
    PRIMITIVE_LONG("long", "long", long.class),
    PRIMITIVE_FLOAT("float", "float", float.class),
    PRIMITIVE_DOUBLE("double", "double", double.class),


    /**
     * 常用数据类型
     */
    STRING(CommonClassNames.JAVA_LANG_STRING, "string", String.class),
    NUMBER(CommonClassNames.JAVA_LANG_NUMBER, "number", Number.class),
    BOOLEAN(CommonClassNames.JAVA_LANG_BOOLEAN, "boolean", Boolean.class),
    BYTE(CommonClassNames.JAVA_LANG_BYTE, "byte", byte.class),
    SHORT(CommonClassNames.JAVA_LANG_SHORT, "short", Short.class),
    INTEGER(CommonClassNames.JAVA_LANG_INTEGER, "integer", Integer.class),
    LONG(CommonClassNames.JAVA_LANG_LONG, "long", Long.class),
    FLOAT(CommonClassNames.JAVA_LANG_FLOAT, "float", Float.class),
    DOUBLE(CommonClassNames.JAVA_LANG_DOUBLE, "double", Double.class),
    CHARACTER(CommonClassNames.JAVA_LANG_CHARACTER, "char", char.class),
    BUFFER(CommonClassNames.JAVA_LANG_STRING_BUFFER, "string", String.class),
    BUILDER(CommonClassNames.JAVA_LANG_STRING_BUILDER, "string", String.class),
    BIG_DECIMAL(ApiDocConstants.ClassPkg.BIG_DECIMAL, "bigDecimal", BigDecimal.class),
    BIG_INTEGER(ApiDocConstants.ClassPkg.BIG_INTEGER, "bigInteger", BigInteger.class),
    DATE(CommonClassNames.JAVA_UTIL_DATE, "date", Date.class),
    CALENDAR(CommonClassNames.JAVA_UTIL_CALENDAR, "calendar", Calendar.class),
    LOCAL_DATETIME(ApiDocConstants.ClassPkg.LOCAL_DATE_TIME, "localDateTime", LocalDateTime.class),
    LOCAL_DATE(ApiDocConstants.ClassPkg.LOCAL_DATE, "localDate", LocalDate.class),
    LOCAL_TIME(ApiDocConstants.ClassPkg.LOCAL_TIME, "localTime", LocalTime.class),
    TIMESTAMP(ApiDocConstants.ClassPkg.TIMESTAMP, "timestamp", Timestamp.class),
    ENUM(CommonClassNames.JAVA_LANG_ENUM, "enum", Enum.class),


    OBJECT_TYPE("java.lang.Object", "object", Object.class),
    ;

    private final String objPkg;

    private final String name;

    private final Class<?> clazz;

    CommonObjectType(String objPkg, String name, Class<?> clazz) {
        this.objPkg = objPkg;
        this.name = name;
        this.clazz = clazz;
    }


    public static CommonObjectType getEnum(String objPkg) {
        if (StringUtils.isNotBlank(objPkg)) {
            for (CommonObjectType value : CommonObjectType.values()) {
                if (value.getObjPkg().equals(objPkg)) {
                    return value;
                }
            }
        }
        return CommonObjectType.OBJECT_TYPE;
    }

    public static String getName(String objPkg) {
        return getEnum(objPkg).getName();
    }

    public static Class<?> getClass(String objPkg) {
        return getEnum(objPkg).getClazz();
    }
}
