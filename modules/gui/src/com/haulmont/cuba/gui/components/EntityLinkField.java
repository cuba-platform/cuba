/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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