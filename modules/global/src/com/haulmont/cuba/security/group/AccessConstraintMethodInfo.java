package com.haulmont.cuba.security.group;

import java.io.Serializable;

public class AccessConstraintMethodInfo implements Serializable {
    private static final long serialVersionUID = -4645590132206253462L;

    private final String className;
    private final String methodName;
    private final String argClassName;

    public AccessConstraintMethodInfo(String className, String methodName, String argClassName) {
        this.className = className;
        this.methodName = methodName;
        this.argClassName = argClassName;
    }

    protected Object readResolve() {
        return new AccessConstraintMethodPredicate(className, methodName, argClassName);
    }
}
