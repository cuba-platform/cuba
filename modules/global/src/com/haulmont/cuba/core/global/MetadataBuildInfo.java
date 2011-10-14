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

    private Map<String, Map<String, Object>> entityAnnotations;

    private Map<String, String> replacedEntities;


    public MetadataBuildInfo(Collection<String> persistentEntitiesPackages,
                             Collection<String> transientEntitiesPackages,
                             Map<String, Map<String, Object>> entityAnnotations,
                             Map<String, String> replacedEntities)
    {
        this.persistentEntitiesPackages = persistentEntitiesPackages;
        this.transientEntitiesPackages = transientEntitiesPackages;
        this.entityAnnotations = entityAnnotations;
        this.replacedEntities = replacedEntities;
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
     * @return  Map of entity class name to annotations map.
     * <p>Annotations map contatins only annotations with String or Boolean value.</p>
     */
    public Map<String, Map<String, Object>> getEntityAnnotations() {
        return entityAnnotations;
    }

    /**
     * @return  Replaced entity class names map.
     */
    public Map<String, String> getReplacedEntities() {
        return replacedEntities;
    }
}
