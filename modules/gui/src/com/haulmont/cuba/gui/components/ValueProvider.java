/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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