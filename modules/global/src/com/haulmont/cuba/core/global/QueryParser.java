/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

    String getEntityAlias();

    /** Returns true if this is a standard select from an entity - not count() and not fields (e.id, etc.) */
    boolean isEntitySelect(String targetEntity);
}
