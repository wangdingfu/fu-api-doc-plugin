package com.wdf.fudoc.request.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-12-18 23:33:48
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpringBootEnvConfigInfo {

    private Integer serverPort;

    private String contextPath;
}
