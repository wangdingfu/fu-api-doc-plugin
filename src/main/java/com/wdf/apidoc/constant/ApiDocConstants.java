package com.wdf.apidoc.constant;

/**
 * @author wangdingfu
 * @descption: 类型常量
 * @date 2022-04-10 21:42:57
 */
public interface ApiDocConstants {

    interface ClassPkg {
        String BIG_DECIMAL = "java.math.BigDecimal";
    }


    interface Comment {

        /**
         * 请求参数的注释tag
         * TODO 后期可改成从配置中取
         */
        String PARAM = "param";
        /**
         * 方法返回值注释tag
         */
        String RETURN = "return";

        /**
         * psi中描述注释内容的类型
         */
        String PSI_COMMENT_DATA = "DOC_COMMENT_DATA";
    }

}
