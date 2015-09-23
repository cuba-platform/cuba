/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.model;

/**
 * @author chevelev
 * @version $Id$
 */
public class VirtualEntity extends EntityImpl {
    public static int idx = 0;

    public VirtualEntity() {
        super(VirtualEntity.generateName());
    }

    static synchronized String generateName() {
        return "Virtual#" + idx++;
    }
}