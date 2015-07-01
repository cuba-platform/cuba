/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.dummy;

import com.haulmont.cuba.core.entity.BaseStringIdEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sys$DummyStringIdEntity")
@SystemLevel
public class DummyStringIdEntity extends BaseStringIdEntity {

    @Id
    @Column
    protected String code;

    @Override
    public String getId() {
        return code;
    }

    @Override
    public void setId(String id) {
        code = id;
    }
}
