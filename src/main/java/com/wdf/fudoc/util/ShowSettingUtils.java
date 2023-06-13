package com.wdf.fudoc.util;

import com.intellij.ide.actions.ShowSettingsUtilImpl;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.TabbedConfigurable;
import com.intellij.openapi.options.newEditor.SettingsDialogFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.navigation.Place;
import com.intellij.util.ui.update.Activatable;
import com.intellij.util.ui.update.UiNotifyConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Consumer;

/**
 * @author wangdingfu
 * @date 2023-05-30 10:48:04
 */
public class ShowSettingUtils extends ShowSettingsUtilImpl {


    public static void showConfigurable(Project project, Configurable configurable, int width, int height) {
        DialogWrapper dialogWrapper = editConfigurable1(project, configurable);
        dialogWrapper.setSize(width, height);
        dialogWrapper.show();
    }

    public static DialogWrapper editConfigurable1(Project project, @NotNull Configurable configurable) {
        return editConfigurable1(project, createDimensionKey(configurable), configurable, isWorthToShowApplyButton(configurable));
    }

    private static boolean isWorthToShowApplyButton(@NotNull Configurable configurable) {
        return configurable instanceof Place.Navigator ||
                configurable instanceof Composite ||
                configurable instanceof TabbedConfigurable;
    }

    public static DialogWrapper editConfigurable1(Project project, @NotNull String dimensionServiceKey, @NotNull Configurable configurable, boolean showApplyButton) {
        return editConfigurable(null, project, configurable, dimensionServiceKey, null, showApplyButton);
    }

    private static DialogWrapper editConfigurable(@Nullable Component parent,
                                                  @Nullable Project project,
                                                  @NotNull Configurable configurable,
                                                  @NotNull String dimensionKey,
                                                  @Nullable Runnable advancedInitialization,
                                                  boolean showApplyButton) {
        Consumer<Configurable> consumer = advancedInitialization != null ? it -> advancedInitialization.run() : null;
        return editConfigurable(parent, project, configurable, consumer, dimensionKey, showApplyButton);
    }

    private static <T extends Configurable> DialogWrapper editConfigurable(@Nullable Component parent,
                                                                           @Nullable Project project,
                                                                           @NotNull T configurable,
                                                                           @Nullable final Consumer<? super T> advancedInitialization,
                                                                           @NotNull String dimensionKey,
                                                                           boolean showApplyButton) {
        final DialogWrapper editor;
        if (parent == null) {
            editor = SettingsDialogFactory.getInstance().create(project, dimensionKey, configurable, showApplyButton, false);
        } else {
            editor = SettingsDialogFactory.getInstance().create(parent, dimensionKey, configurable, showApplyButton, false);
        }
        if (advancedInitialization != null) {
            new UiNotifyConnector.Once(editor.getContentPane(), new Activatable() {
                @Override
                public void showNotify() {
                    advancedInitialization.accept(configurable);
                }
            });
        }
        return editor;
    }

}
