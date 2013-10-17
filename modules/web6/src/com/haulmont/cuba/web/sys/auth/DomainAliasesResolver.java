/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.auth;

/**
 * @author artamonov
 * @version $Id$
 */
public interface DomainAliasesResolver {

    public static final String NAME = "cuba_DomainResolver";

    String getDomainName(String alias);
}
