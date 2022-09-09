package com.wdf.fudoc.common.notification;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.apidoc.data.FuDocDataContent;

/**
 * FuDoc通知消息类 (2021.1.3版本之前)
 *
 * @author wangdingfu
 * @date 2022-05-30 23:29:04
 */
public class FuDocNotification {


    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup(FuDocConstants.Notify.NOTIFY_GROUP);

    public static void notifyInfo(String message) {
        genNotify(NotificationType.INFORMATION, message, FuDocDataContent.getProject());
    }

    public static void notifyWarn(String message) {
        genNotify(NotificationType.WARNING, message, FuDocDataContent.getProject());
    }

    public static void notifyError(String message) {
        genNotify(NotificationType.ERROR, message, FuDocDataContent.getProject());
    }

    /**
     * 接口文档生成通知
     */
    public static void genNotify(NotificationType notificationType, String message, Project project) {
        String readDocAction = FuDocMessageBundle.message(MessageConstants.READ_DOC_ACTION);
        String faqAction = FuDocMessageBundle.message(MessageConstants.FAQ_ACTION);
        String starAction = FuDocMessageBundle.message(MessageConstants.STAR_ACTION);

        NOTIFICATION_GROUP.createNotification(FuDocConstants.FU_DOC, message, notificationType)
                //新增查看文档按钮
                .addAction(new BrowseNotificationAction(readDocAction, UrlConstants.DOCUMENT))
                //新增给我提问题按钮
                .addAction(new BrowseNotificationAction(faqAction, UrlConstants.ISSUE))
                //新增给我点个小爱心按钮
                .addAction(new BrowseNotificationAction(starAction, UrlConstants.GITHUB))
                .notify(project);
    }


}
