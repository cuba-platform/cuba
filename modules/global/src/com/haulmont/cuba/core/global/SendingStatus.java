/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * @author ovchinnikov
 * @version $Id$
 */
public enum SendingStatus implements EnumClass<Integer> {
    QUEUE(0),
    SENDING(100),
    SENT(200),
    NOTSENT(300);

    private Integer id;

    SendingStatus(Integer id) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static SendingStatus fromId(Integer id) {
        for (SendingStatus ss : SendingStatus.values()) {
            if (id.equals(ss.getId())) {
                return ss;
            }
        }
        return null;
    }
}