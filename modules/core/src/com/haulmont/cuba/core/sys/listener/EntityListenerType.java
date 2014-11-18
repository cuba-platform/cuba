/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.listener;

import com.haulmont.cuba.core.listener.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public enum EntityListenerType {

    BEFORE_DETACH(BeforeDetachEntityListener.class),
    BEFORE_ATTACH(BeforeAttachEntityListener.class),
    BEFORE_INSERT(BeforeInsertEntityListener.class),
    AFTER_INSERT(AfterInsertEntityListener.class),
    BEFORE_UPDATE(BeforeUpdateEntityListener.class),
    AFTER_UPDATE(AfterUpdateEntityListener.class),
    AFTER_DELETE(AfterDeleteEntityListener.class),
    BEFORE_DELETE(BeforeDeleteEntityListener.class);

    private final Class listenerInterface;

    private EntityListenerType(Class listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    public Class getListenerInterface() {
        return listenerInterface;
    }
}