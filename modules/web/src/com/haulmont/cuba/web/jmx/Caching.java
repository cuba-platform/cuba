/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.Scripting;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
@ManagedBean("cuba_CachingMBean")
public class Caching implements CachingMBean {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    private Scripting scripting;

    @Override
    public void clearGroovyCache() {
        scripting.clearCache();
        log.info("Cleared scripting provider cache");
    }

    @Override
    public void clearMessagesCache() {
        MessageProvider.clearCache();
        log.info("Cleared messages cache");
    }
}
