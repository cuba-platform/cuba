/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 09.12.2008 10:32:57
 * $Id: MetadataObjectInitTask.java 425 2009-06-22 13:20:24Z krivopustov $
 */
package com.haulmont.chile.core.loader;

public interface MetadataObjectInitTask {
    String getWarning();
    void execute();
}
