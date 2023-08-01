package com.wdf.fudoc.start;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.wdf.fudoc.apidoc.config.state.FuDocSecuritySetting;
import com.wdf.fudoc.apidoc.view.FuDocGeneralForm;
import com.wdf.fudoc.common.CommonResult;
import com.wdf.fudoc.common.constant.FuDocConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-12 21:53:22
 */
public class RequestManager {
    private static final Logger LOGGER = Logger.getInstance(FuDocGeneralForm.class);

    private static final String BASE_URL = "http://localhost:9090";
    private static final String PRIMARY_KEY = "dfe68b77d54943fc8d481c6ae80a2a9d";

    public static String doSendRequest(String apiUrl) {
        return doSendRequest(apiUrl, new HashMap<>());
    }


    public static String doSendRequest(String apiUrl, Map<String, Object> paramMap) {
        FuDocSecuritySetting instance = FuDocSecuritySetting.getInstance();
        paramMap.put("uniqId", instance.getUniqId());
        paramMap.put("version", getPluginVersion());
        //对请求参数加密
        byte[] request = SecureUtil.aes(PRIMARY_KEY.getBytes()).encrypt(JSONUtil.toJsonStr(paramMap));
        HttpResponse httpResponse = null;
        try {
            //请求获取最新通知信息
            httpResponse = HttpRequest.post(BASE_URL + apiUrl).timeout(3000).body(request).execute();
            return httpResponse.body();
        } catch (Exception e) {
            LOGGER.info("请求获取版本信息失败", e);
            return null;
        } finally {
            if (Objects.nonNull(httpResponse)) {
                httpResponse.close();
            }
        }
    }


    private static String getPluginVersion() {
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId(FuDocConstants.ID));
        if (Objects.nonNull(plugin)) {
            return plugin.getVersion();
        }
        return "";
    }


    public static <T> T getData(String result) {
        CommonResult<T> commonResult = JSONUtil.toBean(result, new TypeReference<>() {
        }, true);
        if (Objects.nonNull(commonResult) && commonResult.getCode() == 200) {
            return commonResult.getData();
        }
        return null;
    }
}
