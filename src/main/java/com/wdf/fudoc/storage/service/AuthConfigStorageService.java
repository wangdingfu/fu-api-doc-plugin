//package com.wdf.fudoc.storage.service;
//
//import com.intellij.openapi.vfs.VfsUtil;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.wdf.fudoc.common.constant.FuPaths;
//import com.wdf.fudoc.request.pojo.AuthConfigData;
//import com.wdf.fudoc.request.pojo.BaseAuthConfig;
//import com.wdf.fudoc.request.pojo.FuHttpRequestData;
//import com.wdf.fudoc.request.pojo.ScriptConfigData;
//import com.wdf.fudoc.util.JsonUtil;
//import com.wdf.fudoc.util.StorageUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.MapUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import java.nio.charset.Charset;
//import java.nio.file.Paths;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * 鉴权配置数据持久化service
// *
// * @author wangdingfu
// * @date 2023-06-06 20:36:21
// */
//@Slf4j
//public class AuthConfigStorageService {
//
//    private static final String HTTP_FILE = "request.json";
//
//
//    public static AuthConfigData read(String name) {
//        AuthConfigData authConfigData = new AuthConfigData();
//        try {
//            VirtualFile file = VfsUtil.findFile(Paths.get(FuPaths.AUTH_PATH, name), true);
//            if (Objects.nonNull(file) && file.isDirectory()) {
//                VirtualFile[] children = file.getChildren();
//                if (Objects.nonNull(children)) {
//                    for (VirtualFile child : children) {
//                        if (!child.exists()) {
//                            continue;
//                        }
//                        String fileContent = StringUtils.toEncodedString(child.contentsToByteArray(), Charset.defaultCharset());
//                        if (StringUtils.isBlank(fileContent)) {
//                            continue;
//                        }
//                        String fileName = child.getName();
//                        if (HTTP_FILE.equals(fileName)) {
//                            authConfigData.setHttpRequestData(JsonUtil.toBean(fileContent, FuHttpRequestData.class));
//                        } else {
//                            String tabName = StringUtils.substringBeforeLast(fileName, ".");
//                            ScriptConfigData scriptConfigData = new ScriptConfigData();
//                            scriptConfigData.setScript(fileContent);
//                            authConfigData.addAuthConfig(tabName, scriptConfigData);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.info("读取鉴权配置文件失败", e);
//        }
//        authConfigData.setName(name);
//        return authConfigData;
//    }
//
//
//    public static void write(AuthConfigData authConfigData) {
//        String name = authConfigData.getName();
//        //保存http请求
//        FuHttpRequestData httpRequestData = authConfigData.getHttpRequestData();
//        if (StringUtils.isNotBlank(name) && Objects.nonNull(httpRequestData)) {
//            StorageUtils.writeJson(Paths.get(FuPaths.AUTH_PATH, name).toString(), HTTP_FILE, httpRequestData);
//        }
//
//        //保存脚本配置
//        Map<String, BaseAuthConfig> authConfigMap = authConfigData.getAuthConfigMap();
//        if (MapUtils.isNotEmpty(authConfigMap)) {
//            authConfigMap.forEach((key, value) -> StorageUtils.write(Paths.get(FuPaths.AUTH_PATH, name).toString(), key + "." + value.getSuffix(), value.getScript()));
//        }
//    }
//}
