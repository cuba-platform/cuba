package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.gui.screen.FrameOwner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface WindowAttributesProvider {

    WindowInfo.Type getType(WindowInfo windowInfo);

    @Nullable
    String getTemplate(WindowInfo windowInfo);

    @Nullable
    Boolean isMultiOpen(WindowInfo windowInfo);

    @Nonnull
    Class<? extends FrameOwner> getControllerClass(WindowInfo windowInfo);
}