package com.wdf.fudoc.request.view;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import com.intellij.CommonBundle;
import com.intellij.ide.GeneralSettings;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.impl.ProjectNewWindowDoNotAskOption;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.constant.PathConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

/**
 * @author wangdingfu
 * @date 2022-11-24 18:30:21
 */
public class ResponseFileView {
    private JPanel rootPanel;
    private JButton saveDesktopBtn;
    private JButton saveOtherBtn;
    private JLabel fileNameLabel;
    @Getter
    private final JRootPane rootPane;

    @Setter
    private String downloadPath;

    @Setter
    private HttpResponse httpResponse;

    private String fileName;

    public ResponseFileView() {
        this.rootPane = new JRootPane();
        initRootPane();
        //保存至桌面
        this.saveDesktopBtn.addActionListener(e -> downloadFile(PathConstants.DESKTOP_PATH));
        //保存至指定路径
        this.saveOtherBtn.addActionListener(e -> downloadFile(this.downloadPath));
    }


    DialogWrapper.DoNotAskOption option = new DialogWrapper.DoNotAskOption() {
        @Override
        public boolean isToBeShown() {
            return GeneralSettings.getInstance().isConfirmExit() && ProjectManager.getInstance().getOpenProjects().length > 0;
        }

        @Override
        public void setToBeShown(boolean value, int exitCode) {
            GeneralSettings.getInstance().setConfirmExit(value);
        }

        @Override
        public boolean canBeHidden() {
            return false;
        }

        @Override
        public boolean shouldSaveOptionsOnCancel() {
            return false;
        }

        @Override
        public @NotNull String getDoNotShowMessage() {
            return IdeBundle.message("do.not.ask.me.again");
        }
    };

    private void downloadFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            FuDocNotification.notifyWarn(FuDocMessageBundle.message(MessageConstants.FU_REQUEST_DOWNLOAD_FILE_FAIL));
        }
        ProgressManager.getInstance().run(new Task.Backgroundable(ProjectUtils.getCurrProject(), "Download file", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                File file = FileUtil.file(filePath, fileName);
                if (file.exists()) {
                    //生成新的文件名
                    file = FileUtil.file(filePath, "【Fu Doc】" + RandomUtil.randomInt(100000) + "-" + fileName);
                }
                httpResponse.writeBody(FileUtil.getOutputStream(file), true, new StreamProgress() {
                    @Override
                    public void start() {
                        indicator.start();
                    }

                    @Override
                    public void progress(long total, long progressSize) {
                        indicator.setText("download..... " + NumberUtil.decimalFormat("#%", NumberUtil.div(progressSize, total)));
                    }

                    @Override
                    public void finish() {
                        indicator.stop();
                    }
                });
            }
        });
    }


    public void initRootPane() {
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.rootPanel);
        rootPane.setDefaultButton(this.saveDesktopBtn);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.fileNameLabel.setText("文件名称:  " + fileName);
    }
}
