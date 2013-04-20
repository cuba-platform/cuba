/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.util

import javax.naming.OperationNotSupportedException

/**
 * @author artamonov
 * @version $Id$
 */
@PackageScope
class ViewNodeBuilder extends BuilderSupport {

    @Override
    protected void setParent(Object parent, Object child) {
        if (child != null)
            parent.viewProperties.add(child)
    }

    @Override
    protected Object createNode(Object name) {
        throw new OperationNotSupportedException()
    }

    @Override
    protected Object createNode(Object name, Object value) {
        throw new OperationNotSupportedException()
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        if ('view' == name) {
            return new ViewNode(
                    name: attributes['name'],
                    entity: attributes['entity'],
                    className: attributes['class'],
                    extendsView: attributes['extends'],
                    systemProperties: attributes['systemProperties']
            )
        }

        if ('property' == name) {
            return new ViewPropertyNode(
                    name: attributes['name'],
                    view: attributes['view'],
                    lazy: attributes['lazy']
            )
        }

        throw new OperationNotSupportedException()
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        throw new OperationNotSupportedException()
    }
}