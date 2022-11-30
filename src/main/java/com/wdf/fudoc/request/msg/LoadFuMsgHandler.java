package com.wdf.fudoc.request.msg;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.wdf.fudoc.common.CommonResult;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.components.bo.FuMsgBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * 加载需要提示的消息
 *
 * @author wangdingfu
 * @date 2022-11-30 16:07:39
 */
@Slf4j
public class LoadFuMsgHandler implements StartupActivity.Background {

    private static final String URL = UrlConstants.FU_DOC_URL + "/fu_doc_msg/list";

    /**
     * idea启动后动态获取需要提示的消息
     */
    @Override
    public void runActivity(@NotNull Project project) {
        ThreadUtil.execAsync(() -> {
            try {
                HttpResponse httpResponse = HttpRequest.get(URL).timeout(6000).executeAsync();
                String result;
                if (Objects.nonNull(httpResponse) && StringUtils.isNotBlank(result = httpResponse.body())) {
                    CommonResult<List<FuMsgBO>> resultData = JSONUtil.toBean(result, new TypeReference<>() {
                    }, false);
                    List<FuMsgBO> fuMsgBOList;
                    if (Objects.nonNull(resultData) && CollectionUtils.isNotEmpty(fuMsgBOList = resultData.getData())) {
                        fuMsgBOList.forEach(FuMsgManager::addMsg);
                    }
                }
            } catch (Exception e) {
                //加载提示信息异常
                log.info("动态加载需要提示的信息异常", e);
            }
        });
    }
}
