/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.model.Attribute;

/**
 * Author: Alexander Chevelev
 * Date: 05.04.2011
 * Time: 3:09:53
 */
public enum InferredType {
    Collection {
        public boolean matches(Attribute attribute) {
            return attribute.isCollection() && attribute.isEntityReferenceAttribute();
        }
    },
    Entity {
        public boolean matches(Attribute attribute) {
            return !attribute.isCollection() && attribute.isEntityReferenceAttribute();
        }
    },
    Date {
        public boolean matches(Attribute attribute) {
            return java.util.Date.class.isAssignableFrom(attribute.getSimpleType());
        }
    },
    Any {
        public boolean matches(Attribute attribute) {
            return true;
        }
    };

    public abstract boolean matches(Attribute attribute);
}
