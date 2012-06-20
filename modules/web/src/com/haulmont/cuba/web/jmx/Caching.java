/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.global.Messages;
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

    @Inject
    private Messages messages;

    @Inject
    private PersistenceManagerClient persistenceManagerClient;

    @Override
    public int getMessagesCacheSize() {
        return messages.getCacheSize();
    }

    @Override
    public void clearGroovyCache() {
        scripting.clearCache();
        log.info("Scripting provider cache has been cleared");
    }

    @Override
    public void clearMessagesCache() {
        messages.clearCache();
        log.info("Messages cache has been cleared");
    }

    @Override
    public void clearPersistenceManagerClientCache() {
        persistenceManagerClient.clearCache();
        log.info("PersistenceManagerClient cache has been cleared");
    }
}
