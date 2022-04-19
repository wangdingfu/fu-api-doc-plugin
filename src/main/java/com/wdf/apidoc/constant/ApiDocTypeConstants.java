package com.wdf.apidoc.constant;

import com.google.common.collect.Lists;
import com.intellij.psi.CommonClassNames;

import java.util.List;

/**
 * @descption: 类型常量
 * @author wangdingfu
 * @date 2022-04-10 21:42:57
 */
public interface ApiDocTypeConstants {


    List<String> JDK_BASE_OBJECT_LIST = Lists.newArrayList(
            CommonClassNames.JAVA_LANG_STRING,
            CommonClassNames.JAVA_LANG_NUMBER,
            CommonClassNames.JAVA_LANG_BOOLEAN,
            CommonClassNames.JAVA_LANG_BYTE,
            CommonClassNames.JAVA_LANG_SHORT,
            CommonClassNames.JAVA_LANG_INTEGER,
            CommonClassNames.JAVA_LANG_LONG,
            CommonClassNames.JAVA_LANG_FLOAT,
            CommonClassNames.JAVA_LANG_DOUBLE,
            CommonClassNames.JAVA_LANG_CHARACTER,
            CommonClassNames.JAVA_LANG_STRING_BUFFER,
            CommonClassNames.JAVA_LANG_STRING_BUILDER
    );

}
