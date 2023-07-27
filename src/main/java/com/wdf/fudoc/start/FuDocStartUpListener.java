package com.wdf.fudoc.start;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.json.JSONUtil;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.wdf.fudoc.apidoc.config.state.FuDocSecuritySetting;
import com.wdf.fudoc.apidoc.view.FuDocGeneralForm;
import com.wdf.fudoc.common.CommonResult;
import com.wdf.fudoc.common.constant.ApiUrl;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.FuHtmlComponent;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.message.FuMsgManager;
import com.wdf.fudoc.start.dto.AnnouncementDTO;
import com.wdf.fudoc.start.dto.FuDocResultDTO;
import com.wdf.fudoc.start.dto.NotificationActionDTO;
import com.wdf.fudoc.start.dto.NotifyInfoDTO;
import com.wdf.fudoc.storage.FuStorageExecutor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 项目启动后调用
 *
 * @author wangdingfu
 * @date 2022-12-30 23:21:25
 */
public class FuDocStartUpListener implements StartupActivity {
    private static final Logger LOGGER = Logger.getInstance(FuDocGeneralForm.class);

    private static final int DAY = 60 * 60 * 24;
    private static final int ONE_HOUR = 60 * 60;

    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup(FuDocConstants.Notify.NOTIFY_GROUP);

    public static ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public void runActivity(@NotNull Project project) {
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(DynamicPluginListener.TOPIC, new DynamicPluginListener() {
            @Override
            public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
                LOGGER.info("fudoc插件被加载了...");
            }
        });
        ApplicationManager.getApplication().invokeLater(() -> {
            //初始化持久目录
            FuStorageExecutor.init(project);
            //设置可重定向
            HttpGlobalConfig.setMaxRedirectCount(5);
        });
        if (reentrantLock.tryLock()) {
            try {
                FuDocSecuritySetting instance = FuDocSecuritySetting.getInstance();
                //获取过期时间
                long time = instance.getTime();
                long currentSeconds = DateUtil.currentSeconds();
                if (time > currentSeconds && time < currentSeconds + DAY) {
                    return;
                }
                //下一次需要触发的时间
                long nextTime = currentSeconds + ONE_HOUR;
                //对请求参数加密
                try {
                    //请求获取最新通知信息
                    String result = RequestManager.doSendRequest(ApiUrl.PLUGIN_INFO_API_URL);
                    //根据返回结果做对应提示相关动作
                    executeResult(project, result);
                } catch (Exception e) {
                    LOGGER.info("请求获取版本信息失败", e);
                } finally {
                    instance.setTime(nextTime);
                    instance.loadState(instance);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }


    /**
     * 根据服务端返回的结果指定相关动作
     *
     * @param project 当前项目
     * @param result  服务端返回的结果
     */
    private void executeResult(Project project, String result) {
        if (StringUtils.isBlank(result)) {
            return;
        }
        CommonResult<FuDocResultDTO> commonResult = JSONUtil.toBean(result, new TypeReference<>() {
        }, true);
        FuDocResultDTO fuDocResultDTO;
        if (Objects.nonNull(commonResult) && commonResult.getCode() == 200 && Objects.nonNull(fuDocResultDTO = commonResult.getData())) {
            //添加消息
            List<FuMsgBO> msgList = fuDocResultDTO.getMsgList();
            if (CollectionUtils.isNotEmpty(msgList)) {
                msgList.forEach(FuMsgManager::addMsg);
            }

            //处理公告 公告显示失败 则显示通知
            if (!showAnnouncement(project, fuDocResultDTO.getAnnouncement())) {
                //显示通知 公告和通知二选一 控制不能发了公告又发通知
                showNotifyInfo(project, fuDocResultDTO.getNotifyInfo());
            }
        }
    }

    /**
     * 显示公告
     */
    public static boolean showAnnouncement(Project project, AnnouncementDTO announcement) {
        if (Objects.isNull(announcement)) {
            return false;
        }
        String id = announcement.getId();
        String title = announcement.getTitle();
        String content = announcement.getContent();
        if (StringUtils.isBlank(title) || StringUtils.isBlank(content) || StringUtils.isBlank(id)) {
            return false;
        }
        FuDocSecuritySetting instance = FuDocSecuritySetting.getInstance();
        if (!instance.isShow(id)) {
            return false;
        }
        //当前只支持发出html内容弹框公告
        FuHtmlComponent fuHtmlComponent = new FuHtmlComponent(project, title, content, announcement.getWidth(), announcement.getHeight());
        if (fuHtmlComponent.showAndGet()) {
            DumbService.getInstance(project).smartInvokeLater(() -> {
                //像服务端发出请求 当前用户已经确认
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("id", id);
                RequestManager.doSendRequest(ApiUrl.SEND_IS_READ_API_URL, paramMap);
            });
            instance.addTipId(id, true);
        } else {
            instance.addTipId(id, false);
        }
        instance.loadState(instance);
        return true;
    }

    /**
     * 发出消息通知
     */
    public void showNotifyInfo(Project project, NotifyInfoDTO notifyInfoDTO) {
        String message;
        if (Objects.isNull(notifyInfoDTO) || StringUtils.isBlank(message = notifyInfoDTO.getContent())) {
            return;
        }
        Notification notification = NOTIFICATION_GROUP.createNotification(message, NotificationType.INFORMATION);
        //添加行为
        List<NotificationActionDTO> actionList = notifyInfoDTO.getActionList();
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
