package com.wdf.fudoc.request.view;

import lombok.Getter;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-11-24 18:30:21
 */
public class ResponseFileView {
    @Getter
    private JPanel rootPanel;
    private JButton saveDesktopBtn;
    private JButton saveOtherBtn;
    private JLabel fileNameLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;


    public ResponseFileView() {
    }


    public void setFileName(String fileName) {
        this.fileNameLabel.setText("文件名称:  " + fileName);
    }
}
