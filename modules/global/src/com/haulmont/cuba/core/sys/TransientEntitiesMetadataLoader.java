/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.model.Session;

import javax.annotation.ManagedBean;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_TransientEntitiesMetadataLoader")
public class TransientEntitiesMetadataLoader extends ChileMetadataLoader {

    public TransientEntitiesMetadataLoader() {
        super(null);
    }

    public void setSession(Session session) {
        this.session = session;
        this.annotationsLoader = createAnnotationsLoader(session);
    }
}
