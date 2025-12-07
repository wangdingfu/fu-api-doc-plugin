package com.wdf.fudoc.compat;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;

/**
 * JsonFileType 兼容类
 * 用于兼容 IDEA 2025+ 版本中 JsonFileType 的变化
 *
 * @author wangdingfu
 * @date 2025-01-10
 */
public class JsonFileTypeCompat {

    /**
     * 获取 JSON FileType 实例
     * 兼容不同版本的 IDEA
     */
    public static FileType getJsonFileType() {
        try {
            // 尝试使用反射获取 JsonFileType.INSTANCE (兼容旧版本)
            Class<?> jsonFileTypeClass = Class.forName("com.intellij.json.JsonFileType");
            java.lang.reflect.Field instanceField = jsonFileTypeClass.getDeclaredField("INSTANCE");
            return (FileType) instanceField.get(null);
        } catch (Exception e) {
            // 如果反射失败,使用 FileTypeManager (IDEA 2025+)
            FileType fileType = FileTypeManager.getInstance().findFileTypeByName("JSON");
            if (fileType != null) {
                return fileType;
            }
            // 最后的fallback
            return FileTypeManager.getInstance().getFileTypeByExtension("json");
        }
    }

    /**
     * 判断是否为 JSON 文件类型
     */
    public static boolean isJsonFileType(FileType fileType) {
        if (fileType == null) {
            return false;
        }
        FileType jsonType = getJsonFileType();
        return jsonType != null && jsonType.equals(fileType);
    }
}
