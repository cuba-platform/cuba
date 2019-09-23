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
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.components.validators.*;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.DeclarativeAction;
import com.haulmont.cuba.gui.xml.DeclarativeTrackingAction;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkState;
import static com.haulmont.cuba.gui.icons.Icons.ICON_NAME_REGEX;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public abstract class AbstractComponentLoader<T extends Component> implements ComponentLoader<T> {

    protected static final Map<String, Function<ClientConfig, String>> SHORTCUT_ALIASES =
            ImmutableMap.<String, Function<ClientConfig, String>>builder()
                    .put("TABLE_EDIT_SHORTCUT", ClientConfig::getTableEditShortcut)
                    .put("TABLE_INSERT_SHORTCUT", ClientConfig::getTableInsertShortcut)
                    .put("TABLE_ADD_SHORTCUT", ClientConfig::getTableAddShortcut)
                    .put("TABLE_REMOVE_SHORTCUT", ClientConfig::getTableRemoveShortcut)
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

    protected Context context;

    protected UiComponents factory;
    protected LayoutLoaderConfig layoutLoaderConfig;
    protected Element element;

    protected T resultComponent;

    protected BeanLocator beanLocator;

    protected AbstractComponentLoader() {
    }

    @Override
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    protected ComponentContext getComponentContext() {
        checkState(context instanceof ComponentContext,
                "'context' must implement com.haulmont.cuba.gui.xml.layout.ComponentLoader.ComponentContext");

        return (ComponentContext) context;
    }

    protected CompositeComponentContext getCompositeComponentContext() {
        checkState(context instanceof CompositeComponentContext,
                "'context' must implement com.haulmont.cuba.gui.xml.layout.ComponentLoader.CompositeComponentContext");

        return (CompositeComponentContext) context;
    }

    @Override
    public UiComponents getFactory() {
        return factory;
    }

    @Override
    public void setFactory(UiComponents factory) {
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

    protected Security getSecurity() {
        return beanLocator.get(Security.NAME);
    }

    protected Messages getMessages() {
        return beanLocator.get(Messages.NAME);
    }

    protected MessageTools getMessageTools() {
        return beanLocator.get(MessageTools.NAME);
    }

    protected Scripting getScripting() {
        return beanLocator.get(Scripting.NAME);
    }

    protected Configuration getConfiguration() {
        return beanLocator.get(Configuration.NAME);
    }

    protected ThemeConstants getTheme() {
        ThemeConstantsManager manager = beanLocator.get(ThemeConstantsManager.NAME);
        return manager.getConstants();
    }

    protected boolean isLegacyFrame() {
        return context instanceof ComponentContext
                && ((ComponentContext) context).getFrame().getFrameOwner() instanceof LegacyFrame;
    }

    protected LayoutLoader getLayoutLoader() {
        return beanLocator.getPrototype(LayoutLoader.NAME, context);
    }

    protected LayoutLoader getLayoutLoader(Context context) {
        return beanLocator.getPrototype(LayoutLoader.NAME, context);
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

    protected void loadCss(Component component, Element element) {
        String css = element.attributeValue("css");
        if (StringUtils.isNotEmpty(css)) {
            HtmlAttributes htmlAttributes = beanLocator.get(HtmlAttributes.NAME);
            htmlAttributes.applyCss(component, css);
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
            String editable = element.attributeValue("editable");
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

            if (component instanceof HasHtmlCaption) {
                loadCaptionAsHtml((HasHtmlCaption) component, element);
            }
        }
    }

    protected void loadCaptionAsHtml(HasHtmlCaption component, Element element) {
        String captionAsHtml = element.attributeValue("captionAsHtml");
        if (!Strings.isNullOrEmpty(captionAsHtml)) {
            component.setCaptionAsHtml(Boolean.parseBoolean(captionAsHtml));
        }
    }

    protected void loadDescription(Component.HasDescription component, Element element) {
        if (element.attribute("description") != null) {
            String description = element.attributeValue("description");

            description = loadResourceString(description);
            component.setDescription(description);

            if (component instanceof HasHtmlDescription) {
                loadDescriptionAsHtml((HasHtmlDescription) component, element);
            }
        }
    }

    protected void loadDescriptionAsHtml(HasHtmlDescription component, Element element) {
        String descriptionAsHtml = element.attributeValue("descriptionAsHtml");
        if (!Strings.isNullOrEmpty(descriptionAsHtml)) {
            component.setDescriptionAsHtml(Boolean.parseBoolean(descriptionAsHtml));
        }
    }

    protected void loadContextHelp(HasContextHelp component, Element element) {
        String contextHelpText = element.attributeValue("contextHelpText");
        if (StringUtils.isNotEmpty(contextHelpText)) {
            contextHelpText = loadResourceString(contextHelpText);
            component.setContextHelpText(contextHelpText);
        }

        String htmlEnabled = element.attributeValue("contextHelpTextHtmlEnabled");
        if (StringUtils.isNotEmpty(htmlEnabled)) {
            component.setContextHelpTextHtmlEnabled(Boolean.parseBoolean(htmlEnabled));
        }
    }

    protected boolean loadVisible(Component component, Element element) {
        if (component instanceof DatasourceComponent
                && ((DatasourceComponent) component).getDatasource() != null) {

            DatasourceComponent wiredComponent = (DatasourceComponent) component;
            MetaClass metaClass = wiredComponent.getDatasource().getMetaClass();
            MetaPropertyPath propertyPath = wiredComponent.getMetaPropertyPath();

            if (propertyPath != null
                    && !getSecurity().isEntityAttrReadPermitted(metaClass, propertyPath.toString())) {
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

        return getMessageTools().loadString(context.getMessagesPack(), caption);
    }

    protected String loadThemeString(String value) {
        if (value != null && value.startsWith(ThemeConstants.PREFIX)) {
            value = getTheme().get(value.substring(ThemeConstants.PREFIX.length()));
        }
        return value;
    }

    protected int loadThemeInt(String value) {
        if (value != null && value.startsWith(ThemeConstants.PREFIX)) {
            value = getTheme().get(value.substring(ThemeConstants.PREFIX.length()));
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
        String height = element.attributeValue("height");
        if (StringUtils.isNotEmpty(height)) {
            if ("auto".equalsIgnoreCase(height)) {
                component.setHeight(Component.AUTO_SIZE);
            } else {
                component.setHeight(loadThemeString(height));
            }
        }
    }

    protected void loadHeight(Component component, Element element, @Nullable String defaultValue) {
        String height = element.attributeValue("height");
        if ("auto".equalsIgnoreCase(height)) {
            component.setHeight(Component.AUTO_SIZE);
        } else if (!StringUtils.isBlank(height)) {
            component.setHeight(loadThemeString(height));
        } else if (!StringUtils.isBlank(defaultValue)) {
            component.setHeight(defaultValue);
        }
    }

    protected void loadWidth(Component component, Element element) {
        String width = element.attributeValue("width");
        if (StringUtils.isNotEmpty(width)) {
            if ("auto".equalsIgnoreCase(width)) {
                component.setWidth(Component.AUTO_SIZE);
            } else {
                component.setWidth(loadThemeString(width));
            }
        }
    }

    protected void loadTabIndex(Component.Focusable component, Element element) {
        String tabIndex = element.attributeValue("tabIndex");
        if (StringUtils.isNotEmpty(tabIndex)) {
            component.setTabIndex(Integer.parseInt(tabIndex));
        }
    }

    protected void loadSettingsEnabled(HasSettings component, Element element) {
        String settingsEnabled = element.attributeValue("settingsEnabled");
        if (StringUtils.isNotEmpty(settingsEnabled)) {
            component.setSettingsEnabled(Boolean.parseBoolean(settingsEnabled));
        }
    }

    protected void loadWidth(Component component, Element element, @Nullable String defaultValue) {
        String width = element.attributeValue("width");
        if ("auto".equalsIgnoreCase(width)) {
            component.setWidth(Component.AUTO_SIZE);
        } else if (!StringUtils.isBlank(width)) {
            component.setWidth(loadThemeString(width));
        } else if (!StringUtils.isBlank(defaultValue)) {
            component.setWidth(defaultValue);
        }
    }

    protected void loadCollapsible(Collapsable component, Element element, boolean defaultCollapsable) {
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

    protected void loadBorder(HasBorder component, Element element) {
        String border = element.attributeValue("border");
        if (!StringUtils.isEmpty(border)) {
            if ("visible".equalsIgnoreCase(border)) {
                component.setBorderVisible(true);
            } else if ("hidden".equalsIgnoreCase(border)) {
                component.setBorderVisible(false);
            }
        }
    }

    protected void loadMargin(HasMargin layout, Element element) {
        String margin = element.attributeValue("margin");
        if (!StringUtils.isEmpty(margin)) {
            MarginInfo marginInfo = parseMarginInfo(margin);
            layout.setMargin(marginInfo);
        }
    }

    protected MarginInfo parseMarginInfo(String margin) {
        if (margin.contains(";") || margin.contains(",")) {
            String[] margins = margin.split("[;,]");
            if (margins.length != 4) {
                throw new GuiDevelopmentException(
                        "Margin attribute must contain 1 or 4 boolean values separated by ',' or ';", context);
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

    protected void assignFrame(Component.BelongToFrame component) {
        if (context instanceof ComponentContext
                && ((ComponentContext) context).getFrame() != null) {
            component.setFrame(((ComponentContext) context).getFrame());
        }
    }

    protected void loadAction(ActionOwner component, Element element) {
        String actionId = element.attributeValue("action");
        if (!StringUtils.isEmpty(actionId)) {
            ComponentContext componentContext = getComponentContext();
            componentContext.addPostInitTask(
                    new ActionOwnerAssignActionPostInitTask(component, actionId, componentContext.getFrame())
            );
        }
    }

    protected void loadPresentations(HasPresentations component, Element element) {
        String presentations = element.attributeValue("presentations");
        if (StringUtils.isNotEmpty(presentations)) {
            component.usePresentations(Boolean.parseBoolean(presentations));
            getComponentContext().addPostInitTask(new LoadPresentationsPostInitTask(component));
        }
    }

    protected void loadIcon(Component.HasIcon component, Element element) {
        if (element.attribute("icon") != null) {
            String icon = element.attributeValue("icon");
            component.setIcon(getIconPath(icon));
        }
    }

    protected String getIconPath(String icon) {
        if (icon == null || icon.isEmpty()) {
            return null;
        }

        String iconPath = null;

        if (ICON_NAME_REGEX.matcher(icon).matches()) {
            Icons icons = beanLocator.get(Icons.NAME);
            iconPath = icons.get(icon);
        }

        if (StringUtils.isEmpty(iconPath)) {
            String themeValue = loadThemeString(icon);
            iconPath = loadResourceString(themeValue);
        }

        return iconPath;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    protected Consumer<?> loadValidator(Element validatorElement) {
        String className = validatorElement.attributeValue("class");
        String scriptPath = validatorElement.attributeValue("script");
        String script = validatorElement.getText();

        Consumer<?> validator = null;

        if (StringUtils.isNotBlank(scriptPath) || StringUtils.isNotBlank(script)) {
            validator = new ScriptValidator(validatorElement, context.getMessagesPack());
        } else {
            Class aClass = getScripting().loadClass(className);
            if (aClass == null)
                throw new GuiDevelopmentException(String.format("Class %s is not found", className), context);
            if (!StringUtils.isBlank(context.getMessagesPack()))
                try {
                    validator = (Consumer<?>) ReflectionHelper.newInstance(aClass, validatorElement, context.getMessagesPack());
                } catch (NoSuchMethodException e) {
                    //
                }
            if (validator == null) {
                try {
                    validator = (Consumer<?>) ReflectionHelper.newInstance(aClass, validatorElement);
                } catch (NoSuchMethodException e) {
                    try {
                        validator = (Consumer<?>) ReflectionHelper.newInstance(aClass);
                    } catch (NoSuchMethodException e1) {
                        // todo log warn
                    }
                }
            }
            if (validator == null) {
                throw new GuiDevelopmentException(
                        String.format("Validator class %s has no supported constructors", aClass), context);
            }
        }
        return validator;
    }

    @Deprecated
    protected Consumer<?> getDefaultValidator(MetaProperty property) {
        Consumer<?> validator = null;
        if (property.getRange().isDatatype()) {
            Messages messages = getMessages();

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

    protected void loadActions(ActionsHolder actionsHolder, Element element) {
        Element actionsEl = element.element("actions");
        if (actionsEl == null) {
            return;
        }

        for (Element actionEl : actionsEl.elements("action")) {
            actionsHolder.addAction(loadDeclarativeAction(actionsHolder, actionEl));
        }
    }

    protected Action loadDeclarativeAction(ActionsHolder actionsHolder, Element element) {
        return loadDeclarativeActionDefault(actionsHolder, element);
    }

    protected Action loadDeclarativeActionDefault(ActionsHolder actionsHolder, Element element) {
        String id = loadActionId(element);

        String trackSelection = element.attributeValue("trackSelection");

        boolean shouldTrackSelection = Boolean.parseBoolean(trackSelection);
        String invokeMethod = element.attributeValue("invoke");

        if (StringUtils.isEmpty(invokeMethod)) {
            return loadStubAction(element, id, shouldTrackSelection);
        }

        return loadInvokeAction(actionsHolder, element, id, shouldTrackSelection, invokeMethod);
    }

    @Nonnull
    protected String loadActionId(Element element) {
        String id = element.attributeValue("id");
        if (id == null) {
            Element component = element;
            for (int i = 0; i < 2; i++) {
                if (component.getParent() != null)
                    component = component.getParent();
                else
                    throw new GuiDevelopmentException("No action ID provided", context);
            }
            throw new GuiDevelopmentException("No action ID provided", context,
                    "Component ID", component.attributeValue("id"));
        }
        return id;
    }

    protected Action loadInvokeAction(ActionsHolder actionsHolder, Element element, String id, boolean shouldTrackSelection, String invokeMethod) {
        BaseAction action;
        String shortcut = loadShortcut(trimToNull(element.attributeValue("shortcut")));
        if (shouldTrackSelection) {
            action = new DeclarativeTrackingAction(
                    id,
                    loadResourceString(element.attributeValue("caption")),
                    loadResourceString(element.attributeValue("description")),
                    getIconPath(element.attributeValue("icon")),
                    element.attributeValue("enable"),
                    element.attributeValue("visible"),
                    invokeMethod,
                    shortcut,
                    actionsHolder
            );

            loadActionConstraint(action, element);

            return action;
        } else {
            action = new DeclarativeAction(
                    id,
                    loadResourceString(element.attributeValue("caption")),
                    loadResourceString(element.attributeValue("description")),
                    getIconPath(element.attributeValue("icon")),
                    element.attributeValue("enable"),
                    element.attributeValue("visible"),
                    invokeMethod,
                    shortcut,
                    actionsHolder
            );
        }
        action.setPrimary(Boolean.parseBoolean(element.attributeValue("primary")));
        return action;
    }

    protected Action loadStubAction(Element element, String id, boolean shouldTrackSelection) {
        Action targetAction;

        if (shouldTrackSelection) {
            targetAction = new ItemTrackingAction(id);
        } else {
            targetAction = new BaseAction(id);
        }

        initAction(element, targetAction);

        return targetAction;
    }

    protected void initAction(Element element, Action targetAction) {
        String caption = element.attributeValue("caption");
        if (StringUtils.isNotEmpty(caption)) {
            targetAction.setCaption(loadResourceString(caption));
        }

        String description = element.attributeValue("description");
        if (StringUtils.isNotEmpty(description)) {
            targetAction.setDescription(loadResourceString(description));
        }

        String icon = element.attributeValue("icon");
        if (StringUtils.isNotEmpty(icon)) {
            targetAction.setIcon(getIconPath(icon));
        }

        String enable = element.attributeValue("enable");
        if (StringUtils.isNotEmpty(enable)) {
            targetAction.setEnabled(Boolean.parseBoolean(enable));
        }

        String visible = element.attributeValue("visible");
        if (StringUtils.isNotEmpty(visible)) {
            targetAction.setVisible(Boolean.parseBoolean(visible));
        }

        String shortcut = trimToNull(element.attributeValue("shortcut"));
        if (shortcut != null) {
            targetAction.setShortcut(loadShortcut(shortcut));
        }

        Element propertiesEl = element.element("properties");
        if (propertiesEl != null) {
            ActionCustomPropertyLoader propertyLoader = beanLocator.get(ActionCustomPropertyLoader.class);
            for (Element propertyEl : propertiesEl.elements("property")) {
                propertyLoader.load(targetAction,
                        propertyEl.attributeValue("name"), propertyEl.attributeValue("value"));
            }
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
                throw new GuiDevelopmentException(message, context);
            }

            String fqnConfigName = splittedShortcut[0].substring(2);
            String methodName = splittedShortcut[1].substring(0, splittedShortcut[1].length() - 1);

            //noinspection unchecked
            Class<Config> configClass = (Class<Config>) getScripting().loadClass(fqnConfigName);
            if (configClass != null) {
                Config config = getConfiguration().getConfig(configClass);

                try {
                    String shortcutValue = (String) MethodUtils.invokeMethod(config, methodName);
                    if (StringUtils.isNotEmpty(shortcutValue)) {
                        return shortcutValue;
                    }
                } catch (NoSuchMethodException e) {
                    String message = String.format("An error occurred while loading shortcut: " +
                            "can't find method \"%s\" in \"%s\"", methodName, fqnConfigName);
                    throw new GuiDevelopmentException(message, context);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    String message = String.format("An error occurred while loading shortcut: " +
                            "can't invoke method \"%s\" in \"%s\"", methodName, fqnConfigName);
                    throw new GuiDevelopmentException(message, context);
                }
            } else {
                String message = String.format("An error occurred while loading shortcut: " +
                        "can't find config interface \"%s\"", fqnConfigName);
                throw new GuiDevelopmentException(message, context);
            }
        }
        return null;
    }

    protected String loadShortcutFromAlias(String shortcut) {
        if (shortcut.endsWith("_SHORTCUT}")) {
            String alias = shortcut.substring(2, shortcut.length() - 1);
            if (SHORTCUT_ALIASES.containsKey(alias)) {
                ClientConfig clientConfig = getConfiguration().getConfig(ClientConfig.class);
                return SHORTCUT_ALIASES.get(alias).apply(clientConfig);
            } else {
                String message = String.format("An error occurred while loading shortcut. " +
                        "Can't find shortcut for alias \"%s\"", alias);
                throw new GuiDevelopmentException(message, context);
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
                throw new GuiDevelopmentException(message, context);
            }
        }
        return null;
    }

    protected Action loadPickerDeclarativeAction(ActionsHolder actionsHolder, Element element) {
        String id = loadActionId(element);

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            if (isLegacyFrame()) {
                Action legacyAction = loadLegacyPickerAction((PickerField) actionsHolder, id);
                if (legacyAction != null) {
                    return legacyAction;
                }
            } else {
                String type = element.attributeValue("type");
                if (StringUtils.isNotEmpty(type)) {
                    Actions actions = beanLocator.get(Actions.NAME);

                    Action action = actions.create(type, id);
                    initAction(element, action);

                    return action;
                }
            }
        }

        return loadDeclarativeActionDefault(actionsHolder, element);
    }

    @Deprecated
    @Nullable
    protected Action loadLegacyPickerAction(PickerField actionsHolder, String actionId) {
        // Try to create a standard picker action
        for (PickerField.ActionType type : PickerField.ActionType.values()) {
            if (type.getId().equals(actionId)) {
                Action action = type.createAction(actionsHolder);
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
                            String.format("Unknown open type: '%s' for action: '%s'", openTypeString, actionId),
                            context);
                }

                if (action instanceof PickerField.LookupAction) {
                    ((PickerField.LookupAction) action).setLookupScreenOpenType(openType);
                } else if (action instanceof PickerField.OpenAction) {
                    ((PickerField.OpenAction) action).setEditScreenOpenType(openType);
                }
                return action;
            }
        }
        return null;
    }

    protected Function<?, String> loadFormatter(Element element) {
        Element formatterElement = element.element("formatter");
        if (formatterElement != null) {
            String className = formatterElement.attributeValue("class");

            if (StringUtils.isEmpty(className)) {
                throw new GuiDevelopmentException("Formatter's attribute 'class' is not specified", context);
            }

            Class<?> aClass = getScripting().loadClass(className);
            if (aClass == null) {
                throw new GuiDevelopmentException(String.format("Class %s is not found", className), context);
            }

            try {
                Constructor<?> constructor = aClass.getConstructor(Element.class);
                try {
                    //noinspection unchecked
                    return (Function<?, String>) constructor.newInstance(formatterElement);
                } catch (Throwable e) {
                    throw new GuiDevelopmentException(
                            String.format("Unable to instantiate class %s: %s", className, e.toString()), context);
                }
            } catch (NoSuchMethodException e) {
                try {
                    //noinspection unchecked
                    return (Function<?, String>) aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e1) {
                    throw new GuiDevelopmentException(
                            String.format("Unable to instantiate class %s: %s", className, e1.toString()), context);
                }
            }
        } else {
            return null;
        }
    }

    protected void loadOrientation(HasOrientation component, Element element) {
        String orientation = element.attributeValue("orientation");
        if (orientation == null) {
            return;
        }

        if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(HasOrientation.Orientation.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(HasOrientation.Orientation.VERTICAL);
        } else {
            throw new GuiDevelopmentException("Invalid orientation value: " + orientation, context,
                    "Component ID", ((Component) component).getId());
        }
    }

    protected void loadInputPrompt(HasInputPrompt component, Element element) {
        String inputPrompt = element.attributeValue("inputPrompt");
        if (StringUtils.isNotBlank(inputPrompt)) {
            component.setInputPrompt(loadResourceString(inputPrompt));
        }
    }

    protected void loadFocusable(Component.Focusable component, Element element) {
        String focusable = element.attributeValue("focusable");
        if (StringUtils.isNotBlank(focusable)) {
            component.setFocusable(Boolean.parseBoolean(focusable));
        }
    }

    @Nullable
    protected String getWindowId(Context context) {
        if (context instanceof ComponentContext) {
            Frame frame = getComponentContext().getFrame();
            Screen screen = UiControllerUtils.getScreen(frame.getFrameOwner());
            return screen.getId();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected void loadContainer(T component, Element element) {
        if (component instanceof HasValueSource) {
            String property = element.attributeValue("property");
            loadContainer(element, property).ifPresent(container ->
                    ((HasValueSource) component).setValueSource(new ContainerValueSource<>(container, property)));
        }
    }

    protected Optional<InstanceContainer> loadContainer(Element element, String property) {
        String containerId = element.attributeValue("dataContainer");

        // In case a component has only a property,
        // we try to obtain `dataContainer` from a parent element.
        // For instance, a component is placed within the Form component
        if (Strings.isNullOrEmpty(containerId) && property != null) {
            containerId = getParentDataContainer(element);
        }

        if (!Strings.isNullOrEmpty(containerId)) {
            if (property == null) {
                throw new GuiDevelopmentException(
                        String.format("Can't set container '%s' for component '%s' because 'property' " +
                                "attribute is not defined", containerId, element.attributeValue("id")), context);
            }

            FrameOwner frameOwner = getComponentContext().getFrame().getFrameOwner();
            ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);

            return Optional.of(screenData.getContainer(containerId));
        }

        return Optional.empty();
    }

    protected Optional<CollectionContainer> loadOptionsContainer(Element element) {
        String containerId = element.attributeValue("optionsContainer");
        if (containerId != null) {
            FrameOwner frameOwner = getComponentContext().getFrame().getFrameOwner();
            ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);
            InstanceContainer container = screenData.getContainer(containerId);
            if (!(container instanceof CollectionContainer)) {
                throw new GuiDevelopmentException("Not a CollectionContainer: " + containerId, context);
            }
            return Optional.of((CollectionContainer) container);
        }

        return Optional.empty();
    }

    protected String getParentDataContainer(Element element) {
        Element parent = element.getParent();
        while (parent != null) {
            if (layoutLoaderConfig.getLoader(parent.getName()) != null) {
                return parent.attributeValue("dataContainer");
            }
            parent = parent.getParent();
        }
        return null;
    }

    @Nullable
    protected Component findComponent(String componentId) {
        if (context instanceof ComponentContext) {
            return getComponentContext().getFrame().getComponent(componentId);
        } else if (context instanceof CompositeComponentContext) {
            // We assume that CompositeComponent has only one root component
            Component current = resultComponent;
            while (current.getParent() != null) {
                current = current.getParent();
            }

            if (current instanceof ComponentContainer) {
                return ((ComponentContainer) current).getComponent(componentId);
            }
        }

        return null;
    }

    protected void loadRequiredIndicatorVisible(HasRequiredIndicator component, Element element) {
        String requiredIndicatorVisible = element.attributeValue("requiredIndicatorVisible");
        if (!Strings.isNullOrEmpty(requiredIndicatorVisible)) {
            component.setRequiredIndicatorVisible(Boolean.parseBoolean(requiredIndicatorVisible));
        }
    }
}