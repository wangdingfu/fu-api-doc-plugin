package com.wdf.fudoc.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * git工具类
 *
 * @author wangdingfu
 * @date 2023-09-05 16:00:13
 */
public class GitUtil {


    /**
     * 获取git用户名
     */
    public static String getGitGlobalUsername() {
        return executeCommand("git", "config", "--global", "user.name");
    }


    /**
     * 执行git命令 并返回git输出的第一行结果
     *
     * @param command git命令
     * @return git输出的第一行结果
     */
    public static String executeCommand(String... command) {
        String result = null;
        try {
            // 创建 ProcessBuilder 对象，并设置要执行的命令
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            // 执行命令，并读取命令输出
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 读取命令输出的结果
            String line = reader.readLine();
            if (StringUtils.isNotBlank(line)) {
                result = line.trim();
            }
            // 等待命令执行完成
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return result;
    }
}
