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

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.Map;
import java.util.function.Consumer;

public interface EntityLinkField<V> extends Field<V>, Component.Focusable {

    String NAME = "entityLinkField";

    @Nullable
    String getScreen();
    void setScreen(@Nullable String screen);

    /**
     * @return open type
     * @deprecated Use {@link #getOpenMode()} instead.
     */
    @Deprecated
    WindowManager.OpenType getScreenOpenType();

    /**
     * @param openType open type
     * @deprecated Use {@link #setOpenMode(OpenMode)} instead.
     */
    @Deprecated
    void setScreenOpenType(WindowManager.OpenType openType);

    /**
     * @return open mode for editor screen
     */
    OpenMode getOpenMode();

    /**
     * Sets open mode for editor screen.
     *
     * @param openMode open mode
     */
    void setOpenMode(OpenMode openMode);

    @Nullable
    Map<String, Object> getScreenParams();
    void setScreenParams(@Nullable Map<String, Object> params);

    /**
     * @return ScreenCloseListener or null if not set
     * @deprecated Use {@link Subscription} instead to unsubscribe.
     */
    @Deprecated
    @Nullable
    ScreenCloseListener getScreenCloseListener();

    /**
     * Sets listener to handle close window event. <b> Note, if screen extends {@link Screen} the window parameter will
     * be null. </b>
     *
     * @param closeListener a listener to set
     * @deprecated Use {@link #addEditorCloseListener(Consumer)} instead.
     */
    @Deprecated
    void setScreenCloseListener(@Nullable ScreenCloseListener closeListener);

    /**
     * Adds editor close listener.
     *
     * @param editorCloseListener a listener to set
     * @return subscription
     */
    Subscription addEditorCloseListener(Consumer<EditorCloseEvent> editorCloseListener);

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

    /**
     * Listener to handle close window event.
     *
     * @deprecated Use {@link #addEditorCloseListener(Consumer)}.
     */
    @Deprecated
    interface ScreenCloseListener {
        void windowClosed(@Nullable Window window, String actionId);
    }

    /**
     * Describes editor close event.
     */
    class EditorCloseEvent<V> extends EventObject {
        protected EditorScreen screen;
        protected String actionId;

        public EditorCloseEvent(EntityLinkField<V> source, EditorScreen screen, String actionId) {
            super(source);
            
            this.screen = screen;
            this.actionId = actionId;
        }

        @Nullable
        public EditorScreen getEditorScreen() {
            return screen;
        }

        @Nullable
        public String getActionId() {
            return actionId;
        }

        @Override
        public EntityLinkField<V> getSource() {
            //noinspection unchecked
            return (EntityLinkField<V>) super.getSource();
        }
    }
}