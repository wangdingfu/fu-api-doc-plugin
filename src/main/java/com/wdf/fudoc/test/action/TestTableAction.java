package com.wdf.fudoc.test.action;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.wdf.fudoc.util.*;
import org.jetbrains.annotations.NotNull;

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
