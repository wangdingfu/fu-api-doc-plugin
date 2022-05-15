package com.wdf.apidoc.assemble;

import com.wdf.apidoc.pojo.data.FuApiDocParamData;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装抽象类
 * @date 2022-05-09 23:32:00
 */
public abstract class AbstractAssembleService implements ApiDocAssembleService {


    /**
     * 构建渲染接口文档参数的数据对象
     *
     * @param objectInfoDesc 对象解析后的描述信息集合
     * @return 接口文档页面显示的参数数据
     */
    protected FuApiDocParamData buildFuApiDocParamData(ObjectInfoDesc objectInfoDesc) {
        FuApiDocParamData fuApiDocParamData = new FuApiDocParamData();

        return fuApiDocParamData;
    }
}
