package com.wdf.fudoc;

import cn.hutool.json.JSON;

/**
* @author wangdingfu
* @date 2023-03-12 17:40:35
*/
public class TestJava {

    public void paddingData(JSON response, JSON fuDoc) {
        fuDoc.putByPath("token", response.getByPath("data.token"));
    }
}
