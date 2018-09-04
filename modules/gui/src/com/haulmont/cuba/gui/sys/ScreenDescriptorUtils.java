package com.haulmont.cuba.gui.sys;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.screen.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public final class ScreenDescriptorUtils {

    private ScreenDescriptorUtils() {
    }

    public static String getInferredScreenId(UiController uiController,
                                             Class<? extends FrameOwner> annotatedScreenClass) {
        checkNotNullArgument(uiController);
        checkNotNullArgument(annotatedScreenClass);

        return getInferredScreenId(uiController.id(), uiController.value(), annotatedScreenClass.getName());
    }

    public static String getInferredScreenId(String idAttribute, String valueAttribute, String className) {
        String id = valueAttribute;
        if (Strings.isNullOrEmpty(id)) {
            id = idAttribute;

            if (Strings.isNullOrEmpty(id)) {
                int indexOfDot = className.lastIndexOf('.');
                if (indexOfDot < 0) {
                    id = className;
                } else {
                    id = className.substring(indexOfDot + 1);
                }
            }
        }

        return id;
    }

    public static String getInferredTemplate(UiDescriptor uiDescriptor,
                                             Class<? extends FrameOwner> annotatedScreenClass) {
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