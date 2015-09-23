/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.model.Attribute;

/**
 * @author chevelev
 * @version $Id$
 */
public enum InferredType {
    Collection {
        @Override
        public boolean matches(Attribute attribute) {
            return attribute.isCollection() && attribute.isEntityReferenceAttribute();
        }
    },
    Entity {
        @Override
        public boolean matches(Attribute attribute) {
            return !attribute.isCollection() && attribute.isEntityReferenceAttribute();
        }
    },
    Date {
        @Override
        public boolean matches(Attribute attribute) {
            return java.util.Date.class.isAssignableFrom(attribute.getSimpleType());
        }
    },
    Any {
        @Override
        public boolean matches(Attribute attribute) {
            return true;
        }
    };

    public abstract boolean matches(Attribute attribute);
}