/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Contains special window parameters which affect low-level window opening behaviour.
 *
 * @author krivopustov
 * @version $Id$
 */
public enum WindowParams {

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
     * Set value in the parameters map.
     * @param params    window parameters map
     * @param value     parameter value
     */
    public void set(Map<String, Object> params, Object value) {
        params.put(name(), value);
    }
}
