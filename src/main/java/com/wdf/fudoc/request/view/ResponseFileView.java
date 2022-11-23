package com.wdf.fudoc.request.view;

import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2022-11-23 22:01:33
 */
public class ResponseFileView {


    @Getter
    private final JRootPane rootPane;
    private final JPanel mainPanel;

    private final JLabel fileNameLabel;

    /**
     * 下载文件按钮
     */
    private final JButton downloadFileBtn;

    public ResponseFileView() {
        this.rootPane = new JRootPane();
        this.mainPanel = new JPanel(new BorderLayout());
        this.fileNameLabel = new JLabel("");
        this.downloadFileBtn = new JButton("下载文件");
        this.mainPanel.add(this.fileNameLabel, BorderLayout.WEST);
        this.mainPanel.add(this.downloadFileBtn, BorderLayout.CENTER);
        initRootPane();
    }


    public void setFileName(String fileName) {
        this.fileNameLabel.setText(fileName);
    }


    public void initRootPane() {
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.mainPanel);
        rootPane.setDefaultButton(this.downloadFileBtn);
    }
}
