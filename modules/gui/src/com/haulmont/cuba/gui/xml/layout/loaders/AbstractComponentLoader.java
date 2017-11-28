/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.validators.*;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.DeclarativeAction;
import com.haulmont.cuba.gui.xml.DeclarativeTrackingAction;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public abstract class AbstractComponentLoader<T extends Component> implements ComponentLoader<T> {
    protected static final Pattern ICON_LITERAL_REGEX = Pattern.compile("[A-Z_]*");

    protected static final Map<String, Function<ClientConfig, String>> shortcutAliases =
            ImmutableMap.<String, Function<ClientConfig, String>>builder()
                    .put("TABLE_EDIT_SHORTCUT", ClientConfig::getTableEditShortcut)
                    .put("COMMIT_SHORTCUT", ClientConfig::getCommitShortcut)
                    .put("CLOSE_SHORTCUT", ClientConfig::getCloseShortcut)
                    .put("FILTER_APPLY_SHORTCUT", ClientConfig::getFilterApplyShortcut)
                    .put("FILTER_SELECT_SHORTCUT", ClientConfig::getFilterSelectShortcut)
                    .put("NEXT_TAB_SHORTCUT", ClientConfig::getNextTabShortcut)
                    .put("PREVIOUS_TAB_SHORTCUT", ClientConfig::getPreviousTabShortcut)
                    .put("PICKER_LOOKUP_SHORTCUT", ClientConfig::getPickerLookupShortcut)
                    .put("PICKER_OPEN_SHORTCUT", ClientConfig::getPickerOpenShortcut)
                    .put("PICKER_CLEAR_SHORTCUT", ClientConfig::getPickerClearShortcut)
                    .build();

    protected Locale locale;
    protected String messagesPack;
    protected Context context;

    protected Security security = AppBeans.get(Security.NAME);

    protected Messages messages = AppBeans.get(Messages.NAME);
    protected MessageTools messageTools = AppBeans.get(MessageTools.NAME);
    protected Scripting scripting = AppBeans.get(Scripting.NAME);
    protected Resources resources = AppBeans.get(Resources.NAME);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected Configuration configuration = AppBeans.get(Configuration.NAME);
    protected ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

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

    protected void loadResponsive(Component component, Element element) {
        String responsive = element.attributeValue("responsive");
        if (StringUtils.isNotEmpty(responsive)) {
            component.setResponsive(Boolean.parseBoolean(responsive));
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
                ((Component.Editable) component).setEditable(Boolean.parseBoolean(editable));
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
        if (StringUtils.isNotEmpty(visible)) {
            boolean visibleValue = Boolean.parseBoolean(visible);
            component.setVisible(visibleValue);

            return visibleValue;
        }

        return true;
    }

    protected boolean loadEnable(Component component, Element element) {
        String enable = element.attributeValue("enable");
        if (StringUtils.isNotEmpty(enable)) {
            boolean enabled = Boolean.parseBoolean(enable);
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

    protected int loadThemeInt(String value) {
        if (value != null && value.startsWith(ThemeConstants.PREFIX)) {
            value = themeConstants.get(value.substring(ThemeConstants.PREFIX.length()));
        }
        return value == null ? 0 : Integer.parseInt(value);
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

    protected void loadTabIndex(Component.Focusable component, Element element) {
        String tabIndex = element.attributeValue("tabIndex");
        if (StringUtils.isNotEmpty(tabIndex)) {
            component.setTabIndex(Integer.parseInt(tabIndex));
        }
    }

    protected void loadSettingsEnabled(Component.HasSettings component, Element element) {
        String settingsEnabled = element.attributeValue("settingsEnabled");
        if (StringUtils.isNotEmpty(settingsEnabled)) {
            component.setSettingsEnabled(Boolean.parseBoolean(settingsEnabled));
        }
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
        boolean b = Strings.isNullOrEmpty(collapsable) ? defaultCollapsable : Boolean.parseBoolean(collapsable);
        component.setCollapsable(b);
        if (b) {
            String collapsed = element.attributeValue("collapsed");
            if (!StringUtils.isBlank(collapsed)) {
                component.setExpanded(!Boolean.parseBoolean(collapsed));
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
            MarginInfo marginInfo = parseMarginInfo(margin);
            layout.setMargin(marginInfo);
        }
    }

    protected MarginInfo parseMarginInfo(String margin) {
        if (margin.contains(";") || margin.contains(",")) {
            final String[] margins = margin.split("[;,]");
            if (margins.length != 4) {
                throw new GuiDevelopmentException(
                        "Margin attribute must contain 1 or 4 boolean values separated by ',' or ';", context.getFullFrameId());
            }

            return new MarginInfo(
                    Boolean.parseBoolean(StringUtils.trimToEmpty(margins[0])),
                    Boolean.parseBoolean(StringUtils.trimToEmpty(margins[1])),
                    Boolean.parseBoolean(StringUtils.trimToEmpty(margins[2])),
                    Boolean.parseBoolean(StringUtils.trimToEmpty(margins[3]))
            );
        } else {
            return new MarginInfo(Boolean.parseBoolean(margin));
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
        if (StringUtils.isNotEmpty(presentations)) {
            component.usePresentations(Boolean.parseBoolean(presentations));
            context.addPostInitTask(new LoadPresentationsPostInitTask(component));
        }
    }

    protected void loadIcon(Component.HasIcon component, Element element) {
        if (element.attribute("icon") != null) {
            String icon = element.attributeValue("icon");

            String iconPath = null;

            if (ICON_LITERAL_REGEX.matcher(icon).matches()) {
                iconPath = AppBeans.get(Icons.class).get(icon);
            }

            if (StringUtils.isEmpty(iconPath)) {
                String themeValue = loadThemeString(icon);
                iconPath = loadResourceString(themeValue);
            }

            component.setIcon(iconPath);
        }
    }

    @SuppressWarnings("unchecked")
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
            Class type = property.getRange().asDatatype().getJavaClass();
            if (type.equals(Integer.class)) {
                validator = new IntegerValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (type.equals(Long.class)) {
                validator = new LongValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (type.equals(Double.class) || type.equals(BigDecimal.class)) {
                validator = new DoubleValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (type.equals(java.sql.Date.class)) {
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
        return loadDeclarativeActionDefault(actionsHolder, element);
    }

    protected Action loadDeclarativeActionDefault(Component.ActionsHolder actionsHolder, Element element) {
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
        shortcut = loadShortcut(shortcut);

        if (Boolean.parseBoolean(trackSelection)) {
            DeclarativeTrackingAction action = new DeclarativeTrackingAction(
                    id,
                    loadResourceString(element.attributeValue("caption")),
                    loadResourceString(element.attributeValue("description")),
                    loadResourceString(loadThemeString(element.attributeValue("icon"))),
                    element.attributeValue("enable"),
                    element.attributeValue("visible"),
                    element.attributeValue("invoke"),
                    shortcut,
                    actionsHolder
            );

            loadActionConstraint(action, element);

            return action;
        } else {
            return new DeclarativeAction(
                    id,
                    loadResourceString(element.attributeValue("caption")),
                    loadResourceString(element.attributeValue("description")),
                    loadResourceString(loadThemeString(element.attributeValue("icon"))),
                    element.attributeValue("enable"),
                    element.attributeValue("visible"),
                    element.attributeValue("invoke"),
                    shortcut,
                    actionsHolder
            );
        }
    }

    protected void loadActionConstraint(Action action, Element element) {
        if (action instanceof Action.HasSecurityConstraint) {
            Action.HasSecurityConstraint itemTrackingAction = (Action.HasSecurityConstraint) action;

            Attribute operationTypeAttribute = element.attribute("constraintOperationType");
            if (operationTypeAttribute != null) {
                ConstraintOperationType operationType
                        = ConstraintOperationType.fromId(operationTypeAttribute.getValue());
                itemTrackingAction.setConstraintOperationType(operationType);
            }

            String constraintCode = element.attributeValue("constraintCode");
            itemTrackingAction.setConstraintCode(constraintCode);
        }
    }

    protected String loadShortcut(String shortcut) {
        if (StringUtils.isNotEmpty(shortcut) && shortcut.startsWith("${") && shortcut.endsWith("}")) {
            String fqnShortcut = loadShortcutFromFQNConfig(shortcut);
            if (fqnShortcut != null) {
                return fqnShortcut;
            }

            String configShortcut = loadShortcutFromConfig(shortcut);
            if (configShortcut != null) {
                return configShortcut;
            }

            String aliasShortcut = loadShortcutFromAlias(shortcut);
            if (aliasShortcut != null)
                return aliasShortcut;
        }
        return shortcut;
    }

    protected String loadShortcutFromFQNConfig(String shortcut) {
        if (shortcut.contains("#")) {
            String[] splittedShortcut = shortcut.split("#");
            if (splittedShortcut.length != 2) {
                String message = "An error occurred while loading shortcut: incorrect format of shortcut.";
                throw new GuiDevelopmentException(message, context.getFullFrameId());
            }

            String fqnConfigName = splittedShortcut[0].substring(2);
            String methodName = splittedShortcut[1].substring(0, splittedShortcut[1].length() - 1);

            //noinspection unchecked
            Class<Config> configClass = (Class<Config>) scripting.loadClass(fqnConfigName);
            if (configClass != null) {
                Config config = configuration.getConfig(configClass);

                try {
                    String shortcutValue = (String) MethodUtils.invokeMethod(config, methodName, null);
                    if (StringUtils.isNotEmpty(shortcutValue)) {
                        return shortcutValue;
                    }
                } catch (NoSuchMethodException e) {
                    String message = String.format("An error occurred while loading shortcut: " +
                            "can't find method \"%s\" in \"%s\"", methodName, fqnConfigName);
                    throw new GuiDevelopmentException(message, context.getFullFrameId());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    String message = String.format("An error occurred while loading shortcut: " +
                            "can't invoke method \"%s\" in \"%s\"", methodName, fqnConfigName);
                    throw new GuiDevelopmentException(message, context.getFullFrameId());
                }
            } else {
                String message = String.format("An error occurred while loading shortcut: " +
                        "can't find config interface \"%s\"", fqnConfigName);
                throw new GuiDevelopmentException(message, context.getFullFrameId());
            }
        }
        return null;
    }

    protected String loadShortcutFromAlias(String shortcut) {
        if (shortcut.endsWith("_SHORTCUT}")) {
            String alias = shortcut.substring(2, shortcut.length() - 1);
            if (shortcutAliases.containsKey(alias)) {
                return shortcutAliases.get(alias).apply(clientConfig);
            } else {
                String message = String.format("An error occurred while loading shortcut. " +
                        "Can't find shortcut for alias \"%s\"", alias);
                throw new GuiDevelopmentException(message, context.getFullFrameId());
            }
        }
        return null;
    }

    protected String loadShortcutFromConfig(String shortcut) {
        if (shortcut.contains(".")) {
            String shortcutPropertyKey = shortcut.substring(2, shortcut.length() - 1);
            String shortcutValue = AppContext.getProperty(shortcutPropertyKey);
            if (StringUtils.isNotEmpty(shortcutValue)) {
                return shortcutValue;
            } else {
                String message = String.format("Action shortcut property \"%s\" doesn't exist", shortcutPropertyKey);
                throw new GuiDevelopmentException(message, context.getFullFrameId());
            }
        }
        return null;
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

        return loadDeclarativeActionDefault(actionsHolder, element);
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

        return getLoader(element, loaderClass);
    }

    protected ComponentLoader getLoader(Element element, Class<? extends ComponentLoader> loaderClass) {
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

    protected void loadInputPrompt(Component.HasInputPrompt component, Element element) {
        String inputPrompt = element.attributeValue("inputPrompt");
        if (StringUtils.isNotBlank(inputPrompt)) {
            component.setInputPrompt(loadResourceString(inputPrompt));
        }
    }

    protected void loadFocusable(Component.Focusable component, Element element) {
        String focusable = element.attributeValue("focusable");
        if (StringUtils.isNotBlank(focusable)) {
            component.setFocusable(Boolean.valueOf(focusable));
        }
    }
}