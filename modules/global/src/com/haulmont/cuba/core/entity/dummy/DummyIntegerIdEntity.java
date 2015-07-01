/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.dummy;

import com.haulmont.cuba.core.entity.BaseIntegerIdEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Entity;

/**
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sys$DummyIntegerIdEntity")
@SystemLevel
public class DummyIntegerIdEntity extends BaseIntegerIdEntity {
}
