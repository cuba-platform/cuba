/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 10:16:44
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.GroovyHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Locale;

public abstract class ComponentLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    protected Locale locale;
    protected String messagesPack;
    protected Context context;

    protected ComponentLoader(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public void setMessagesPack(String name) {
        this.messagesPack = name;
    }

    protected void loadId(Component component, Element element) {
        final String id = element.attributeValue("id");
        component.setId(id);
    }

    protected void loadStyleName(Component component, Element element)
    {
        final String styleName = element.attributeValue("stylename");
        if (!StringUtils.isEmpty(styleName)) {
            component.setStyleName(styleName);
        }
    }

    protected void assignXmlDescriptor(Component component, Element element) {
        if (component instanceof Component.HasXmlDescriptor) {
            ((Component.HasXmlDescriptor) component).setXmlDescriptor(element);
        }
    }

    protected void loadEditable(Component component, Element element) {
        if (component instanceof Component.Editable) {
            final String editable = element.attributeValue("editable");
            if (!StringUtils.isEmpty(editable)) {
                ((Component.Editable) component).setEditable(BooleanUtils.toBoolean(editable));
            }
        }
    }

    protected void loadCaption(Component.HasCaption component, Element element) {
        String caption = element.attributeValue("caption");

        if (!StringUtils.isEmpty(caption)) {
            caption = loadResourceString(caption);
            component.setCaption(caption);
        }
    }

    protected void loadVisible(Component component, Element element) {
        String visible = element.attributeValue("visible");
        if (visible == null) {
            final Element e = element.element("visible");
            if (e != null) {
                visible = e.getText();
            }
        }

        if (!StringUtils.isEmpty(visible)) {
            component.setVisible(evaluateBoolean(visible));
        }
    }

    protected void loadEnable(Component component, Element element) {
        String enable = element.attributeValue("enable");
        if (enable == null) {
            final Element e = element.element("enable");
            if (e != null) {
                enable = e.getText();
            }
        }

        if (!StringUtils.isEmpty(enable)) {
            if (isBoolean(enable)) {
                component.setEnabled(Boolean.valueOf(enable));
            }
        }
    }

    protected String loadResourceString(String caption) {
        if (caption != null && caption.startsWith("msg://")) {
            String path = caption.substring(6);
            final String[] strings = path.split("/");
            if (strings.length == 1) {
                if (messagesPack != null) {
                    caption = MessageProvider.getMessage(messagesPack, strings[0]);
                }
            } else if (strings.length == 2) {
                caption = MessageProvider.getMessage(strings[0], strings[1]);
            } else {
                throw new UnsupportedOperationException("Unsupported resource string format: " + caption);
            }
        }
        return caption;
    }

    protected void loadAlign(final Component component, Element element) {
        final String align = element.attributeValue("align");
        if (!StringUtils.isBlank(align)) {
            context.addLazyTask(new LazyTask() {
                public void execute(Context context, IFrame frame) {
                    component.setAlignment(Component.Alignment.valueOf(align));
                }
            });
        }
    }

    protected void loadHeight(Component component, Element element) {
        loadHeight(component, element, null);
    }

    protected void loadHeight(Component component, Element element, String defaultValue) {
        final String height = element.attributeValue("height");
        if (!StringUtils.isBlank(height)) {
            component.setHeight(height);
        } else if (!StringUtils.isBlank(defaultValue)) {
            component.setHeight(defaultValue);
        }
    }

    protected void loadWidth(Component component, Element element) {
        loadWidth(component, element, null);
    }

    protected void loadWidth(Component component, Element element, String defaultValue) {
        final String width = element.attributeValue("width");
        if (!StringUtils.isBlank(width)) {
            component.setWidth(width);
        } else if (!StringUtils.isBlank(defaultValue)) {
            component.setWidth(defaultValue);
        }
    }

    protected void loadExpandable(Component.Expandable component, Element element) {
        final String expandable = element.attributeValue("expandable");
        if (!StringUtils.isEmpty(expandable) && isBoolean(expandable)) {
            component.setExpandable(Boolean.valueOf(expandable));
        }
    }

    protected void addAssignWindowTask(final Component.BelongToFrame component) {
        context.addLazyTask(new LazyTask() {
            public void execute(Context context, IFrame frame) {
                component.setFrame(frame);
            }
        });
    }

    protected Boolean evaluateBoolean(String expression) {
        Boolean value;
        if (isBoolean(expression)) {
            value = Boolean.valueOf(expression);
        } else {
            @SuppressWarnings({"unchecked"})
            Boolean res = GroovyHelper.evaluate(expression, context.getBinding());
            value = res;
        }
        return value;
    }

    protected static boolean isBoolean(String s) {
        return "true".equals(s) || "false".equals(s);
    }
}
