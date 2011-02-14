/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 22.11.2010 16:37:30
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface ScreenHistory {

    String NAME = "cuba_ScreenHistory";

    int MAX_RECORDS = 100;

    void cleanup();
}
