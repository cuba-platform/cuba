/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.07.2009 16:13:44
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import java.util.Set;

/**
 * Parses JPQL query and returns some information about it
 */
public interface QueryParser {

    /** Get all parameter names */
    Set<String> getParamNames();

    /** Main entity alias */
    String getEntityAlias(String targetEntity);
}
