package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.util.FuStringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-08 16:06:57
 */
@Getter
@Setter
public class ConfigEnvTableBO extends DynamicTableBO {

    /**
     * 环境名称
     */
    private String envName;

    /**
     * 域名
     */
    private String domain;

    /**
     * 应用
     */
    private String application;

    /**
     * 环境描述
     */
    private String description;

    /**
     * 端口号（单独存储，方便管理）
     */
    private Integer port;

    /**
     * 上下文路径
     */
    private String contextPath;

    /**
     * 请求头配置
     */
    private String headers;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 环境类型（dev/test/prod）
     */
    private String envType;

    /**
     * 获取端口的字符串表示（用于表格显示）
     *
     * @return 端口的字符串，如果为null则返回空字符串
     */
    public String getPortDisplay() {
        return port != null ? port.toString() : "";
    }

    /**
     * 设置端口（从字符串）
     *
     * @param portString 端口字符串
     */
    public void setPortDisplay(String portString) {
        if (FuStringUtils.isBlank(portString)) {
            this.port = null;
        } else {
            try {
                this.port = Integer.parseInt(portString);
            } catch (NumberFormatException e) {
                this.port = null;
            }
        }
    }

    /**
     * 获取完整的URL
     *
     * @return 完整的URL
     */
    public String getFullUrl() {
        StringBuilder sb = new StringBuilder();
        if (domain != null && !domain.startsWith("http")) {
            sb.append("http://");
        }
        sb.append(domain);

        if (port != null && !domain.contains(":" + port)) {
            sb.append(":").append(port);
        }

        if (contextPath != null && !contextPath.isEmpty()) {
            if (!contextPath.startsWith("/")) {
                sb.append("/");
            }
            sb.append(contextPath);
        }

        return sb.toString();
    }
}
