/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Describes special window parameters that are set by system mechanisms such as {@link WindowManager}.
 * Some of the parameters are intended for use by application code, others affect low-level window opening behaviour.
 *
 * @author krivopustov
 * @version $Id$
 */
public enum WindowParams {

    /**
     * Entity instance which is passed to an editor's controller <code>init</code> method from {@link WindowManager}.
     */
    ITEM,

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
    public boolean getBool(@Nullable WindowContext context) {
        return context != null && BooleanUtils.isTrue((Boolean) context.getParams().get(name()));
    }

    /**
     * Get string value from the WindowContext.
     * @param context   window context
     * @return          parameter value
     */
    public String getString(@Nullable WindowContext context) {
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
    public <T extends Entity> T getEntity(Map<String, Object> params) {
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
