/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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

    T createParam(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource, boolean inExpr);

    T createParam(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                  boolean inExpr, UUID categoryAttrId);

    T createParam(String name, Class javaClass, String entityWhere, String entityView, Datasource datasource,
                  MetaProperty property, boolean inExpr);
}
