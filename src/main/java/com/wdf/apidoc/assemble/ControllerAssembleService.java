package com.wdf.apidoc.assemble;

import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装Controller接口文档
 * @date 2022-05-09 23:32:39
 */
public class ControllerAssembleService extends AbstractAssembleService {


    /**
     * 判断当前实现类是否为Controller
     *
     * @param classInfoDesc java类信息描述对象
     * @return true:是  false:不是
     */
    @Override
    public boolean isAssemble(ClassInfoDesc classInfoDesc) {
        //判断是否有Controller|RestController注解

        return false;
    }

    /**
     * Controller类组装成接口文档
     *
     * @param classInfoDesc Controller类描述信息
     * @return 接口集合
     */
    @Override
    public List<FuApiDocItemData> assemble(ClassInfoDesc classInfoDesc) {
        //获取Controller类上的请求路径
        return null;
    }
}
