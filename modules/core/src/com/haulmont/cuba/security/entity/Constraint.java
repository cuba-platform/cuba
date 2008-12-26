/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.12.2008 16:25:02
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "sec$Constraint")
@Table(name = "SEC_CONSTRAINT")
public class Constraint extends StandardEntity
{
    private static final long serialVersionUID = -8598548105315052474L;

    @Column(name = "ENTITY_NAME", length = 50)
    private String entityName;

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

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }
}
