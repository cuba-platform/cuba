/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
