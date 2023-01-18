package com.wdf.fudoc.start;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.SystemUtil;
import com.intellij.notification.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.wdf.fudoc.apidoc.view.FuDocGeneralForm;
import com.wdf.fudoc.common.CommonResult;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.config.state.FuDocSecuritySetting;
import com.wdf.fudoc.start.dto.NotificationActionDTO;
import com.wdf.fudoc.start.dto.VersionInfoDTO;
import com.wdf.fudoc.start.dto.VersionInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


/**
 * 主要用于应用启动后发送一些提示信息
 *
 * @author wangdingfu
 * @date 2022-08-14 22:32:40
 */
public class FuDocAppStartUpListener implements StartupActivity.Background {
    private static final Logger LOGGER = Logger.getInstance(FuDocGeneralForm.class);

    private static final String PRIMARY_KEY = "dfe68b77d54943fc8d481c6ae80a2a9d";

    private static final String URL = "http://150.158.164.160:9090/fu_doc_plugin/version";
//    private static final String URL = "http://wangdingfu.tpddns.cn:9090/fu_doc_plugin/version";

    private static final int DAY = 60 * 60 * 24;
    private static final int ONE_HOUR = 60 * 60;

    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup(FuDocConstants.Notify.NOTIFY_GROUP);


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
        byte[] request = SecureUtil.aes(PRIMARY_KEY.getBytes()).encrypt(JSONUtil.toJsonStr(buildQueryParam(instance.getUniqId())));
        try {
            //请求获取最新通知信息
            String result = HttpRequest.post(URL).timeout(3000).body(request).execute().body();
            //发送通知信息
            notifyInfo(instance, project, result, nextTime);
        } catch (Exception e) {
            instance.setTime(nextTime);
            LOGGER.info("请求获取版本信息失败", e);
        }
        instance.loadState(instance);
    }


    /**
     * 通知更新信息
     *
     * @param instance 持久化实例对象
     * @param project  当前项目
     * @param result   通知结果
     * @param nextTime 下次请求时间
     */
    private void notifyInfo(FuDocSecuritySetting instance, Project project, String result, long nextTime) {
        if (StringUtils.isNotBlank(result)) {
            CommonResult<VersionInfoVO> commonResult = JSONUtil.toBean(result, new TypeReference<>() {
            }, true);
            VersionInfoVO data = commonResult.getData();
            if (Objects.nonNull(data) && data.getCode() == 1 && data.getTime() > 0) {
                nextTime = data.getTime();
                //发起通知
                notifyUpdateInfo(project, data);
            }
            instance.setUniqId(Objects.nonNull(data) ? data.getUniqId() : "");
            instance.setTime(nextTime);
            instance.loadState(instance);
        }
    }


    /**
     * 构造请求参数
     *
     * @param uniqId 唯一标识ID
     * @return 请求参数
     */
    private VersionInfoDTO buildQueryParam(String uniqId) {
        VersionInfoDTO versionInfoDTO = new VersionInfoDTO();
        HostInfo hostInfo = SystemUtil.getHostInfo();
        if (Objects.nonNull(hostInfo)) {
            versionInfoDTO.setHostName(hostInfo.getName());
            versionInfoDTO.setHostAddress(hostInfo.getAddress());
        }

        //获取当前插件版本
        versionInfoDTO.setUniqId(uniqId);
        versionInfoDTO.setPluginVersion("v1.2.7");
        return versionInfoDTO;
    }


    public void notifyUpdateInfo(Project project, VersionInfoVO versionInfoVO) {
        String message = versionInfoVO.getMessage();
        if (StringUtils.isBlank(message)) {
            return;
        }
        Notification notification = NOTIFICATION_GROUP.createNotification(message, NotificationType.INFORMATION);
        //添加行为
        List<NotificationActionDTO> actionList = versionInfoVO.getActionList();
        if (CollectionUtils.isNotEmpty(actionList)) {
            for (NotificationActionDTO action : actionList) {
                String actionName = action.getActionName();
                String actionContent = action.getActionContent();
                if (StringUtils.isNotBlank(actionName) && StringUtils.isNotBlank(actionContent)) {
                    notification.addAction(new BrowseNotificationAction(actionName, actionContent));
                }
            }
        }
        //发出通知
        notification.notify(project);
    }
}
