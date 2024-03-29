package com.wdf.fudoc.futool.dtoconvert.domain.model;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.List;

public class GenerateContext {

    /** 工程对象 */
    private Project project;
    /** 文件 */
    private PsiFile psiFile;
    /** 数据上下文 */
    private DataContext dataContext;
    /** 编辑器 */
    private Editor editor;
    /** 元素 */
    private PsiElement psiElement;
    /** 位点 */
    private Integer offset;
    /** 文档 */
    private Document document;
    /** 行号 */
    private Integer lineNumber;
    /** 开始位置 */
    private Integer startOffset;
    /** 文本编辑 */
    private CharSequence editorText;
    private List<String> importList;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public PsiFile getPsiFile() {
        return psiFile;
    }

    public void setPsiFile(PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    public DataContext getDataContext() {
        return dataContext;
    }

    public void setDataContext(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public PsiElement getPsiElement() {
        return psiElement;
    }

    public void setPsiElement(PsiElement psiElement) {
        this.psiElement = psiElement;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    public CharSequence getEditorText() {
        return editorText;
    }

    public void setEditorText(CharSequence editorText) {
        this.editorText = editorText;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> importList) {
        this.importList = importList;
    }
}
