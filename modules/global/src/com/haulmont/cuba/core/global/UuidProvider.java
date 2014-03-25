/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Provides static method to create UUIDs.
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class UuidProvider {

    private static volatile UuidSource uuidSource;

    /**
     * @return new UUID
     */
    public static UUID createUuid() {
        if (AppContext.getApplicationContext() == null) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            return new UUID(random.nextLong(), random.nextLong());
        }

        if (uuidSource == null) {
            uuidSource = AppBeans.get(UuidSource.NAME, UuidSource.class);
        }
        return uuidSource.createUuid();
    }

    /**
     * Fast implementation of creating a {@code UUID} from the standard string representation.
     *
     * <p/>Borrowed from Apache Jackrabbit project which is licensed under the Apache License, Version 2.0.
     * See http://www.apache.org/licenses/LICENSE-2.0.
     *
     * @param str   a string according to {@link java.util.UUID#toString()} rules
     * @return      UUID instance
     */
    public static UUID fromString(String str) {
        if (str == null)
            return null;

        if (str.length() != 36) {
            throw new IllegalArgumentException(str);
        }
        long m = 0, x = 0;
        for (int i = 0; i < 36; i++) {
            int c = str.charAt(i);
            switch (i) {
                case 18:
                    m = x;
                    x = 0;
                    // fall through
                case 8:
                case 13:
                case 23:
                    if (c != '-') {
                        throw new IllegalArgumentException(str);
                    }
                    break;
                default:
                    if (c >= '0' && c <= '9') {
                        x = (x << 4) | (c - '0');
                    } else if (c >= 'a' && c <= 'f') {
                        x = (x << 4) | (c - 'a' + 0xa);
                    } else if (c >= 'A' && c <= 'F') {
                        x = (x << 4) | (c - 'A' + 0xa);
                    } else {
                        throw new IllegalArgumentException(str);
                    }
            }
        }
        return new UUID(m, x);
    }
}
