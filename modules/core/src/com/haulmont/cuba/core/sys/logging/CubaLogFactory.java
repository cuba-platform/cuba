/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.logging;

import org.apache.commons.logging.impl.LogFactoryImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;

public class CubaLogFactory extends LogFactoryImpl {

    @Override
    public Log getInstance(String s) throws LogConfigurationException {
        Log log = super.getInstance(s);
        return new CubaLogWrapper(log);
    }
}
