package com.wdf.fudoc.util;

import java.util.regex.Pattern;

/**
 * URL 构建工具类
 * 提供域名、端口、上下文路径等 URL 相关的处理方法
 *
 * @author wangdingfu
 * @date 2023-12-05
 */
public class UrlBuildUtils {

    // 预编译正则表达式，提升性能
    private static final Pattern URL_WITH_PORT = Pattern.compile("^[a-zA-Z]+://[^:/]+:\\d+.*$");
    private static final Pattern URL_WITHOUT_PORT = Pattern.compile("^[a-zA-Z]+://[^:/]+$");

    private UrlBuildUtils() {
        // 工具类不允许实例化
    }

    /**
     * 构建完整的URL（包含协议和端口）
     *
     * @param domain 域名
     * @param port   端口
     * @return 完整的URL
     */
    public static String buildFullUrl(String domain, Integer port) {
        if (FuStringUtils.isBlank(domain)) {
            return "";
        }

        String result = domain.trim();

        // 确保域名有协议前缀
        if (!result.startsWith("http://") && !result.startsWith("https://")) {
            result = "http://" + result;
        }

        // 添加端口（如果需要）
        if (port != null && !hasPort(result)) {
            result = result + ":" + port;
        }

        return result;
    }

    /**
     * 构建完整的URL（包含协议、端口和上下文路径）
     *
     * @param domain      域名
     * @param port        端口
     * @param contextPath 上下文路径
     * @return 完整的URL
     */
    public static String buildFullUrl(String domain, Integer port, String contextPath) {
        String url = buildFullUrl(domain, port);
        if (FuStringUtils.isBlank(url)) {
            return "";
        }

        // 移除 URL 末尾的斜杠
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        // 添加上下文路径
        if (FuStringUtils.isNotBlank(contextPath)) {
            String ctxPath = contextPath.trim();
            if (!ctxPath.startsWith("/")) {
                ctxPath = "/" + ctxPath;
            }
            url = url + ctxPath;
        }

        return url;
    }

    /**
     * 检查 URL 是否已包含端口号
     *
     * @param url URL
     * @return 是否包含端口
     */
    public static boolean hasPort(String url) {
        if (FuStringUtils.isBlank(url)) {
            return false;
        }
        return URL_WITH_PORT.matcher(url).matches();
    }

    /**
     * 确保域名有协议前缀
     *
     * @param domain 域名
     * @return 带协议的域名
     */
    public static String ensureProtocol(String domain) {
        if (FuStringUtils.isBlank(domain)) {
            return "";
        }

        String result = domain.trim();
        if (!result.startsWith("http://") && !result.startsWith("https://")) {
            result = "http://" + result;
        }
        return result;
    }

    /**
     * 规范化上下文路径（移除开头的斜杠）
     *
     * @param contextPath 上下文路径
     * @return 规范化后的路径（不以斜杠开头）
     */
    public static String normalizeContextPath(String contextPath) {
        if (FuStringUtils.isBlank(contextPath)) {
            return null;
        }

        String result = contextPath.trim();
        if (result.startsWith("/")) {
            result = result.substring(1);
        }
        return result;
    }
}
