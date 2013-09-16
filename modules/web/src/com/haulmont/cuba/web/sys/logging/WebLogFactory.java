/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.impl.LogFactoryImpl;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class WebLogFactory extends LogFactoryImpl {

    @Override
    public Log getInstance(String s) throws LogConfigurationException {
        Log log = super.getInstance(s);
        return new WebLogWrapper(log);
    }
}
