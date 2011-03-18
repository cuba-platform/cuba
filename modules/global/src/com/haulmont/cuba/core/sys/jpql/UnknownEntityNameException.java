package com.haulmont.cuba.core.sys.jpql;

/**
 * User: Alex Chevelev
 * Date: 14.10.2010
 * Time: 0:06:42
 */
public class UnknownEntityNameException extends Throwable {
    private String entityName;

    public UnknownEntityNameException(String entityName) {
        super("Entity with name [" + entityName + "] is unknown");
        this.entityName = entityName;
    }
}
