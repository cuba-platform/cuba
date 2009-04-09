/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 20:10:53
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

// test change

@Entity(name = "core$Server")
@Table(name = "SYS_SERVER")
public class Server extends StandardEntity
{
    private static final long serialVersionUID = 1892335683693067357L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "IS_RUNNING")
    private Boolean running;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }
}
