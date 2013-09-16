/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;

/**
 * Security constraint definition entity
 */
@Entity(name = "sec$Constraint")
@Table(name = "SEC_CONSTRAINT")
@SystemLevel
public class Constraint extends StandardEntity
{
    private static final long serialVersionUID = -8598548105315052474L;

    @Column(name = "ENTITY_NAME", length = 50)
    private String entityName;

    @Column(name = "JOIN_CLAUSE", length = 500)
    private String joinClause;

    @Column(name = "WHERE_CLAUSE", length = 500)
    private String whereClause;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getJoinClause() {
        return joinClause;
    }

    public void setJoinClause(String joinClause) {
        this.joinClause = joinClause;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }
}
