/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.validators.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.DeclarativeAction;
import com.haulmont.cuba.gui.xml.DeclarativeTrackingAction;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractComponentLoader<T extends Component> implements ComponentLoader<T> {

    protected Locale locale;
    protected String messagesPack;
    protected Context context;

    protected Security security = AppBeans.get(Security.NAME);

    protected Messages messages = AppBeans.get(Messages.NAME);
    protected MessageTools messageTools = AppBeans.get(MessageTools.NAME);
    protected Scripting scripting = AppBeans.get(Scripting.NAME);
    protected Resources resources = AppBeans.get(Resources.NAME);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected ThemeConstants themeConstants;
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig layoutLoaderConfig;
    protected Element element;

    protected T resultComponent;

    protected AbstractComponentLoader() {
        ThemeConstantsManager themeConstantsManager = AppBeans.get(ThemeConstantsManager.NAME);
        this.themeConstants = themeConstantsManager.getConstants();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String getMessagesPack() {
        return messagesPack;
    }

    @Override
    public void setMessagesPack(String name) {
        this.messagesPack = name;
    }

    @Override
    public ComponentsFactory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(ComponentsFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setElement(Element element) {
        this.element = element;
    }

    @Override
    public Element getElement(Element element) {
        return element;
    }

    @Override
    public T getResultComponent() {
        return resultComponent;
    }

    @Override
    public LayoutLoaderConfig getLayoutLoaderConfig() {
        return layoutLoaderConfig;
    }

    @Override
    public void setLayoutLoaderConfig(LayoutLoaderConfig layoutLoaderConfig) {
        this.layoutLoaderConfig = layoutLoaderConfig;
    }

    protected void loadId(Component component, Element element) {
        String id = element.attributeValue("id");
        component.setId(id);
    }

    protected void loadStyleName(Component component, Element element) {
        if (element.attribute("stylename") != null) {
            component.setStyleName(element.attributeValue("stylename"));
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
                    && ((DatasourceComponent) component).getDatasource() != null) {

                DatasourceComponent wiredComponent = (DatasourceComponent) component;
                MetaClass metaClass = wiredComponent.getDatasource().getMetaClass();
                MetaPropertyPath propertyPath = wiredComponent.getMetaPropertyPath();

                if (propertyPath != null
                        && !security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString())) {
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
        if (element.attribute("caption") != null) {
            String caption = element.attributeValue("caption");

            caption = loadResourceString(caption);
            component.setCaption(caption);
        }
    }

    protected void loadDescription(Component.HasCaption component, Element element) {
        if (element.attribute("description") != null) {
            String description = element.attributeValue("description");

            description = loadResourceString(description);
            component.setDescription(description);
        }
    }

    protected boolean loadVisible(Component component, Element element) {
        if (component instanceof DatasourceComponent
                && ((DatasourceComponent) component).getDatasource() != null) {

            DatasourceComponent wiredComponent = (DatasourceComponent) component;
            MetaClass metaClass = wiredComponent.getDatasource().getMetaClass();
            MetaPropertyPath propertyPath = wiredComponent.getMetaPropertyPath();

            if (propertyPath != null
                    && !security.isEntityAttrReadPermitted(metaClass, propertyPath.toString())) {
                component.setVisible(false);
                return false;
            }
        }

        String visible = element.attributeValue("visible");
        if (!StringUtils.isEmpty(visible)) {
            Boolean visibleValue = Boolean.valueOf(visible);
            component.setVisible(visibleValue);

            return visibleValue;
        }

        return true;
    }

    protected boolean loadEnable(Component component, Element element) {
        String enable = element.attributeValue("enable");
        if (!StringUtils.isEmpty(enable)) {
            Boolean enabled = Boolean.valueOf(enable);
            component.setEnabled(enabled);

            return enabled;
        }

        return true;
    }

    protected String loadResourceString(String caption) {
        if (StringUtils.isEmpty(caption)) {
            return caption;
        }

        return messageTools.loadString(messagesPack, caption);
    }

    protected String loadThemeString(String value) {
        if (value != null && value.startsWith(ThemeConstants.PREFIX)) {
            value = themeConstants.get(value.substring(ThemeConstants.PREFIX.length()));
        }
        return value;
    }

    protected void loadAlign(Component component, Element element) {
        String align = element.attributeValue("align");
        if (!StringUtils.isBlank(align)) {
            component.setAlignment(Alignment.valueOf(align));
        }
    }

    protected void loadHeight(Component component, Element element) {
        loadHeight(component, element, null);
    }

    protected void loadHeight(Component component, Element element, @Nullable String defaultValue) {
        final String height = element.attributeValue("height");
        if ("auto".equalsIgnoreCase(height)) {
            component.setHeight(Component.AUTO_SIZE);
        } else if (!StringUtils.isBlank(height)) {
            component.setHeight(loadThemeString(height));
        } else if (!StringUtils.isBlank(defaultValue)) {
            component.setHeight(defaultValue);
        }
    }

    protected void loadWidth(Component component, Element element) {
        loadWidth(component, element, null);
    }

    protected void loadWidth(Component component, Element element, @Nullable String defaultValue) {
        final String width = element.attributeValue("width");
        if ("auto".equalsIgnoreCase(width)) {
            component.setWidth(Component.AUTO_SIZE);
        } else if (!StringUtils.isBlank(width)) {
            component.setWidth(loadThemeString(width));
        } else if (!StringUtils.isBlank(defaultValue)) {
            component.setWidth(defaultValue);
        }
    }

    protected void loadCollapsible(Component.Collapsable component, Element element, boolean defaultCollapsable) {
        String collapsable = element.attributeValue("collapsable");
        boolean b = Strings.isNullOrEmpty(collapsable) ? defaultCollapsable : BooleanUtils.toBoolean(collapsable);
        component.setCollapsable(b);
        if (b) {
            String collapsed = element.attributeValue("collapsed");
            if (!StringUtils.isBlank(collapsed)) {
                component.setExpanded(!BooleanUtils.toBoolean(collapsed));
            }
        }
    }

    protected void loadBorder(Component.HasBorder component, Element element) {
        String border = element.attributeValue("border");
        if (!StringUtils.isEmpty(border)) {
            if ("visible".equalsIgnoreCase(border)) {
                component.setBorderVisible(true);
            } else if ("hidden".equalsIgnoreCase(border)) {
                component.setBorderVisible(false);
            }
        }
    }

    protected void loadMargin(Component.Margin layout, Element element) {
        final String margin = element.attributeValue("margin");
        if (!StringUtils.isEmpty(margin)) {
            if (margin.contains(";") || margin.contains(",")) {
                final String[] margins = margin.split("[;,]");
                if (margins.length != 4) {
                    throw new GuiDevelopmentException(
                        "Margin attribute must contain 1 or 4 boolean values separated by ',' or ';", context.getFullFrameId());
                }

                layout.setMargin(
                        Boolean.valueOf(StringUtils.trimToEmpty(margins[0])),
                        Boolean.valueOf(StringUtils.trimToEmpty(margins[1])),
                        Boolean.valueOf(StringUtils.trimToEmpty(margins[2])),
                        Boolean.valueOf(StringUtils.trimToEmpty(margins[3]))
                );
            } else {
                layout.setMargin(Boolean.valueOf(margin));
            }
        }
    }

    protected void assignFrame(final Component.BelongToFrame component) {
        if (context.getFrame() != null) {
            component.setFrame(context.getFrame());
        } else
            throw new GuiDevelopmentException("ComponentLoaderContext.frame is null", context.getFullFrameId());
    }

    protected void loadAction(Component.ActionOwner component, Element element) {
        final String actionName = element.attributeValue("action");
        if (!StringUtils.isEmpty(actionName)) {
            context.addPostInitTask(new AssignActionPostInitTask(component, actionName, context.getFrame()));
        }
    }

    protected void loadPresentations(Component.HasPresentations component, Element element) {
        String presentations = element.attributeValue("presentations");
        if (!StringUtils.isEmpty(presentations)) {
            component.usePresentations(Boolean.valueOf(presentations));
            context.addPostInitTask(new LoadPresentationsPostInitTask(component));
        }
    }

    protected void loadIcon(Component.HasIcon component, Element element) {
        if (element.attribute("icon") != null) {
            String icon = element.attributeValue("icon");

            String themeValue = loadThemeString(icon);
            icon = loadResourceString(themeValue);
            component.setIcon(icon);
        }
    }

    protected Field.Validator loadValidator(Element validatorElement) {
        final String className = validatorElement.attributeValue("class");
        final String scriptPath = validatorElement.attributeValue("script");
        final String script = validatorElement.getText();

        Field.Validator validator = null;

        if (StringUtils.isNotBlank(scriptPath) || StringUtils.isNotBlank(script)) {
            validator = new ScriptValidator(validatorElement, getMessagesPack());
        } else {
            Class aClass = scripting.loadClass(className);
            if (aClass == null)
                throw new GuiDevelopmentException("Class " + className + " is not found", context.getFullFrameId());
            if (!StringUtils.isBlank(getMessagesPack()))
                try {
                    validator = (Field.Validator) ReflectionHelper.newInstance(aClass, validatorElement, getMessagesPack());
                } catch (NoSuchMethodException e) {
                    //
                }
            if (validator == null) {
                try {
                    validator = (Field.Validator) ReflectionHelper.newInstance(aClass, validatorElement);
                } catch (NoSuchMethodException e) {
                    try {
                        validator = (Field.Validator) ReflectionHelper.newInstance(aClass);
                    } catch (NoSuchMethodException e1) {
                        //
                    }
                }
            }
            if (validator == null) {
                throw new GuiDevelopmentException("Validator class " + aClass + " has no supported constructors",
                        context.getFullFrameId());
            }
        }
        return validator;
    }

    protected Field.Validator getDefaultValidator(MetaProperty property) {
        Field.Validator validator = null;
        if (property.getRange().isDatatype()) {
            Datatype<Object> dt = property.getRange().asDatatype();
            if (dt.equals(Datatypes.get(IntegerDatatype.NAME))) {
                validator = new IntegerValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (dt.equals(Datatypes.get(LongDatatype.NAME))) {
                validator = new LongValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (dt.equals(Datatypes.get(DoubleDatatype.NAME)) || dt.equals(Datatypes.get(BigDecimalDatatype.NAME))) {
                validator = new DoubleValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (dt.equals(Datatypes.get(DateDatatype.NAME))) {
                validator = new DateValidator(messages.getMainMessage("validation.invalidDate"));
            }
        }
        return validator;
    }

    protected void loadActions(Component.ActionsHolder actionsHolder, Element element) {
        Element actionsEl = element.element("actions");
        if (actionsEl == null)
            return;

        for (Element actionEl : Dom4j.elements(actionsEl, "action")) {
            actionsHolder.addAction(loadDeclarativeAction(actionsHolder, actionEl));
        }
    }

    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (id == null) {
            Element component = element;
            for (int i = 0; i < 2; i++) {
                if (component.getParent() != null)
                    component = component.getParent();
                else
                    throw new GuiDevelopmentException("No action ID provided", context.getFullFrameId());
            }
            throw new GuiDevelopmentException("No action ID provided", context.getFullFrameId(),
                    "Component ID", component.attributeValue("id"));
        }

        String trackSelection = element.attributeValue("trackSelection");
        String shortcut = StringUtils.trimToNull(element.attributeValue("shortcut"));
        if (Boolean.valueOf(trackSelection)) {
            return new DeclarativeTrackingAction(
                    id,
                    loadResourceString(element.attributeValue("caption")),
                    loadResourceString(element.attributeValue("description")),
                    loadResourceString(element.attributeValue("icon")),
                    element.attributeValue("enable"),
                    element.attributeValue("visible"),
                    element.attributeValue("invoke"),
                    shortcut,
                    actionsHolder
            );
        } else {
            return new DeclarativeAction(
                    id,
                    loadResourceString(element.attributeValue("caption")),
                    loadResourceString(element.attributeValue("description")),
                    loadResourceString(element.attributeValue("icon")),
                    element.attributeValue("enable"),
                    element.attributeValue("visible"),
                    element.attributeValue("invoke"),
                    shortcut,
                    actionsHolder
            );
        }
    }

    protected Action loadPickerDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (id == null) {
            Element component = element;
            for (int i = 0; i < 2; i++) {
                if (component.getParent() != null) {
                    component = component.getParent();
                } else {
                    throw new GuiDevelopmentException("No action ID provided for " + element.getName(), context.getFullFrameId());
                }
            }
            throw new GuiDevelopmentException("No action ID provided for " + element.getName(), context.getFullFrameId(),
                    "PickerField ID", component.attributeValue("id"));
        }

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            // Try to create a standard picker action
            for (PickerField.ActionType type : PickerField.ActionType.values()) {
                if (type.getId().equals(id)) {
                    Action action = type.createAction((PickerField) actionsHolder);
                    if (type != PickerField.ActionType.LOOKUP && type != PickerField.ActionType.OPEN) {
                        return action;
                    }

                    String openTypeString = element.attributeValue("openType");
                    if (openTypeString == null) {
                        return action;
                    }

                    WindowManager.OpenType openType;
                    try {
                        openType = WindowManager.OpenType.valueOf(openTypeString);
                    } catch (IllegalArgumentException e) {
                        throw new GuiDevelopmentException(
                                "Unknown open type: '" + openTypeString + "' for action: '" + id + "'", context.getFullFrameId());
                    }

                    if (action instanceof PickerField.LookupAction) {
                        ((PickerField.LookupAction) action).setLookupScreenOpenType(openType);
                    } else if (action instanceof PickerField.OpenAction) {
                        ((PickerField.OpenAction) action).setEditScreenOpenType(openType);
                    }
                    return action;
                }
            }
        }

        return loadDeclarativeAction(actionsHolder, element);
    }

    protected Formatter loadFormatter(Element element) {
        final Element formatterElement = element.element("formatter");
        if (formatterElement != null) {
            final String className = formatterElement.attributeValue("class");

            if (StringUtils.isEmpty(className)) {
                throw new GuiDevelopmentException("Formatter's attribute 'class' is not specified", context.getCurrentFrameId());
            }

            Class<?> aClass = scripting.loadClass(className);
            if (aClass == null) {
                throw new GuiDevelopmentException(String.format("Class %s is not found", className), context.getFullFrameId());
            }

            try {
                final Constructor<?> constructor = aClass.getConstructor(Element.class);
                try {
                    return (Formatter) constructor.newInstance(formatterElement);
                } catch (Throwable e) {
                    throw new GuiDevelopmentException("Unable to instatiate class " + className + ": " + e.toString(),
                            context.getFullFrameId());
                }
            } catch (NoSuchMethodException e) {
                try {
                    return (Formatter) aClass.newInstance();
                } catch (Exception e1) {
                    throw new GuiDevelopmentException("Unable to instatiate class " + className + ": " + e1.toString(),
                            context.getFullFrameId());
                }
            }
        } else {
            return null;
        }
    }

    protected ComponentLoader getLoader(Element element, String name) {
        Class<? extends ComponentLoader> loaderClass = layoutLoaderConfig.getLoader(name);
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + name, context.getFullFrameId());
        }

        ComponentLoader loader;
        try {
            Constructor<? extends ComponentLoader> constructor = loaderClass.getConstructor();
            loader = constructor.newInstance();

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
            loader.setContext(context);
            loader.setLayoutLoaderConfig(layoutLoaderConfig);
            loader.setFactory(factory);
            loader.setElement(element);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new GuiDevelopmentException("Loader instatiation error: " + e, context.getFullFrameId());
        }

        return loader;
    }
}