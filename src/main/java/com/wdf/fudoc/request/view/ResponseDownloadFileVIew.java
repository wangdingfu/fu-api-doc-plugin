package com.wdf.fudoc.request.view;

import lombok.Getter;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-11-23 21:51:14
 */
public class ResponseDownloadFileVIew {
    @Getter
    private JPanel rootPanel;
    private JPanel showPanel;
    private JPanel downloadPanel;
    private JLabel fileNameLabel;
    private JButton downloadFileBtn;

    public ResponseDownloadFileVIew() {
    }


    public void setFileName(String fileName){
        this.fileNameLabel.setText(fileName);
    }
}
