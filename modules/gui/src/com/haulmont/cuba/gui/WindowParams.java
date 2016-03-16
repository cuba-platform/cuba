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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * Describes special window parameters that are set by system mechanisms such as {@link WindowManager}.
 * Some of the parameters are intended for use by application code, others affect low-level window opening behaviour.
 *
 */
public enum WindowParams {

    /**
     * Entity instance which is passed to an editor's controller <code>init</code> method from {@link WindowManager}.
     */
    ITEM,

    /**
     * Folder's UUID if the screen is opening from <code>FoldersPane</code>.
     */
    FOLDER_ID,

    /**
     * Window caption that can be set by controller in its <code>init</code> method.
     */
    CAPTION,

    /**
     * Window description that can be set by controller in its <code>init</code> method.
     * Description is shown as window tab tooltip.
     */
    DESCRIPTION,

    /**
     * If this parameter is set, a lookup screen can set up its lookup component (usually Table) for multi-selection.
     */
    MULTI_SELECT,

    /**
     * Prevents datasources from automatic refresh caused by visual components.
     */
    DISABLE_AUTO_REFRESH,

    /**
     * Disables window settings applying after showing a window.
     */
    DISABLE_APPLY_SETTINGS,

    /**
     * Disables suspendable datasources resuming after showing a window.
     */
    DISABLE_RESUME_SUSPENDED;

    /**
     * Get boolean value from the WindowContext.
     * @param context   window context
     * @return          parameter value
     */
    public boolean getBool(@Nullable FrameContext context) {
        return context != null && BooleanUtils.isTrue((Boolean) context.getParams().get(name()));
    }

    /**
     * Get string value from the WindowContext.
     * @param context   window context
     * @return          parameter value
     */
    public String getString(@Nullable FrameContext context) {
        if (context == null)
            return null;
        else
            return (String) context.getParams().get(name());
    }

    /**
     * Get string value from the parameters map.
     * @param params    parameters map
     * @return          parameter value
     */
    public String getString(Map<String, Object> params) {
        return (String) params.get(name());
    }

    /**
     * Get Entity value from the parameters map.
     * @param params    parameters map
     * @return          parameter value
     */
    public Entity getEntity(Map<String, Object> params) {
        return (Entity) params.get(name());
    }

    /**
     * Get UUID value from the parameters map.
     * @param params    parameters map
     * @return          parameter value
     */
    public UUID getUuid(Map<String, Object> params) {
        return (UUID) params.get(name());
    }

    /**
     * Get value from the WindowContext.
     * @param context   window context
     * @return          parameter value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(@Nullable FrameContext context) {
        if (context == null)
            return null;
        else
            return (T) context.getParams().get(name());
    }

    /**
     * Get value from the parameters map.
     * @param params    parameters map
     * @return          parameter value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Map<String, Object> params) {
        return (T) params.get(name());
    }

    /**
     * Set value in the parameters map.
     * @param params    window parameters map
     * @param value     parameter value
     */
    public void set(Map<String, Object> params, Object value) {
        params.put(name(), value);
    }
}
