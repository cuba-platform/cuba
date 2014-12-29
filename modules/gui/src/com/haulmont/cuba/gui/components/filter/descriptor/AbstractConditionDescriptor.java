/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.QueryParser;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

/**
 * Class that encapsulates common filter condition descriptor behaviour. Condition descriptors are used for
 * generating condition objects.
 * @author devyatkin
 * @version $Id$
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$AbstractConditionDescriptor")
@SystemLevel
public abstract class AbstractConditionDescriptor extends AbstractNotPersistentEntity{

    protected Element element;
    protected String name;
    protected String caption;
    protected String locCaption;
    protected String filterComponentName;
    protected MetaClass datasourceMetaClass;
    protected CollectionDatasource datasource;
    protected String entityAlias;
    protected Boolean inExpr = false;
    protected Boolean showImmediately = false;

    public AbstractConditionDescriptor(String name, String filterComponentName, CollectionDatasource datasource) {
        this.name = name;
        this.filterComponentName = filterComponentName;
        this.datasource = datasource;
        this.datasourceMetaClass = datasource.getMetaClass();

        String query = datasource.getQuery();
        QueryParser parser = QueryTransformerFactory.createParser(query);
        entityAlias = parser.getEntityAlias(datasourceMetaClass.getName());
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    @MetaProperty
    public String getLocCaption() {
        return locCaption;
    }

    public String getFilterComponentName() {
        return filterComponentName;
    }

    public MetaClass getDatasourceMetaClass() {
        return datasourceMetaClass;
    }

    public Boolean isInExpr() {
        return inExpr;
    }

    public void setInExpr(Boolean inExpr) {
        this.inExpr = inExpr;
    }

    public Boolean isShowImmediately() {
        return showImmediately;
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public String getOperatorType() {
        if (element != null)
            return element.attributeValue("operatorType", null);
        else return null;
    }

    public abstract AbstractCondition createCondition();

    public abstract Class getJavaClass();

    public abstract String getEntityParamWhere();

    public abstract String getEntityParamView();

    @MetaProperty
    public String getTreeCaption() {
        return getLocCaption();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                '}';
    }
}