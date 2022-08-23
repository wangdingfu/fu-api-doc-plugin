package com.wdf.fudoc.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * java类路径
 *
 * @author wangdingfu
 * @date 2022-04-27 21:32:42
 */
public interface CommonObjectNames {

    String HTTP_REQUEST = "javax.servlet.http.HttpServletRequest";
    String HTTP_RESPONSE = "javax.servlet.http.HttpServletResponse";

    String MYBATIS_PLUS_PAGE = "com.baomidou.mybatisplus.plugins.Page";


    String MULTIPART_FILE = "org.springframework.web.multipart.MultipartFile";


    String JSON_IGNORE = "com.fasterxml.jackson.annotation.JsonIgnore";

    String JSON_PROPERTY = "com.fasterxml.jackson.annotation.JsonProperty";


    List<String> filterList = Lists.newArrayList(
            HTTP_REQUEST,
            HTTP_RESPONSE
    );


}
