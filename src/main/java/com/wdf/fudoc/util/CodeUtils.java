package com.wdf.fudoc.util;

import java.util.Scanner;

/**
 * @author wangdingfu
 * @date 2023-01-12 23:44:20
 */
public class CodeUtils {


    public static String getCpuId() {
        try {
            String[] linux = {"dmidecode", "-t", "processor", "|", "grep", "'ID'"};
            String[] windows = {"wmic", "cpu", "get", "ProcessorId"};

            // 获取系统信息
            String property = System.getProperty("os.name");
            Process process = Runtime.getRuntime().exec(property.contains("Window") ? windows : linux);
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream(), "utf-8");
            sc.next();
            return sc.next();
        } catch (Exception e) {
            return null;
        }

    }

}
