/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.pointer;

import com.haulmont.cuba.core.sys.jpql.DomainModel;

/**
 * @author chevelev
 * @version $Id$
 */
public class NoPointer implements Pointer {
    private static final NoPointer instance = new NoPointer();

    private NoPointer() {
    }

    public static Pointer instance() {
        return instance;
    }

    @Override
    public Pointer next(DomainModel model, String field) {
        return this;
    }
}