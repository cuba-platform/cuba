/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

/**
 * @author zlatoverov
 * @version $Id$
 */
@Source(type = SourceType.DATABASE)
public interface ExceptionHandlersConfig extends Config {

    @Property("cuba.uniqueConstraintViolationPattern")
    String getUniqueConstraintViolationPattern();
    void setUniqueConstraintViolationPattern(String uniqueConstraintViolationPattern);
}