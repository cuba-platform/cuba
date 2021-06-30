package com.haulmont.cuba.security.group;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;

public class GroovyConstraintPredicate<T extends Entity> implements ConstraintPredicate<T> {
    private static final long serialVersionUID = -4294634026671321267L;

    private final String groovyScript;

    public GroovyConstraintPredicate(String groovyScript) {
        this.groovyScript = groovyScript;
    }

    @Override
    public boolean test(T o) {
        Security security = AppBeans.get(Security.class);
        return (boolean) security.evaluateConstraintScript(o, groovyScript);
    }
}