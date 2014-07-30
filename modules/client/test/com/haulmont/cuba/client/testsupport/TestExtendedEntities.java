/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;

/**
 * @author artamonov
 * @version $Id$
 */
public class TestExtendedEntities extends ExtendedEntities {

    public TestExtendedEntities(Metadata metadata) {
        super(metadata);
    }
}