/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
