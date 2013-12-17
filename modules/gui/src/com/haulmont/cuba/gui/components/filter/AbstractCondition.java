/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.*;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Class that encapsulates common filter condition behaviour.
 *
 * @author devyatkin
 * @version $Id$
 */
public abstract class AbstractCondition<T extends AbstractParam> {

    public static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    public interface Listener {
        void captionChanged();

        void paramChanged();
    }

    protected String name;
    protected String caption;
    protected String locCaption;
    protected String filterComponentName;
    protected String text;
    protected boolean group;
    protected boolean unary;
    protected boolean inExpr;
    protected Class javaClass;
    protected Class paramClass;
    protected T param;
    protected String entityAlias;
    protected boolean hidden;
    protected boolean required;
    protected String entityParamWhere;
    protected String entityParamView;
    protected Datasource datasource;
    protected UUID categoryAttrId;
    protected ParamFactory<T> paramFactory = getParamFactory();

    protected List<Listener> listeners = new ArrayList<>();

    protected AbstractCondition() {
        throw new UnsupportedOperationException();
    }

    protected AbstractCondition(Element element, String filterComponentName, Datasource datasource) {
        this.filterComponentName = filterComponentName;
        name = element.attributeValue("name");
        text = StringEscapeUtils.unescapeXml(element.getText());
        if (text == null)
            text = "";

        caption = element.attributeValue("caption");
        unary = Boolean.valueOf(element.attributeValue("unary"));
        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
        hidden = Boolean.valueOf(element.attributeValue("hidden"));
        required = Boolean.valueOf(element.attributeValue("required"));
        entityParamWhere = element.attributeValue("paramWhere");
        entityParamView = element.attributeValue("paramView");
        this.datasource = datasource;

        Scripting scripting = AppBeans.get(Scripting.NAME);

        String aclass = element.attributeValue("class");
        if (!isBlank(aclass))
            javaClass = scripting.loadClass(aclass);

        List<Element> paramElements = Dom4j.elements(element, "param");
        if (!paramElements.isEmpty()) {
            Element paramElem = paramElements.iterator().next();

            if (BooleanUtils.toBoolean(paramElem.attributeValue("hidden", "false"), "true", "false")) {
                paramElem = paramElements.iterator().next();
            }
            String paramName = paramElem.attributeValue("name");

            if (!isBlank(paramElem.attributeValue("javaClass"))) {
                paramClass = scripting.loadClass(paramElem.attributeValue("javaClass"));
                if (SetValueEntity.class.isAssignableFrom(paramClass)) {
                    categoryAttrId = UUID.fromString(paramElem.attributeValue("categoryAttrId"));
                }
            }

            if (unary) {
                param = paramFactory.createParam(paramName, null, null, null, null, false, required);
            } else {
                param = createParam(paramName);
            }

            param.parseValue(paramElem.getText());
        }
    }

    protected AbstractCondition(AbstractConditionDescriptor<T> descriptor) {
        name = descriptor.getName();
        caption = descriptor.getCaption();
        locCaption = descriptor.getLocCaption();
        filterComponentName = descriptor.getFilterComponentName();
        javaClass = descriptor.getJavaClass();
        unary = javaClass == null;
        param = descriptor.createParam(this);
        entityParamWhere = descriptor.getEntityParamWhere();
        entityParamView = descriptor.getEntityParamView();
        datasource = descriptor.getDatasource();
    }

    protected T createParam(String paramName) {
        if (categoryAttrId != null) {
            return paramFactory.createParam(paramName, paramClass, entityParamWhere,
                    entityParamView, datasource, inExpr, categoryAttrId, required);
        } else
            return paramFactory.createParam(paramName, paramClass == null ? javaClass : paramClass,
                    entityParamWhere, entityParamView, datasource, inExpr, required);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
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

    public void setLocCaption(String locCaption) {
        if (ObjectUtils.equals(this.locCaption, locCaption))
            return;

        this.locCaption = locCaption;
        for (Listener listener : listeners) {
            listener.captionChanged();
        }
    }

    public String getText() {
        updateText();
        return text;
    }

    protected void updateText() {
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;

        for (AbstractCondition.Listener listener : listeners) {
            listener.paramChanged();
        }
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    public String getFilterComponentName() {
        return filterComponentName;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void toXml(Element element) {
        String text = getText();
        if (StringUtils.isNotBlank(text))
            element.setText(text);

        element.addAttribute("name", name);

        if (javaClass != null)
            element.addAttribute("class", javaClass.getName());

        if (caption != null)
            element.addAttribute("caption", caption);

        if (unary)
            element.addAttribute("unary", "true");

        if (inExpr)
            element.addAttribute("inExpr", "true");

        if (hidden)
            element.addAttribute("hidden", "true");

        if (required)
            element.addAttribute("required", "true");

        if (param != null) {
            param.toXml(element);
            if (entityParamWhere != null)
                element.addAttribute("paramWhere", entityParamWhere);
            if (entityParamView != null)
                element.addAttribute("paramView", entityParamView);
        }
    }

    public String getError() {
        return null;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
    }

    public boolean isGroup() {
        return group;
    }

    public boolean isUnary() {
        return unary;
    }

    public void setUnary(boolean unary) {
        this.unary = unary;
    }

    public boolean isInExpr() {
        return inExpr;
    }

    public void setInExpr(boolean inExpr) {
        this.inExpr = inExpr;
    }

    public String getOperationCaption() {
        return "";
    }

    public String getEntityParamView() {
        return entityParamView;
    }

    public void setEntityParamView(String entityParamView) {
        this.entityParamView = entityParamView;
    }

    public String getEntityParamWhere() {
        return entityParamWhere;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setEntityParamWhere(String entityParamWhere) {
        this.entityParamWhere = entityParamWhere;
    }

    public String createParamName() {
        return "component$" + getFilterComponentName() + "." +
                getName().replace('.', '_') + RandomStringUtils.randomNumeric(5);
    }

    public abstract AbstractOperationEditor createOperationEditor();

    public abstract AbstractOperationEditor getOperationEditor();

    protected abstract ParamFactory<T> getParamFactory();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                '}';
    }
}