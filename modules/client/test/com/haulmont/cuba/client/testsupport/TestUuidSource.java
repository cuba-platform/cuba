/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
