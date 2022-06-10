package com.wdf.apidoc.constant.enumtype;

import com.intellij.psi.CommonClassNames;
import com.wdf.apidoc.constant.FuDocConstants;
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
    PRIMITIVE_INT("int", "int", int.class, FuDocObjectType.PRIMITIVE),
    PRIMITIVE_BOOLEAN("boolean", "boolean", boolean.class, FuDocObjectType.PRIMITIVE),
    PRIMITIVE_CHAR("char", "char", char.class, FuDocObjectType.PRIMITIVE),
    PRIMITIVE_SHORT("short", "short", short.class, FuDocObjectType.PRIMITIVE),
    PRIMITIVE_BYTE("byte", "byte", byte.class, FuDocObjectType.PRIMITIVE),
    PRIMITIVE_LONG("long", "long", long.class, FuDocObjectType.PRIMITIVE),
    PRIMITIVE_FLOAT("float", "float", float.class, FuDocObjectType.PRIMITIVE),
    PRIMITIVE_DOUBLE("double", "double", double.class, FuDocObjectType.PRIMITIVE),


    /**
     * 常用数据类型
     */
    STRING(CommonClassNames.JAVA_LANG_STRING, "string", String.class, FuDocObjectType.COMMON_OBJECT),
    NUMBER(CommonClassNames.JAVA_LANG_NUMBER, "number", Number.class, FuDocObjectType.COMMON_OBJECT),
    BOOLEAN(CommonClassNames.JAVA_LANG_BOOLEAN, "boolean", Boolean.class, FuDocObjectType.COMMON_OBJECT),
    BYTE(CommonClassNames.JAVA_LANG_BYTE, "byte", byte.class, FuDocObjectType.COMMON_OBJECT),
    SHORT(CommonClassNames.JAVA_LANG_SHORT, "short", Short.class, FuDocObjectType.COMMON_OBJECT),
    INTEGER(CommonClassNames.JAVA_LANG_INTEGER, "integer", Integer.class, FuDocObjectType.COMMON_OBJECT),
    LONG(CommonClassNames.JAVA_LANG_LONG, "long", Long.class, FuDocObjectType.COMMON_OBJECT),
    FLOAT(CommonClassNames.JAVA_LANG_FLOAT, "float", Float.class, FuDocObjectType.COMMON_OBJECT),
    DOUBLE(CommonClassNames.JAVA_LANG_DOUBLE, "double", Double.class, FuDocObjectType.COMMON_OBJECT),
    CHARACTER(CommonClassNames.JAVA_LANG_CHARACTER, "char", char.class, FuDocObjectType.COMMON_OBJECT),
    BUFFER(CommonClassNames.JAVA_LANG_STRING_BUFFER, "string", String.class, FuDocObjectType.COMMON_OBJECT),
    BUILDER(CommonClassNames.JAVA_LANG_STRING_BUILDER, "string", String.class, FuDocObjectType.COMMON_OBJECT),
    BIG_DECIMAL(FuDocConstants.ClassPkg.BIG_DECIMAL, "bigDecimal", BigDecimal.class, FuDocObjectType.COMMON_OBJECT),
    BIG_INTEGER(FuDocConstants.ClassPkg.BIG_INTEGER, "bigInteger", BigInteger.class, FuDocObjectType.COMMON_OBJECT),
    DATE(CommonClassNames.JAVA_UTIL_DATE, "date", Date.class, FuDocObjectType.COMMON_OBJECT),
    CALENDAR(CommonClassNames.JAVA_UTIL_CALENDAR, "calendar", Calendar.class, FuDocObjectType.COMMON_OBJECT),
    LOCAL_DATETIME(FuDocConstants.ClassPkg.LOCAL_DATE_TIME, "localDateTime", LocalDateTime.class, FuDocObjectType.COMMON_OBJECT),
    LOCAL_DATE(FuDocConstants.ClassPkg.LOCAL_DATE, "localDate", LocalDate.class, FuDocObjectType.COMMON_OBJECT),
    LOCAL_TIME(FuDocConstants.ClassPkg.LOCAL_TIME, "localTime", LocalTime.class, FuDocObjectType.COMMON_OBJECT),
    TIMESTAMP(FuDocConstants.ClassPkg.TIMESTAMP, "timestamp", Timestamp.class, FuDocObjectType.COMMON_OBJECT),
    ENUM(CommonClassNames.JAVA_LANG_ENUM, "enum", Enum.class, FuDocObjectType.COMMON_OBJECT),


    OBJECT_TYPE("java.lang.Object", "object", Object.class, FuDocObjectType.DEFAULT_OBJECT),
    ;

    private final String objPkg;

    private final String name;

    private final Class<?> clazz;

    private final FuDocObjectType fuDocObjectType;

    CommonObjectType(String objPkg, String name, Class<?> clazz, FuDocObjectType fuDocObjectType) {
        this.objPkg = objPkg;
        this.name = name;
        this.clazz = clazz;
        this.fuDocObjectType = fuDocObjectType;
    }

    public boolean isPrimitiveOrCommon() {
        return FuDocObjectType.COMMON_OBJECT.equals(this.getFuDocObjectType())
                || FuDocObjectType.PRIMITIVE.equals(this.getFuDocObjectType());
    }


    public static boolean isPrimitive(String objPkg) {
        FuDocObjectType fuDocObjectType = getEnum(objPkg).getFuDocObjectType();
        return FuDocObjectType.PRIMITIVE.equals(fuDocObjectType);
    }

    public static boolean isPrimitiveOrCommon(String objPkg) {
        FuDocObjectType fuDocObjectType = getEnum(objPkg).getFuDocObjectType();
        return FuDocObjectType.COMMON_OBJECT.equals(fuDocObjectType)
                || FuDocObjectType.PRIMITIVE.equals(fuDocObjectType);
    }

    public static boolean isCommon(String objPkg) {
        FuDocObjectType fuDocObjectType = getEnum(objPkg).getFuDocObjectType();
        return FuDocObjectType.COMMON_OBJECT.equals(fuDocObjectType);
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
