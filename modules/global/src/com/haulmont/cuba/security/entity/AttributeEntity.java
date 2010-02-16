/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 15.02.2010 11:53:35
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

public interface AttributeEntity {

    public AttributeType getAttributeType();
    public void setAttributeType(AttributeType attrType);

    public String getMetaClassName();
    public void setMetaClassName(String name);
    
}
