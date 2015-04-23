/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.SecurityImpl;

/**
 * @author artamonov
 * @version $Id$
 */
public class TestSecurity extends SecurityImpl {

    public TestSecurity(UserSessionSource sessionSource, Metadata metadata, ExtendedEntities extendedEntities) {
        this.userSessionSource = sessionSource;
        this.metadata = metadata;
        this.metadataTools = metadata.getTools();
        this.extendedEntities = extendedEntities;
    }
}