package com.wdf.fudoc.start;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.wdf.fudoc.apidoc.config.state.FuDocSecuritySetting;
import com.wdf.fudoc.apidoc.view.FuDocGeneralForm;
import com.wdf.fudoc.start.dto.UpdateInfoDTO;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 项目启动后调用
 *
 * @author wangdingfu
 * @date 2022-12-30 23:21:25
 */
public class FuDocStartUpListener implements StartupActivity.Background {
    private static final Logger LOGGER = Logger.getInstance(FuDocGeneralForm.class);

    private static final int DAY = 60 * 60 * 24;
    private static final int ONE_HOUR = 60 * 60;
    private static final String URL = "http://150.158.164.160:9090/fu_doc_plugin/version";
    private static final String PRIMARY_KEY = "dfe68b77d54943fc8d481c6ae80a2a9d";


    @Override
    public void runActivity(@NotNull Project project) {
        FuDocSecuritySetting instance = FuDocSecuritySetting.getInstance();
        //获取过期时间
        long time = instance.getTime();
        long currentSeconds = DateUtil.currentSeconds();
        //每天至少请求一次
        if (time > currentSeconds && time < currentSeconds + DAY) {
            return;
        }
        //下一次需要触发的时间
        long nextTime = currentSeconds + ONE_HOUR;

        //对请求参数加密
        byte[] request = SecureUtil.aes(PRIMARY_KEY.getBytes()).encrypt(JSONUtil.toJsonStr(updateInfo(instance.getUniqId())));
        HttpResponse httpResponse = null;
        try {
            //请求获取最新通知信息
            httpResponse = HttpRequest.post(URL).timeout(3000).body(request).execute();
            String result = httpResponse.body();
        } catch (Exception e) {
            instance.setTime(nextTime);
            LOGGER.info("请求获取版本信息失败", e);
        } finally {
            if (Objects.nonNull(httpResponse)) {
                httpResponse.close();
            }
        }
        instance.loadState(instance);
    }


    private UpdateInfoDTO updateInfo(String uniqId) {
        UpdateInfoDTO updateInfoDTO = new UpdateInfoDTO();
        updateInfoDTO.setUniqId(uniqId);
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId("com.wdf.api"));
        if (Objects.nonNull(plugin)) {
            updateInfoDTO.setVersion(plugin.getVersion());
        }
        return updateInfoDTO;
    }
}
