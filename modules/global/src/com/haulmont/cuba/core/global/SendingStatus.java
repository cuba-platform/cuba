/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Sergey Ovchinnikov
 * Created: 27.01.11 15:42
 *
 * $Id$
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum SendingStatus implements EnumClass<Integer> {
    QUEUE(0),
    SENDING(100),
    SENT(200),
    NOTSENT(300);

    private Integer id;

    SendingStatus(Integer id) {
        this.id=id;
    }

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
