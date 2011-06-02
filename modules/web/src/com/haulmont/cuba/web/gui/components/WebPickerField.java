/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 12:33:12
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.vaadin.data.Property;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WebPickerField
        extends
            WebAbstractField<com.haulmont.cuba.web.toolkit.ui.PickerField>
        implements
            PickerField, Component.Wrapper
{
    private static final long serialVersionUID = -938974178359790495L;
    
    private CaptionMode captionMode = CaptionMode.ITEM;
    private String captionProperty;

    protected Object value;

    private MetaClass metaClass;

    private String lookupScreen;
    private WindowManager.OpenType lookupScreenOpenType = WindowManager.OpenType.THIS_TAB;

    protected ValueProvider valueProvider;

    protected List<Action> actions = new ArrayList<Action>();

    private String lookupButtonCaption = "";
    private String lookupButtonIcon = "pickerfield/img/lookup-btn.png";
    private String clearButtonCaption = "";
    private String clearButtonIcon = "pickerfield/img/clear-btn.png";

    public WebPickerField() {
        component = new com.haulmont.cuba.web.toolkit.ui.PickerField() {
            @Override
            public void setReadOnly(boolean readOnly) {
                super.setReadOnly(readOnly);
                for (Button button : getButtons()) {
                    if ((button instanceof PickerButton) && isStandardAction(((PickerButton) button).getAction())) {
                        button.setVisible(!readOnly);
                    }
                }
            }
        };
        component.setImmediate(true);
        attachListener(component);
        initActions();
    }

    protected void initActions() {
        addAction(
                new AbstractAction("lookup") {
                    @Override
                    public void actionPerform(Component component) {
                        if (isEditable()) {
                            String windowAlias = getLookupScreen();
                            if (windowAlias == null) {
                                final MetaClass metaClass = getMetaClass();
                                windowAlias = metaClass.getName() + ".lookup";
                            }

                            WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
                            WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);

                            WindowManager wm = App.getInstance().getWindowManager();
                            wm.openLookup(
                                    windowInfo,
                                    new Window.Lookup.Handler() {
                                        public void handleLookup(Collection items) {
                                            if (!items.isEmpty()) {
                                                final Object item = items.iterator().next();
                                                __setValue(item);
                                            }
                                        }
                                    },
                                    lookupScreenOpenType,
                                    valueProvider != null ? valueProvider.getParameters() : Collections.EMPTY_MAP
                            );
                        }
                    }

                    @Override
                    public String getCaption() {
                        return lookupButtonCaption;
                    }

                    @Override
                    public String getIcon() {
                        return lookupButtonIcon;
                    }
                }
        );
        addAction(
                new AbstractAction("clear") {
                    @Override
                    public void actionPerform(Component component) {
                        if (isEditable()) {
                            __setValue(null);
                        }
                    }

                    @Override
                    public String getCaption() {
                        return clearButtonCaption;
                    }

                    @Override
                    public String getIcon() {
                        return clearButtonIcon;
                    }
                }
        );
    }

    private boolean isStandardAction(Action action) {
        return "lookup".equals(action.getId()) || "clear".equals(action.getId());
    }

    private void __setValue(Object newValue) {
        setValue(newValue);
    }

    private ItemWrapper createItemWrapper(final Object newValue) {
        return new ItemWrapper(newValue, metaClass, dsManager) {
            @Override
            public String toString() {
                if (CaptionMode.PROPERTY.equals(captionMode)) {
                    return String.valueOf(getValue() == null ? ""
                            : ((Instance) getValue()).getValue(captionProperty));
                } else {
                    return super.toString();
                }
            }
        };
    }

    public MetaClass getMetaClass() {
        final Datasource ds = getDatasource();
        if (ds != null) {
            return metaProperty.getRange().asClass();
        } else {
            return metaClass;
        }
    }

    public void setMetaClass(MetaClass metaClass) {
        final Datasource ds = getDatasource();
        if (ds != null) throw new IllegalStateException("Datasource is not null");
        this.metaClass = metaClass;
    }

    public String getLookupScreen() {
        return lookupScreen;
    }

    public void setLookupScreen(String lookupScreen) {
        this.lookupScreen = lookupScreen;
    }

    public WindowManager.OpenType getLookupScreenOpenType() {
        return lookupScreenOpenType;
    }

    public void setLookupScreenOpenType(WindowManager.OpenType lookupScreenOpenType) {
        this.lookupScreenOpenType = lookupScreenOpenType;
    }

    @Override
    public <T> T getValue() {
        if (component.getPropertyDataSource() != null) {
            return (T) super.getValue();
        } else {
            return (T) value;
        }
    }

    @Override
    public void setValue(Object value) {
        if (component.getPropertyDataSource() != null) {
            this.value = value;
            super.setValue(value);
        }
        else {
            this.value = value;
            ItemWrapper wrapper = createItemWrapper(value);
            super.setValue(wrapper);
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        dsManager = new DsManager(datasource, this);

        MetaClass metaClass = datasource.getMetaClass();
        this.metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new RuntimeException(String.format("Property '%s' not found in class %s", property, metaClass));
        }

        final MetaPropertyPath propertyPath = new MetaPropertyPath(metaProperty.getDomain(), metaProperty);
        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(propertyPath), dsManager);
        final Property itemProperty = wrapper.getItemProperty(propertyPath);

        component.setPropertyDataSource(itemProperty);

        setRequired(metaProperty.isMandatory());
        
        this.metaClass = metaProperty.getRange().asClass();
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths, DsManager dsManager) {
        return new ItemWrapper(datasource, propertyPaths, dsManager) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
                return new PropertyWrapper(item, propertyPath, dsManager) {
                    @Override
                    public String toString() {
                        if (CaptionMode.PROPERTY.equals(captionMode)) {
                            return String.valueOf(getValue() == null ? "" : ((Instance) getValue()).getValue(captionProperty));
                        } else {
                            return super.toString();
                        }
                    }
                };
            }
        };
    }

    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    public String getCaptionProperty() {
        return captionProperty;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    private Button findButton(String actionName) {
        for (Button button : component.getButtons()) {
            if (button instanceof PickerButton && actionName.equals(((PickerButton) button).getAction().getId()))
                return button;
        }
        return null;
    }

    public void setLookupButtonCaption(String caption) {
        lookupButtonCaption = caption;
        Button button = findButton("lookup");
        if (button != null)
            button.setCaption(caption);
    }

    public void setLookupButtonIcon(String iconName) {
        lookupButtonIcon = iconName;
        Button button = findButton("lookup");
        if (button != null) {
            if (!StringUtils.isBlank(iconName)) {
                button.setIcon(new ThemeResource(iconName));
                button.addStyleName(BaseTheme.BUTTON_LINK);
            } else {
                button.setIcon(null);
                button.removeStyleName(BaseTheme.BUTTON_LINK);
            }
        }
    }

    public void setClearButtonCaption(String caption) {
        clearButtonCaption = caption;
        Button button = findButton("clear");
        if (button != null)
            button.setCaption(caption);
    }

    public void setClearButtonIcon(String iconName) {
        clearButtonIcon = iconName;
        Button button = findButton("clear");
        if (button != null) {
            if (!StringUtils.isBlank(iconName)) {
                button.setIcon(new ThemeResource(iconName));
                button.addStyleName(BaseTheme.BUTTON_LINK);
            } else {
                button.setIcon(null);
                button.removeStyleName(BaseTheme.BUTTON_LINK);
            }
        }
    }

    public ValueProvider getValueProvider() {
        return valueProvider;
    }

    public void setValueProvider(ValueProvider valueProvider) {
        this.valueProvider = valueProvider;
    }

    @Override
    public void addAction(Action action) {
        actions.add(action);
        PickerButton button = new PickerButton(action);
        component.addButton(button);
    }

    @Override
    public void removeAction(Action action) {
        actions.remove(action);
        for (Button button : component.getButtons()) {
            if ((button instanceof PickerButton) && ((PickerButton) button).getAction() == action) {
                component.removeButton(button);
                break;
            }
        }
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    public Action getAction(String id) {
        return null;
    }

    public class PickerButton extends Button {

        private Action action;

        public PickerButton(Action action) {
            this.action = action;
            setCaption(action.getCaption());
            String icon = action.getIcon();
            if (!StringUtils.isBlank(icon)) {
                setIcon(new ThemeResource(icon));
                addStyleName(BaseTheme.BUTTON_LINK);
            }
            addListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            PickerButton.this.action.actionPerform(WebPickerField.this);
                        }
                    }
            );
        }

        public Action getAction() {
            return action;
        }
    }
}
