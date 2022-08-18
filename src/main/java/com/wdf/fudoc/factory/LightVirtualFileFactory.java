package com.wdf.fudoc.factory;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.testFramework.LightVirtualFile;
import com.wdf.fudoc.common.FtlFileType;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-08-18 10:46:35
 */
public class LightVirtualFileFactory {

    private static final String FILE_NAME = "fu_doc";

    private static final String JAVA_SUFFIX = ".java";
    private static final String XML_SUFFIX = ".xml";
    private static final String FTL_SUFFIX = ".ftl";
    private static final String JSON_SUFFIX = ".json";
    private static final String VM_SUFFIX = ".java.vm";

    public static LightVirtualFile create(FileType fileType) {
        if (Objects.nonNull(fileType)) {
            if (fileType instanceof XmlFileType) {
                return new LightVirtualFile(FILE_NAME + XML_SUFFIX);
            }
            if (fileType instanceof JavaFileType) {
                return new LightVirtualFile(FILE_NAME + JAVA_SUFFIX);
            }
            if (fileType instanceof JsonFileType) {
                return new LightVirtualFile(FILE_NAME + JSON_SUFFIX);
            }
            if (fileType instanceof FtlFileType) {
                return new LightVirtualFile(FILE_NAME + FTL_SUFFIX);
            }
        }
        //默认返回vm类型
        return new LightVirtualFile(FILE_NAME + VM_SUFFIX);
    }
}
