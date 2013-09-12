/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import java.util.Set;

/**
 * Parses JPQL query and returns some information about it.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface QueryParser {

    /** Get all parameter names */
    Set<String> getParamNames();

    /** Main entity name */
    String getEntityName();

    /** Main entity alias */
    String getEntityAlias(String targetEntity);

    /** Returns true if this is a standard select from an entity - not count() and not fields (e.id, etc.) */
    boolean isEntitySelect(String targetEntity);
}
