/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.10.2009 14:48:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;
import java.util.ArrayList;

public abstract class Condition {

    public interface Listener {
        void captionChanged();
        void paramChanged();
    }

    protected String name;
    protected String caption;
    protected String locCaption;
    protected String filterComponentName;
    protected String text;
    protected boolean unary;
    protected boolean inExpr;
    protected Class javaClass;
    protected Param param;
    protected String entityAlias;
    protected boolean hidden;
    protected String entityParamWhere;
    protected String entityParamView;
    protected Datasource datasource;

    protected List<Listener> listeners = new ArrayList<Listener>();

    protected Condition() {
        throw new UnsupportedOperationException();
    }

    protected Condition(Element element, String filterComponentName, Datasource datasource) {
        this.filterComponentName = filterComponentName;
        name = element.attributeValue("name");
        text = StringEscapeUtils.unescapeXml(element.getText());
        caption = element.attributeValue("caption");
        unary = Boolean.valueOf(element.attributeValue("unary"));
        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
        hidden = Boolean.valueOf(element.attributeValue("hidden"));
        entityParamWhere = element.attributeValue("paramWhere");
        entityParamView = element.attributeValue("paramView");
        this.datasource = datasource;

        String aclass = element.attributeValue("class");
        if (!isBlank(aclass))
            javaClass = ScriptingProvider.loadClass(aclass);

        List<Element> paramElements = Dom4j.elements(element, "param");
        if (!paramElements.isEmpty()) {
            Element paramElem = paramElements.iterator().next();
            String paramName = paramElem.attributeValue("name");

            if (unary) {
                param = new Param(paramName, null, null, null, null, false);
            } else {
                param = createParam(paramName);
            }

            param.parseValue(paramElem.getText());
        }
    }

    protected Condition(ConditionDescriptor descriptor) {
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

    protected Param createParam(String paramName) {
        return new Param(paramName, javaClass, entityParamWhere, entityParamView, datasource, inExpr);
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

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;

        for (Condition.Listener listener : listeners) {
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

    public void toXml(Element element) {
        element.setText(getText());
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

        if (param != null) {
            Element paramElem = element.addElement("param");
            paramElem.addAttribute("name", param.getName());

            paramElem.setText(param.formatValue());

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

    public abstract OperationEditor createOperationEditor();
}
