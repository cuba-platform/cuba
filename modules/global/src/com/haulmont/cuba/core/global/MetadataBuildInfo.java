/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.io.Serializable;
import java.util.Collection;

/**
 * $Id$
 *
 * @author krivopustov
 */
public class MetadataBuildInfo implements Serializable {

    private static final long serialVersionUID = -7522972347506596997L;

    private Collection<String> persistentEntitiesPackages;

    private Collection<String> transientEntitiesPackages;

    public MetadataBuildInfo(Collection<String> persistentEntitiesPackages, Collection<String> transientEntitiesPackages) {
        this.persistentEntitiesPackages = persistentEntitiesPackages;
        this.transientEntitiesPackages = transientEntitiesPackages;
    }

    public Collection<String> getPersistentEntitiesPackages() {
        return persistentEntitiesPackages;
    }

    public Collection<String> getTransientEntitiesPackages() {
        return transientEntitiesPackages;
    }
}
