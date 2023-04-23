package com.wdf.fudoc.test.action;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.impl.PsiJavaParserFacadeImpl;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.impl.file.PsiFileImplUtil;
import com.intellij.psi.impl.file.impl.FileManager;
import com.intellij.psi.impl.file.impl.FileManagerImpl;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileSystemItemUtil;
import com.intellij.psi.util.FindClassUtil;
import com.intellij.psi.util.PsiClassUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.LocalTimeCounter;
import com.intellij.util.ResourceUtil;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.FuDocTest;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiCategoryDialog;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.message.FuMessageComponent;
import com.wdf.fudoc.components.message.FuMsgBuilder;
import com.wdf.fudoc.spring.SpringConfigManager;
import com.wdf.fudoc.util.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2022-09-05 19:39:54
 */
public class TestTableAction extends AnAction {

    private static final String TEST_JAVA = "package com.wdf.fudoc;\n" +
            "\n" +
            "import cn.hutool.json.JSON;\n" +
            "\n" +
            "/**\n" +
            " * @author wangdingfu\n" +
            " * @date 2023-03-12 17:40:35\n" +
            " */\n" +
            "public class TestJava {\n" +
            "\n" +
            "    public void paddingData(JSON response, JSON fuDoc) {\n" +
            "        fuDoc.putByPath(\"token\", response.getByPath(\"data.token\"));\n" +
            "    }\n" +
            "}\n";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        EditorFactory editorFactory = EditorFactory.getInstance();
        PsiElement targetElement = PsiClassUtils.getTargetElement(e);
        PsiClass psiClass = PsiClassUtils.getPsiClass(targetElement);
//        PsiJavaParserFacade psiJavaParserFacade = new PsiJavaParserFacadeImpl(e.getProject());
//        PsiClass classFromText = psiJavaParserFacade.createClassFromText(TEST_JAVA, targetElement);
//        Document document = PsiDocumentManager.getInstance(e.getProject()).getDocument(classFromText.getContainingFile());
//
//        Editor editor = editorFactory.createEditor(document, e.getProject(), JavaFileType.INSTANCE, false);
//        PopupUtils.create(editor.getComponent(),null,new AtomicBoolean(true));
        PsiFileFactory instance = PsiFileFactory.getInstance(e.getProject());
        PsiFile fileFromText = instance.createFileFromText("testJava.java", JavaFileType.INSTANCE, TEST_JAVA);
        Document document = PsiDocumentManager.getInstance(e.getProject()).getDocument(fileFromText);
                Editor editor = editorFactory.createEditor(document, e.getProject(), JavaFileType.INSTANCE, false);
        PopupUtils.create(editor.getComponent(),null,new AtomicBoolean(true));
    }

    private static EditorImpl createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        return (EditorImpl)EditorFactory.getInstance().createEditor(document, project, EditorKind.MAIN_EDITOR);
    }
}
