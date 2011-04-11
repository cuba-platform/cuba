/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.ServiceLocator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServiceLocatorImpl extends ServiceLocator
{
    protected Context __getJndiContext() {
        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return jndiContext;
    }

    protected Object __lookup(String name) {
        return AppContext.getApplicationContext().getBean(name);
    }

}
