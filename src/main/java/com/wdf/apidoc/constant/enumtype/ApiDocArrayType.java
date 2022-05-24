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
 * @Descption 数组类型
 * @Date 2022-05-24 22:45:59
 */
@Getter
public enum ApiDocArrayType {

    int1("int[]", int[].class),
    int2("int[][]", int[][].class),
    integer1(CommonClassNames.JAVA_LANG_INTEGER + "[]", Integer[].class),
    integer2(CommonClassNames.JAVA_LANG_INTEGER + "[][]", Integer[][].class),


    boolean1("boolean[]", boolean[].class),
    boolean2("boolean[][]", boolean[][].class),
    Boolean1(CommonClassNames.JAVA_LANG_BOOLEAN + "[]", Boolean[].class),
    Boolean2(CommonClassNames.JAVA_LANG_BOOLEAN + "[][]", Boolean[][].class),

    char1("char[]", char[].class),
    char2("char[][]", char[][].class),
    Character1(CommonClassNames.JAVA_LANG_CHARACTER + "[]", Character[].class),
    Character2(CommonClassNames.JAVA_LANG_CHARACTER + "[][]", Character[][].class),

    short1("short[]", short[].class),
    short2("short[][]", short[][].class),
    Short1(CommonClassNames.JAVA_LANG_SHORT + "[]", Short[].class),
    Short2(CommonClassNames.JAVA_LANG_SHORT + "[][]", Short[][].class),


    long1("long[]", long[].class),
    long2("long[][]", long[][].class),
    Long1(CommonClassNames.JAVA_LANG_LONG + "[]", Long[].class),
    Long2(CommonClassNames.JAVA_LANG_LONG + "[][]", Long[][].class),

    double1("double[]", double[].class),
    double2("double[][]", double[][].class),
    Double1(CommonClassNames.JAVA_LANG_DOUBLE + "[]", Double[].class),
    Double2(CommonClassNames.JAVA_LANG_DOUBLE + "[][]", Double[][].class),

    float1("float[]", float[].class),
    float2("float[][]", float[][].class),
    Float1(CommonClassNames.JAVA_LANG_FLOAT + "[]", Float[].class),
    Float2(CommonClassNames.JAVA_LANG_FLOAT + "[][]", Float[][].class),

    string1("string[]", String[].class),
    string2("string[][]", String[][].class),

    byte1("byte[]", byte[].class),
    byte2("byte[][]", byte[][].class),

    Date1(CommonClassNames.JAVA_UTIL_DATE + "[]", Date[].class),
    Date2(CommonClassNames.JAVA_UTIL_DATE + "[][]", Date[][].class),

    Calendar1(CommonClassNames.JAVA_UTIL_CALENDAR + "[]", Calendar[].class),
    Calendar2(CommonClassNames.JAVA_UTIL_CALENDAR + "[][]", Calendar[][].class),

    LocalDateTime1(ApiDocConstants.ClassPkg.LOCAL_DATE_TIME + "[]", LocalDateTime[].class),
    LocalDateTime2(ApiDocConstants.ClassPkg.LOCAL_DATE_TIME + "[][]", LocalDateTime[][].class),

    BigDecimal1(ApiDocConstants.ClassPkg.BIG_DECIMAL + "[]", BigDecimal[].class),
    BigDecimal2(ApiDocConstants.ClassPkg.BIG_DECIMAL + "[][]", BigDecimal[][].class),

    BigInteger1(ApiDocConstants.ClassPkg.BIG_INTEGER + "[]", BigInteger[].class),
    BigInteger2(ApiDocConstants.ClassPkg.BIG_INTEGER + "[][]", BigInteger[][].class),

    LocalDate1(ApiDocConstants.ClassPkg.LOCAL_DATE + "[]", LocalDate[].class),
    LocalDate2(ApiDocConstants.ClassPkg.LOCAL_DATE + "[][]", LocalDate[][].class),

    LocalTime1(ApiDocConstants.ClassPkg.LOCAL_TIME + "[]", LocalTime[].class),
    LocalTime2(ApiDocConstants.ClassPkg.LOCAL_TIME + "[][]", LocalTime[][].class),

    Timestamp1(ApiDocConstants.ClassPkg.TIMESTAMP + "[]", Timestamp[].class),
    Timestamp2(ApiDocConstants.ClassPkg.TIMESTAMP + "[][]", Timestamp[][].class),


    Object1("java.lang.Object[]", Object[].class),
    ;


    private final String arrayType;


    private final Class<?> arrayClass;

    ApiDocArrayType(String arrayType, Class<?> arrayClass) {
        this.arrayType = arrayType;
        this.arrayClass = arrayClass;
    }

    public static ApiDocArrayType getEnum(String arrayType) {
        if (StringUtils.isNotBlank(arrayType)) {
            for (ApiDocArrayType value : ApiDocArrayType.values()) {
                if (value.getArrayType().equals(arrayType)) {
                    return value;
                }
            }
        }
        return ApiDocArrayType.Object1;
    }


    public static Class<?> getClass(String arrayType) {
        return getEnum(arrayType).getArrayClass();
    }
}
