/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.WindowConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
@ManagedBean("cuba_CachingFacadeMBean")
public class CachingFacade implements CachingFacadeMBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private Scripting scripting;

    @Inject
    private Messages messages;

    @Inject
    private PersistenceManagerClient persistenceManagerClient;

    @Inject
    private ViewRepository viewRepository;

    @Inject
    private WindowConfig windowConfig;

    @Inject
    private MenuConfig menuConfig;

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

    @Override
    public void clearViewRepositoryCache() {
        ((AbstractViewRepository) viewRepository).reset();
    }

    @Override
    public void clearWindowConfig() {
        windowConfig.reset();
    }

    @Override
    public void clearMenuConfig() {
        menuConfig.reset();
    }
}
