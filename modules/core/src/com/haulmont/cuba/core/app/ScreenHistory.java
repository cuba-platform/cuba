/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

/**
 * Provides some functionality for history of opened screens.
 *
 * @author novikov
 * @version $Id$
 */
public interface ScreenHistory {

    String NAME = "cuba_ScreenHistory";

    int MAX_RECORDS = 100;

    void cleanup();
}
