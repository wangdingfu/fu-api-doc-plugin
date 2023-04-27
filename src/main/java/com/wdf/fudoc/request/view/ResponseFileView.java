package com.wdf.fudoc.request.view;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.constant.PathConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.components.ChooseFileComponents;
import com.wdf.fudoc.components.listener.ChooseFileListener;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

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
    private FuResponseData fuResponseData;

    private String fileName;

    private static final String RESPONSE_SELECT_DIR = "RESPONSE_SELECT_DIR";

    public ResponseFileView() {
        this.rootPane = new JRootPane();
        initRootPane();
        //保存至桌面
        this.saveDesktopBtn.addActionListener(e -> downloadFile(PathConstants.DESKTOP_PATH));
        //保存至指定路径
        this.saveOtherBtn.addActionListener(e -> {
            //选择路径
            String title = FuDocMessageBundle.message(MessageConstants.FU_REQUEST_SELECT_DIR_TITLE);
            ChooseFileComponents chooseFileComponents = new ChooseFileComponents(RESPONSE_SELECT_DIR, title, null);
            chooseFileComponents.chooseFile(new ChooseFileListener() {
                @Override
                public boolean isFileSelectable(VirtualFile file) {
                    return file.isDirectory();
                }

                @Override
                public void selectedFile(File file) {
                    //下载文件
                    downloadFile(file.getPath());
                }
            });
        });
    }

    private void downloadFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            FuDocNotification.notifyWarn(FuDocMessageBundle.message(MessageConstants.FU_REQUEST_DOWNLOAD_FILE_FAIL));
        }
        ProgressManager.getInstance().run(new Task.Backgroundable(ProjectUtils.getCurrProject(), "Download file", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                File targetFile = FileUtil.file(filePath, fileName);
                if (targetFile.exists()) {
                    //生成新的文件名
                    targetFile = FileUtil.file(filePath, "【Fu Doc】" + RandomUtil.randomInt(100000) + "-" + fileName);
                }
                //文件临时暂存目录
                File srcFile = FileUtil.file(fuResponseData.getFilePath());
                if (!srcFile.exists()) {
                    //提示用户 历史请求的文件遗失 无法保存
                    FuDocNotification.notifyWarn(FuDocMessageBundle.message(MessageConstants.FU_REQUEST_DOWNLOAD_NOT_FILE));
                    return;
                }
                //将源文件拷贝至目标文件
                FileUtil.copyFile(srcFile, targetFile);
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


    public void resetDefaultBtn() {
        rootPane.setDefaultButton(this.saveDesktopBtn);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.fileNameLabel.setText("文件名称:  " + fileName);
    }
}
