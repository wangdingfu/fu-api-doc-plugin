package com.wdf.fudoc.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wangdingfu
 * @Descption 公共类常量
 * @Date 2022-04-27 21:32:42
 */
public interface CommonObjectNames {

    String HTTP_REQUEST = "javax.servlet.http.HttpServletRequest";
    String HTTP_RESPONSE = "javax.servlet.http.HttpServletResponse";

    String MYBATIS_PLUS_PAGE = "com.baomidou.mybatisplus.plugins.Page";


    String MULTIPART_FILE = "org.springframework.web.multipart.MultipartFile";


    List<String> filterList = Lists.newArrayList(
            HTTP_REQUEST,
            HTTP_RESPONSE
    );




}
