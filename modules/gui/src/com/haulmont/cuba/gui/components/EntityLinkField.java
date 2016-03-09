/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.DialogOptions;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author artamonov
 */
public interface EntityLinkField extends Field {

    String NAME = "entityLinkField";

    @Nullable
    String getScreen();
    void setScreen(@Nullable String screen);

    WindowManager.OpenType getScreenOpenType();
    void setScreenOpenType(WindowManager.OpenType openType);

    @Deprecated
    @Nullable
    DialogParams getScreenDialogParams();
    /**
     * @deprecated Use {@link #setScreenOpenType(WindowManager.OpenType)}
     */
    @Deprecated
    void setScreenDialogParams(@Nullable DialogParams dialogParams);

    @Nullable
    Map<String, Object> getScreenParams();
    void setScreenParams(@Nullable Map<String, Object> params);

    @Nullable
    ScreenCloseListener getScreenCloseListener();
    void setScreenCloseListener(@Nullable ScreenCloseListener closeListener);

    @Nullable
    EntityLinkClickHandler getCustomClickHandler();
    void setCustomClickHandler(@Nullable EntityLinkClickHandler clickHandler);

    MetaClass getMetaClass();
    void setMetaClass(MetaClass metaClass);

    ListComponent getOwner();
    void setOwner(ListComponent owner);

    interface EntityLinkClickHandler {

        void onClick(EntityLinkField field);
    }

    interface ScreenCloseListener {

        void windowClosed(Window window, String actionId);
    }
}