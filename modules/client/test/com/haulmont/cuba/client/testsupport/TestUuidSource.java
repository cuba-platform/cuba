/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.global.UuidSource;

import java.util.UUID;

/**
 * @author hasanov
 * @version $Id$
 */
public class TestUuidSource implements UuidSource {
    @Override
    public UUID createUuid() {
        return UUID.randomUUID();
    }
}
