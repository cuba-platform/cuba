/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;

/**
 * Security constraint definition entity.
 *
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sec$Constraint")
@Table(name = "SEC_CONSTRAINT")
@SystemLevel
public class Constraint extends StandardEntity {

    private static final long serialVersionUID = -8598548105315052474L;

    @Column(name = "CHECK_TYPE", length = 50, nullable = false)
    protected String checkType = ConstraintCheckType.DATABASE.getId();

    @Column(name = "OPERATION_TYPE", length = 50, nullable = false)
    protected String operationType = ConstraintOperationType.READ.getId();

    @Column(name = "CODE", length = 255)
    protected String code;

    @Column(name = "ENTITY_NAME", length = 255, nullable = false)
    protected String entityName;

    @Column(name = "JOIN_CLAUSE", length = 500)
    protected String joinClause;

    @Column(name = "WHERE_CLAUSE", length = 1000)
    protected String whereClause;

    @Column(name = "GROOVY_SCRIPT", length = 1000)
    protected String groovyScript;

    @Column(name = "FILTER_XML", length = 1000)
    protected String filterXml;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    protected Group group;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getGroovyScript() {
        return groovyScript;
    }

    public void setGroovyScript(String groovyScript) {
        this.groovyScript = groovyScript;
    }

    public String getFilterXml() {
        return filterXml;
    }

    public void setFilterXml(String filterXml) {
        this.filterXml = filterXml;
    }

    public ConstraintCheckType getCheckType() {
        return ConstraintCheckType.fromId(checkType);
    }

    public void setCheckType(ConstraintCheckType checkType) {
        this.checkType = checkType != null ? checkType.getId() : null;
    }

    public ConstraintOperationType getOperationType() {
        return ConstraintOperationType.fromId(operationType);
    }

    public void setOperationType(ConstraintOperationType operationType) {
        this.operationType = operationType != null ? operationType.getId() : null;
    }
}
