package com.wdfu.fudoc;

import cn.hutool.json.JSONUtil;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-07-03 21:17:43
 */
public class YamlTest {

    static String yamlContent = "\n" +
            "spring:\n" +
            "  mvc:\n" +
            "    pathmatch:\n" +
            "      matching-strategy: ant_path_matcher\n" +
            "  application:\n" +
            "    name: @artifactId@\n" +
            "server:\n" +
            "  port: 8800\n" +
            "  profiles:\n" +
            "    active: @profiles.active@";


    public static void main(String[] args) {
        String yamlStr = yamlContent;
        final Map<String, String> itemMap = new ConcurrentHashMap<>();
        //替换@@包裹的内容
        String[] itemList = FuStringUtils.substringsBetween(yamlStr, "@", "@");
        if (Objects.nonNull(itemList)) {
            for (String item : itemList) {
                String itemStr = "@" + item + "@";
                yamlStr = yamlStr.replace(itemStr, item);
                itemMap.put(itemStr, item);
            }
        }
        System.out.println(yamlStr);
        System.out.println(JSONUtil.toJsonPrettyStr(itemMap));
    }
}
