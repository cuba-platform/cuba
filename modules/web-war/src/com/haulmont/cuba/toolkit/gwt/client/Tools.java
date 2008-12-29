/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 19.12.2008 14:12:50
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client;

import com.haulmont.cuba.toolkit.gwt.client.impl.ToolsImpl;

public class Tools {
    private static ToolsImpl impl;

    static {
        impl = new ToolsImpl();
    }

    public static int parseSize(String s) {
        return impl.parseSize(s);
    }
}
