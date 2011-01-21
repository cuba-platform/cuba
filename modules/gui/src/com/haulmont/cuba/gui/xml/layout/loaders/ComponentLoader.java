/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 10:16:44
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
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
            if (component instanceof DatasourceComponent
                    && ((DatasourceComponent) component).getDatasource() != null)
            {
                MetaClass metaClass = ((DatasourceComponent) component).getDatasource().getMetaClass();
                MetaProperty metaProperty = ((DatasourceComponent) component).getMetaProperty();

                UserSession userSession = UserSessionClient.getUserSession();
                boolean editable = (userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                                        || userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                        && userSession.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);

                if (!editable) {
                    ((Component.Editable) component).setEditable(false);
                    return;
                }
            }

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

    protected void loadDescription(Component.HasCaption component, Element element) {
        String description = element.attributeValue("description");

        if (!StringUtils.isEmpty(description)) {
            description = loadResourceString(description);
            component.setDescription(description);
        }
    }

    protected void loadVisible(Component component, Element element) {
        if (component instanceof DatasourceComponent 
                && ((DatasourceComponent) component).getDatasource() != null)
        {
            MetaClass metaClass = ((DatasourceComponent) component).getDatasource().getMetaClass();
            MetaProperty metaProperty = ((DatasourceComponent) component).getMetaProperty();

            UserSession userSession = UserSessionClient.getUserSession();
            if (!userSession.isEntityOpPermitted(metaClass, EntityOp.READ)
                    || !userSession.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.VIEW)) {
                component.setVisible(false);
                return;
            }
        }

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

        if (!StringUtils.isEmpty(enable) && isBoolean(enable)) {
            component.setEnabled(evaluateBoolean(enable));
        }
    }

    protected String loadResourceString(String caption) {
        if (!StringUtils.isEmpty(caption)) {
            if (caption.startsWith("icon://")) {
                caption = caption.substring("icon://".length());
            } else {
                caption = MessageUtils.loadString(messagesPack, caption);
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

    protected void assignFrame(final Component.BelongToFrame component) {
        if (context.getFrame() != null) {
            component.setFrame(context.getFrame());
        } else
            throw new IllegalStateException("ComponentLoaderContext.frame is null");
    }

    protected Boolean evaluateBoolean(String expression) {
        Boolean value;
        if (isBoolean(expression)) {
            value = Boolean.valueOf(expression);
        } else {
            value = ScriptingProvider.evaluateGroovy(
                    ScriptingProvider.Layer.GUI, expression, context.getBinding());
        }
        return value;
    }

    protected static boolean isBoolean(String s) {
        return "true".equals(s) || "false".equals(s);
    }

    protected void loadAction(Component.ActionOwner component, Element element) {
        final String actionName = element.attributeValue("action");
        if (!StringUtils.isEmpty(actionName)) {
            context.addLazyTask(new AssignActionLazyTask(component, actionName, context.getFrame()));
        }
    }

    protected void loadPresentations(Component.HasPresentations component, Element element) {
        String presentations = element.attributeValue("presentations");
        if (!StringUtils.isEmpty(presentations) && isBoolean(presentations)) {
            component.usePresentations(evaluateBoolean(presentations));
            context.addLazyTask(new LoadPresentationsLazyTask(component));
        }
    }

    protected void loadIcon(Component.HasIcon component, Element element) {
        final String icon = element.attributeValue("icon");
        if (!StringUtils.isEmpty(icon)) {
            component.setIcon(loadResourceString(icon));
        }
    }
}
