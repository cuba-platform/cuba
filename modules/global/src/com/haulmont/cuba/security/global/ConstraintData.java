/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.global;

import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.ConstraintCheckType;

import java.io.Serializable;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class ConstraintData implements Serializable {
    protected final String code;
    protected final ConstraintOperationType operationType;
    protected final ConstraintCheckType checkType;
    protected final String join;
    protected final String whereClause;
    protected final String groovyScript;

    public ConstraintData(Constraint constraint) {
        this.code = constraint.getCode();
        this.join = constraint.getJoinClause();
        this.whereClause = constraint.getWhereClause();
        this.groovyScript = constraint.getGroovyScript();
        this.operationType = constraint.getOperationType();
        this.checkType = constraint.getCheckType();
    }

    public String getCode() {
        return code;
    }

    public String getJoin() {
        return join;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public String getGroovyScript() {
        return groovyScript;
    }

    public ConstraintOperationType getOperationType() {
        return operationType;
    }

    public ConstraintCheckType getCheckType() {
        return checkType;
    }
}
