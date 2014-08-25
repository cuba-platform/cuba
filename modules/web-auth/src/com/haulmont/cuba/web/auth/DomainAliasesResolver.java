/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.auth;

/**
 * @author artamonov
 * @version $Id$
 */
public interface DomainAliasesResolver {

    public static final String NAME = "cuba_DomainResolver";

    String getDomainName(String alias);
}