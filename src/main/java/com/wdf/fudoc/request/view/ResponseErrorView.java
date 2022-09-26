package com.wdf.fudoc.request.view;

import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.FuEditorComponent;
import icons.FuDocIcons;
import lombok.Getter;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-26 16:00:32
 */
public class ResponseErrorView {
    @Getter
    private JPanel rootPanel;
    private JPanel imgPanel;
    private JPanel textPanel;
    private JLabel iconLabel;
    private JLabel tipLabel;

    private FuEditorComponent fuEditorComponent;

    public ResponseErrorView() {
        imgPanel.setBorder(JBUI.Borders.empty(10,0,20,0));
        tipLabel.setBorder(JBUI.Borders.emptyBottom(20));
        iconLabel.setIcon(FuDocIcons.FU_REQUEST);
        setErrorDetail("错误：connect ECONNREFUSED 127.0.0.1:8012");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
        this.textPanel = fuEditorComponent.getMainPanel();
    }


    public void setErrorDetail(String errorDetail){
        if(Objects.nonNull(errorDetail)){
            fuEditorComponent.setContent(errorDetail);
        }
    }
}
