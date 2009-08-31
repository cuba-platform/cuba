/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 28.08.2009 15:39:06
 *
 * $Id$
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
