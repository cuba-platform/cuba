/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.11.2008 16:38:30
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import java.util.UUID;

public class BaseLongIdEntity implements BaseEntity<Long>
{
    private Long id;

    private UUID uuid;

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }
}
