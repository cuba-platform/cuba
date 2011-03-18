package com.haulmont.cuba.core.sys.jpql.model;

/**
 * Author: Alexander Chevelev
 * Date: 08.11.2010
 * Time: 20:28:04
 */
public interface Attribute extends Cloneable {
    Class getSimpleType();

    String getName();

    boolean isEntityReferenceAttribute();

    boolean isCollection();

    String getReferencedEntityName();

    String getUserFriendlyName();

    Object clone() throws CloneNotSupportedException;
}
