package com.wdf.apidoc;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.wdf.apidoc.constant.FuDocConstants;
import com.wdf.apidoc.data.FuDocDataContent;

/**
 * @author wangdingfu
 * @descption: FuDoc通知消息类
 * @date 2022-05-30 23:29:04
 */
public class FuDocNotification {


    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance()
            .getNotificationGroup(FuDocConstants.Notify.NOTIFY_GROUP);


    public static void notifyWarn(String message) {
        NOTIFICATION_GROUP.createNotification(message, NotificationType.WARNING).notify(FuDocDataContent.getProject());
    }

    public static void notifyInfo(String message) {
        NOTIFICATION_GROUP.createNotification(message, NotificationType.INFORMATION).notify(FuDocDataContent.getProject());
    }

    public static void notifyError(String message) {
        NOTIFICATION_GROUP.createNotification(message, NotificationType.ERROR).notify(FuDocDataContent.getProject());
    }


}
