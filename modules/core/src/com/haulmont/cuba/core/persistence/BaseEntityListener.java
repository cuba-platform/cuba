/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.11.2008 16:33:43
 *
 * $Id$
 */
package com.haulmont.cuba.core.persistence;

import com.haulmont.cuba.core.entity.BaseEntity;

import javax.persistence.PrePersist;
import java.util.Date;

public class BaseEntityListener
{
    @PrePersist
    void onCreate(Object obj) {
        if (obj instanceof BaseEntity) {
            ((BaseEntity) obj).setCreatedBy("admin");
            ((BaseEntity) obj).setCreateTs(new Date());
        }
    }

}
