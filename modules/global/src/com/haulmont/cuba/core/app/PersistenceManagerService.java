/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

/**
 * Exposes some of {@code PersistenceManagerAPI} methods to the client tier.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface PersistenceManagerService {

    String NAME = "cuba_PersistenceManagerService";

    boolean useLazyCollection(String entityName);

    boolean useLookupScreen(String entityName);

    int getFetchUI(String entityName);

    int getMaxFetchUI(String entityName);
}
