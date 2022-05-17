package com.wdf.apidoc.mock;

import com.github.javafaker.Faker;
import com.wdf.apidoc.constant.enumtype.ContentType;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

import java.util.List;
import java.util.Locale;

/**
 * @author wangdingfu
 * @descption: faker mock数据
 * @date 2022-05-17 23:43:45
 */
public class FakerMockData extends AbstractApiDocMockData{


    @Override
    public String mock(ContentType contentType, List<ObjectInfoDesc> objectInfoDescList) {
        Faker faker = new Faker(Locale.CHINA);

        return null;
    }
}
