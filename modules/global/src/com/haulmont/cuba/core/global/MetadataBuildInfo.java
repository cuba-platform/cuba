/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * DTO to pass metadata information from middleware to clients.
 *
 * $Id$
 *
 * @author krivopustov
 */
public class MetadataBuildInfo implements Serializable {

    private static final long serialVersionUID = -7522972347506596997L;

    private Collection<String> persistentEntitiesPackages;

    private Collection<String> transientEntitiesPackages;

    private Map<String, Map<String, String>> entityAnnotations;

    public MetadataBuildInfo(Collection<String> persistentEntitiesPackages,
                             Collection<String> transientEntitiesPackages,
                             Map<String, Map<String, String>> entityAnnotations)
    {
        this.persistentEntitiesPackages = persistentEntitiesPackages;
        this.transientEntitiesPackages = transientEntitiesPackages;
        this.entityAnnotations = entityAnnotations;
    }

    /**
     * @return  Names of persistent entity packages.
     */
    public Collection<String> getPersistentEntitiesPackages() {
        return persistentEntitiesPackages;
    }

    /**
     * @return  Names of non-persistent entity packages.
     */
    public Collection<String> getTransientEntitiesPackages() {
        return transientEntitiesPackages;
    }

    /**
     * @return  Map of entity class name to annotations map, defined in <code>metadata.xml</code>.
     */
    public Map<String, Map<String, String>> getEntityAnnotations() {
        return entityAnnotations;
    }
}
