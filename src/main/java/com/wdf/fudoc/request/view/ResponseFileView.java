package com.wdf.fudoc.request.view;

import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import lombok.Getter;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-11-24 18:30:21
 */
public class ResponseFileView {
    private JPanel rootPanel;
    private JButton saveDesktopBtn;
    private JButton saveOtherBtn;
    private JLabel fileNameLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    @Getter
    private final JRootPane rootPane;


    public ResponseFileView() {
        this.rootPane = new JRootPane();
        initRootPane();
    }


    public void initRootPane() {
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.rootPanel);
        rootPane.setDefaultButton(this.saveDesktopBtn);
    }

    public void setFileName(String fileName) {
        this.fileNameLabel.setText("文件名称:  " + fileName);
    }
}
