/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UuidSource;

import javax.annotation.ManagedBean;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(UuidSource.NAME)
public class UuidSourceImpl implements UuidSource {

    @Override
    public UUID createUuid() {
        // TODO KK: implement UUID version 1
        return UUID.randomUUID();
    }
}
