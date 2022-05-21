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
    PRIMITIVE_INT("int", "int", int.class, ApiDocObjectType.PRIMITIVE),
    PRIMITIVE_BOOLEAN("boolean", "boolean", boolean.class, ApiDocObjectType.PRIMITIVE),
    PRIMITIVE_CHAR("char", "char", char.class, ApiDocObjectType.PRIMITIVE),
    PRIMITIVE_SHORT("short", "short", short.class, ApiDocObjectType.PRIMITIVE),
    PRIMITIVE_BYTE("byte", "byte", byte.class, ApiDocObjectType.PRIMITIVE),
    PRIMITIVE_LONG("long", "long", long.class, ApiDocObjectType.PRIMITIVE),
    PRIMITIVE_FLOAT("float", "float", float.class, ApiDocObjectType.PRIMITIVE),
    PRIMITIVE_DOUBLE("double", "double", double.class, ApiDocObjectType.PRIMITIVE),


    /**
     * 常用数据类型
     */
    STRING(CommonClassNames.JAVA_LANG_STRING, "string", String.class, ApiDocObjectType.COMMON_OBJECT),
    NUMBER(CommonClassNames.JAVA_LANG_NUMBER, "number", Number.class, ApiDocObjectType.COMMON_OBJECT),
    BOOLEAN(CommonClassNames.JAVA_LANG_BOOLEAN, "boolean", Boolean.class, ApiDocObjectType.COMMON_OBJECT),
    BYTE(CommonClassNames.JAVA_LANG_BYTE, "byte", byte.class, ApiDocObjectType.COMMON_OBJECT),
    SHORT(CommonClassNames.JAVA_LANG_SHORT, "short", Short.class, ApiDocObjectType.COMMON_OBJECT),
    INTEGER(CommonClassNames.JAVA_LANG_INTEGER, "integer", Integer.class, ApiDocObjectType.COMMON_OBJECT),
    LONG(CommonClassNames.JAVA_LANG_LONG, "long", Long.class, ApiDocObjectType.COMMON_OBJECT),
    FLOAT(CommonClassNames.JAVA_LANG_FLOAT, "float", Float.class, ApiDocObjectType.COMMON_OBJECT),
    DOUBLE(CommonClassNames.JAVA_LANG_DOUBLE, "double", Double.class, ApiDocObjectType.COMMON_OBJECT),
    CHARACTER(CommonClassNames.JAVA_LANG_CHARACTER, "char", char.class, ApiDocObjectType.COMMON_OBJECT),
    BUFFER(CommonClassNames.JAVA_LANG_STRING_BUFFER, "string", String.class, ApiDocObjectType.COMMON_OBJECT),
    BUILDER(CommonClassNames.JAVA_LANG_STRING_BUILDER, "string", String.class, ApiDocObjectType.COMMON_OBJECT),
    BIG_DECIMAL(ApiDocConstants.ClassPkg.BIG_DECIMAL, "bigDecimal", BigDecimal.class, ApiDocObjectType.COMMON_OBJECT),
    BIG_INTEGER(ApiDocConstants.ClassPkg.BIG_INTEGER, "bigInteger", BigInteger.class, ApiDocObjectType.COMMON_OBJECT),
    DATE(CommonClassNames.JAVA_UTIL_DATE, "date", Date.class, ApiDocObjectType.COMMON_OBJECT),
    CALENDAR(CommonClassNames.JAVA_UTIL_CALENDAR, "calendar", Calendar.class, ApiDocObjectType.COMMON_OBJECT),
    LOCAL_DATETIME(ApiDocConstants.ClassPkg.LOCAL_DATE_TIME, "localDateTime", LocalDateTime.class, ApiDocObjectType.COMMON_OBJECT),
    LOCAL_DATE(ApiDocConstants.ClassPkg.LOCAL_DATE, "localDate", LocalDate.class, ApiDocObjectType.COMMON_OBJECT),
    LOCAL_TIME(ApiDocConstants.ClassPkg.LOCAL_TIME, "localTime", LocalTime.class, ApiDocObjectType.COMMON_OBJECT),
    TIMESTAMP(ApiDocConstants.ClassPkg.TIMESTAMP, "timestamp", Timestamp.class, ApiDocObjectType.COMMON_OBJECT),
    ENUM(CommonClassNames.JAVA_LANG_ENUM, "enum", Enum.class, ApiDocObjectType.COMMON_OBJECT),


    OBJECT_TYPE("java.lang.Object", "object", Object.class, ApiDocObjectType.DEFAULT_OBJECT),
    ;

    private final String objPkg;

    private final String name;

    private final Class<?> clazz;

    private final ApiDocObjectType apiDocObjectType;

    CommonObjectType(String objPkg, String name, Class<?> clazz, ApiDocObjectType apiDocObjectType) {
        this.objPkg = objPkg;
        this.name = name;
        this.clazz = clazz;
        this.apiDocObjectType = apiDocObjectType;
    }

    public boolean isPrimitiveOrCommon() {
        return ApiDocObjectType.COMMON_OBJECT.equals(this.getApiDocObjectType())
                || ApiDocObjectType.PRIMITIVE.equals(this.getApiDocObjectType());
    }


    public static boolean isPrimitive(String objPkg) {
        ApiDocObjectType apiDocObjectType = getEnum(objPkg).getApiDocObjectType();
        return ApiDocObjectType.PRIMITIVE.equals(apiDocObjectType);
    }

    public static boolean isPrimitiveOrCommon(String objPkg) {
        ApiDocObjectType apiDocObjectType = getEnum(objPkg).getApiDocObjectType();
        return ApiDocObjectType.COMMON_OBJECT.equals(apiDocObjectType)
                || ApiDocObjectType.PRIMITIVE.equals(apiDocObjectType);
    }

    public static boolean isCommon(String objPkg) {
        ApiDocObjectType apiDocObjectType = getEnum(objPkg).getApiDocObjectType();
        return ApiDocObjectType.COMMON_OBJECT.equals(apiDocObjectType);
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
