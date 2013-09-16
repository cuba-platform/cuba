/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public interface ParamFactory<T extends AbstractParam> {

    T createParam(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                  boolean inExpr, boolean required);

    T createParam(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                  boolean inExpr, UUID categoryAttrId, boolean required);

    T createParam(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                  MetaProperty property, boolean inExpr, boolean required);
}
