/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.global.UuidSource;

import javax.annotation.ManagedBean;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(UuidSource.NAME)
public class UuidSourceClientImpl implements UuidSource {

    @Override
    public UUID createUuid() {
        // TODO KK: implement UUID version 1
        return UUID.randomUUID();
    }
}
