package com.wdf.apidoc.assemble;

import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: ApiDoc对象组装
 * @date 2022-05-08 22:19:29
 */
public interface FuDocAssembleService {


    /**
     * 获取实现类是否有能力组装当前的ClassInfoDesc对象为接口文档对象
     *
     * @param classInfoDesc java类信息描述对象
     * @return true: 可以组装成接口文档  false：无法组装
     */
    boolean isAssemble(ClassInfoDesc classInfoDesc);


    /**
     * 根据java类描述信息组装生成接口文档的数据
     *
     * @param classInfoDesc java类描述信息(包含注解、注释、字段等信息)
     * @return 接口文档页面需要渲染的数据
     */
    List<FuApiDocItemData> assemble(ClassInfoDesc classInfoDesc);

}
