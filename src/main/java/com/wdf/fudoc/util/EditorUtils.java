package com.wdf.fudoc.util;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.LocalTimeCounter;
import cn.fudoc.common.util.ProjectUtils;
import com.wdf.fudoc.common.constant.FuDocConstants;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-09 11:31:45
 */
public class EditorUtils {

    public static Document createDocument(String value, FileType fileType) {
        return createDocument(value, fileType, null);
    }

    public static Document createDocument(String value, FileType fileType, Project project) {
        if (Objects.nonNull(fileType) && JsonFileType.INSTANCE.equals(fileType)) {
            //只有json格式才创建虚拟文件
            if (project == null) {
                project = ProjectUtils.getCurrProject();
            }
            final PsiFileFactory factory = PsiFileFactory.getInstance(project);
            final long stamp = LocalTimeCounter.currentTime();
            final PsiFile psiFile = factory.createFileFromText(FuDocConstants.FU_DOC_FILE + fileType.getDefaultExtension(), fileType, value, stamp, true, false);
            final Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
            if (Objects.nonNull(document)) {
                return document;
            }
        }
        return EditorFactory.getInstance().createDocument(value);
    }
}
