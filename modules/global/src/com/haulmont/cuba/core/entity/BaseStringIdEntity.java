/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for persistent entities with String identifier.
 * <p>
 * Does not define an identifier field. Inheritors must define a field of type String and add
 * {@link javax.persistence.Id} annotation to it, e.g.
 * <pre>
 *  &#64;Id
 *  &#64;Column(name = "CODE")
 *  protected String code;
 * </pre>
 *
 * @author krivopustov
 * @version $Id$
 */
@MappedSuperclass
public abstract class BaseStringIdEntity extends BaseGenericIdEntity<String> {

    private static final long serialVersionUID = -1887225952123433245L;

    @Column(name = "UUID", nullable = false)
    @Persistent
    protected UUID uuid;

    protected BaseStringIdEntity() {
        uuid = UuidProvider.createUuid();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public abstract String getId();
}