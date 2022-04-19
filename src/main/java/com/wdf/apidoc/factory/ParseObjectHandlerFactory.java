package com.wdf.apidoc.factory;

import com.wdf.apidoc.handler.ParseObjectHandler;
import com.wdf.apidoc.handler.impl.*;
import lombok.Getter;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author wangdingfu
 * @Descption 解析对象处理器工厂类
 * @Date 2022-04-18 21:07:58
 */
public class ParseObjectHandlerFactory {

    @Getter
    private static final List<ParseObjectHandler> parseObjectHandlerList = Lists.newArrayList();

    static {
        parseObjectHandlerList.add(new ParsePrimitiveTypeHandler());
        parseObjectHandlerList.add(new ParseCommonObjectTypeHandler());
        parseObjectHandlerList.add(new ParseJavaLangObjectTypeHandler());
        parseObjectHandlerList.add(new ParseCollectionObjectHandler());
        parseObjectHandlerList.add(new ParseMapObjectHandler());
        parseObjectHandlerList.add(new ParseDefaultObjectHandler());
    }

}
