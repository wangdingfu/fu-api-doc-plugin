package com.wdfu.fudoc;

import cn.hutool.system.oshi.OshiUtil;
import com.wdf.fudoc.request.view.ResponseErrorView;
import oshi.hardware.ComputerSystem;

import javax.swing.*;

/**
 * @author wangdingfu
 * @Description
 * @date 2022-08-06 21:42:27
 */
public class FuDocSettingTest {

    public static void main1(String[] args) {
        JFrame jFrame = new JFrame();
        ResponseErrorView responseErrorView = new ResponseErrorView();
        jFrame.add(responseErrorView.getRootPanel());
        jFrame.setVisible(true);
        //        关闭事件
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        ComputerSystem system = OshiUtil.getSystem();
        System.out.println(system.getHardwareUUID());
    }
}
