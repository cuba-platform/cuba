/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.QueryParser;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

public abstract class AbstractConditionDescriptor<T extends AbstractParam> {

    protected static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    protected Element element;
    protected String name;
    protected String caption;
    protected String locCaption;
    protected String filterComponentName;
    protected MetaClass metaClass;
    protected CollectionDatasource datasource;
    protected String entityAlias;
    protected boolean inExpr;
    protected ParamFactory<T> paramFactory = getParamFactory();
    protected boolean showImmediately;

    public AbstractConditionDescriptor(String name, String filterComponentName, CollectionDatasource datasource) {
        this.name = name;
        this.filterComponentName = filterComponentName;
        this.datasource = datasource;
        this.metaClass = datasource.getMetaClass();

        String query = datasource.getQuery();
        QueryParser parser = QueryTransformerFactory.createParser(query);
        entityAlias = parser.getEntityAlias(metaClass.getName());
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public String getLocCaption() {
        return locCaption;
    }

    public String getFilterComponentName() {
        return filterComponentName;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public boolean isInExpr() {
        return inExpr;
    }

    public void setInExpr(boolean inExpr) {
        this.inExpr = inExpr;
    }

    public boolean isShowImmediately() {
        return showImmediately;
    }

    public T createParam(AbstractCondition condition) {
        return paramFactory.createParam(condition.createParamName(), getJavaClass(),
                getEntityParamWhere(), getEntityParamView(), datasource, inExpr, condition.isRequired());
    }

    protected abstract ParamFactory<T> getParamFactory();

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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                '}';
    }
}
