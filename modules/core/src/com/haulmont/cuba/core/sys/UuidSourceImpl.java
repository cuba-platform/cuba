/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UuidSource;

import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(UuidSource.NAME)
public class UuidSourceImpl implements UuidSource {

    @Override
    public UUID createUuid() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong());
    }
}
