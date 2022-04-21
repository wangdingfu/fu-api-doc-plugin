package com.wdf.apidoc.factory;

import com.wdf.apidoc.parse.object.ApiDocObjectParser;
import com.wdf.apidoc.parse.object.impl.*;
import lombok.Getter;
import org.apache.commons.compress.utils.Lists;

import java.util.Comparator;
import java.util.List;

/**
 * @author wangdingfu
 * @description 解析对象处理器工厂类
 * @Date 2022-04-18 21:07:58
 */
public class ParseObjectHandlerFactory {

    @Getter
    private static final List<ApiDocObjectParser> OBJECT_PARSER_LIST = Lists.newArrayList();

    static {
        OBJECT_PARSER_LIST.add(new ApiDocPrimitiveParser());
        OBJECT_PARSER_LIST.add(new ApiDocCommonObjectParser());
        OBJECT_PARSER_LIST.add(new ApiDocOtherObjectParser());
        OBJECT_PARSER_LIST.add(new ApiDocCollectionParser());
        OBJECT_PARSER_LIST.add(new ApiDocMapParser());
        OBJECT_PARSER_LIST.add(new ApiDocDefaultParser());
        //根据各自实现类的优先加载顺序来排序
        OBJECT_PARSER_LIST.sort(Comparator.comparing(ApiDocObjectParser::sort));
    }

}
