/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.testsupport;

import org.junit.Assert;

import java.io.Serializable;

import static com.haulmont.cuba.core.sys.serialization.SerializationSupport.deserialize;
import static com.haulmont.cuba.core.sys.serialization.SerializationSupport.serialize;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TestSupport {

    public static final UUID ADMIN_USER_ID = UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

    public static final UUID COMPANY_GROUP_ID = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");

    public static final UUID ADMIN_ROLE_ID = UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93");

    public static <T> T reserialize(Serializable object) throws Exception {
        if (object == null)
            return null;

        return (T) deserialize(serialize(object));
    }

    public static void assertFail(Runnable runnable) {
        try {
            runnable.run();
            Assert.fail();
        } catch (Exception ignored) {
        }
    }
}