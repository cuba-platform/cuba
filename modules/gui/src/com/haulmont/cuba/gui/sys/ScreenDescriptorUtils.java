package com.haulmont.cuba.gui.sys;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public final class ScreenDescriptorUtils {

    private ScreenDescriptorUtils() {
    }

    public static String getInferredScreenId(UiController uiController, Class<? extends Screen> annotatedScreenClass) {
        checkNotNullArgument(uiController);

        String id = uiController.value();
        if (Strings.isNullOrEmpty(id)) {
            id = uiController.id();

            if (Strings.isNullOrEmpty(id)) {
                throw new DevelopmentException("Screen class annotated with @UiController without id " + annotatedScreenClass);
            }
        }

        return id;
    }

    public static String getInferredDesignTemplate(UiDescriptor uiDescriptor, Class<? extends Screen> annotatedScreenClass) {
        checkNotNullArgument(uiDescriptor);

        String templateLocation = uiDescriptor.value();
        if (Strings.isNullOrEmpty(templateLocation)) {
            templateLocation = uiDescriptor.path();

            if (Strings.isNullOrEmpty(templateLocation)) {
                throw new DevelopmentException("Screen class annotated with @UiDescriptor without template: " + annotatedScreenClass);
            }
        }

        return templateLocation;
    }

    public static String getInferredSubscribeId(Subscribe subscribe) {
        checkNotNullArgument(subscribe);

        String target = subscribe.value();
        if (Strings.isNullOrEmpty(target)) {
            target = subscribe.id();
        }

        return target;
    }
}