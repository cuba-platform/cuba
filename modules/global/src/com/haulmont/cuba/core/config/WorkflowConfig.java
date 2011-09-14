/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.config.defaults.DefaultBoolean;

/**
 * Workflow specific parameters
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@Prefix("workflow.")
@Source(type = SourceType.APP)
public interface WorkflowConfig extends Config {

    @DefaultBoolean(false)
    boolean getOneAttachmentUploaderEnabled();
    void setOneAttachmentUploaderEnabled(boolean value);
}