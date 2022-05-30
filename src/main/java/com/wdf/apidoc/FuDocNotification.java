package com.wdf.apidoc;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.data.FuDocDataContent;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @descption: FuDoc通知消息类
 * @date 2022-05-30 23:29:04
 */
public class FuDocNotification {


    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance()
            .getNotificationGroup(ApiDocConstants.Notify.NOTIFY_GROUP);


    public static void notifyWarn(String key) {
        NOTIFICATION_GROUP.createNotification(getMessage(key), NotificationType.WARNING).notify(FuDocDataContent.getProject());
    }

    public static void notifyInfo(String key) {
        NOTIFICATION_GROUP.createNotification(getMessage(key), NotificationType.INFORMATION).notify(FuDocDataContent.getProject());
    }

    public static void notifyError(String key) {
        NOTIFICATION_GROUP.createNotification(getMessage(key), NotificationType.ERROR).notify(FuDocDataContent.getProject());
    }

    private static String getMessage(String key) {
        String message = FuDocMessageBundle.message(key);
        return StringUtils.isBlank(message) ? key : message;
    }

}
