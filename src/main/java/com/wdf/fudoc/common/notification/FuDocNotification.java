package com.wdf.fudoc.common.notification;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.util.ProjectUtils;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FuDoc通知消息类 (2021.1.3版本之前)
 *
 * @author wangdingfu
 * @date 2022-05-30 23:29:04
 */
public class FuDocNotification {


    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup(FuDocConstants.Notify.NOTIFY_GROUP);

    public static void notifyInfo(String message) {
        genNotify(NotificationType.INFORMATION, message, ProjectUtils.getCurrProject());
    }

    public static void notifyWarn(String message) {
        genNotify(NotificationType.WARNING, message, ProjectUtils.getCurrProject());
    }

    public static void notifyError(String message) {
        genNotify(NotificationType.ERROR, message, ProjectUtils.getCurrProject());
    }

    /**
     * 接口文档生成通知
     */
    public static void notifySyncApiResult(NotificationType notificationType, String message, String apiSystemName, String url, JPanel showPanel, AtomicBoolean pinStatus) {
        //查看同步结果
        String syncResult = FuBundle.message(MessageConstants.SYNC_API_RECORD_READ);
        //去接口文档系统查看文档(去YApi查看文档)
        String apiSystem = FuBundle.message(MessageConstants.SYNC_API_INTO_API_SYSTEM, apiSystemName);
        //给我点赞
        String starAction = FuBundle.message(MessageConstants.STAR_ACTION);

        NOTIFICATION_GROUP.createNotification(FuDocConstants.FU_DOC, message, notificationType)
                //新增查看同步结果
                .addAction(new PanelNotificationAction(syncResult, pinStatus, showPanel))
                //去接口文档系统查看文档(去YApi查看文档)
                .addAction(new BrowseNotificationAction(apiSystem, url))
                //新增给我点个小爱心按钮
                .addAction(new BrowseNotificationAction(starAction, UrlConstants.GITHUB))
                .notify(ProjectUtils.getCurrProject());
    }


    /**
     * 接口文档生成通知
     */
    public static void genNotify(NotificationType notificationType, String message, Project project) {
        String readDocAction = FuBundle.message(MessageConstants.READ_DOC_ACTION);
        String faqAction = FuBundle.message(MessageConstants.FAQ_ACTION);
        String starAction = FuBundle.message(MessageConstants.STAR_ACTION);

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
