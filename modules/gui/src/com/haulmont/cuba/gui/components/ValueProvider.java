/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public interface ValueProvider {

    @Nullable
    Map<String, Object> getValues();

    @Nullable
    Map<String, Object> getParameters();
}