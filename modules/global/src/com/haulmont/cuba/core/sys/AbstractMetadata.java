/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ViewRepository;

import java.util.Collection;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractMetadata implements Metadata {

    protected volatile Session session;

    protected volatile ViewRepository viewRepository;

    protected volatile Map<Class, Class> replacedEntities;

    @Override
    public Session getSession() {
        if (session == null) {
            synchronized (this) {
                if (session == null) {
                    session = initMetadata();
                }
            }
        }
        return session;
    }

    @Override
    public ViewRepository getViewRepository() {
        if (viewRepository == null) {
            synchronized (this) {
                if (viewRepository == null) {
                    viewRepository = initViews();
                }
            }
        }
        return viewRepository;
    }

    @Override
    public Map<Class, Class> getReplacedEntities() {
        if (replacedEntities == null) {
            synchronized (this) {
                if (replacedEntities == null) {
                    replacedEntities = initReplacedEntities();
                }
            }
        }
        return replacedEntities;
    }

    protected abstract Session initMetadata();

    protected abstract ViewRepository initViews();

    protected abstract Map<Class,Class> initReplacedEntities();

    protected void loadMetadata(MetadataLoader loader, Collection<String> packages) {
        for (String p : packages) {
            loader.loadPackage(p, p);
        }
    }
}
