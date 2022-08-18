package com.wdf.fudoc.common;

import com.intellij.icons.AllIcons;
import com.intellij.json.JsonLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.xml.XmlBundle;
import com.intellij.xml.psi.XmlPsiBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-08-18 10:51:00
 */
public class FtlFileType  extends LanguageFileType {
    public static final FtlFileType INSTANCE = new FtlFileType();
    public static final String DEFAULT_EXTENSION = "ftl";

    protected FtlFileType(Language language) {
        super(language);
    }

    protected FtlFileType(Language language, boolean secondary) {
        super(language, secondary);
    }

    protected FtlFileType() {
        super(JsonLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "ftl";
    }

    @NotNull
    @Override
    public String getDescription() {
        return XmlBundle.message(XmlPsiBundle.message("filetype.xml.description"));
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Xml;
    }
}
