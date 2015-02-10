/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.EntityLinkField;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaButtonField;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Button;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebEntityLinkField extends WebAbstractField<CubaButtonField> implements EntityLinkField {

    protected EntityLinkClickHandler clickHandler;

    protected String screen;
    protected WindowManager.OpenType screenOpenType = WindowManager.OpenType.THIS_TAB;
    protected DialogParams screenDialogParams;
    protected ScreenCloseListener screenCloseListener;
    protected Map<String, Object> screenParams;

    protected MetaClass metaClass;
    protected ListComponent owner;

    public WebEntityLinkField() {
        component = new CubaButtonField();
        component.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (clickHandler != null) {
                    clickHandler.onClick(WebEntityLinkField.this);
                } else {
                    openEntityEditor();
                }
            }
        });
        component.setInvalidCommitted(true);
        component.setCaptionFormatter(new Converter() {
            @Override
            public Object convertToModel(Object value, Class targetType, Locale locale) throws ConversionException {
                return null;
            }

            @Override
            public Object convertToPresentation(Object value, Class targetType, Locale locale)
                    throws ConversionException {
                if (value == null) {
                    return "";
                }

                if (value instanceof Instance) {
                    return ((Instance) value).getInstanceName();
                }

                Datatype datatype = Datatypes.getNN(value.getClass());

                if (locale != null) {
                    //noinspection unchecked
                    return datatype.format(value, locale);
                }

                //noinspection unchecked
                return datatype.format(value);
            }

            @Override
            public Class getModelType() {
                return Object.class;
            }

            @Override
            public Class getPresentationType() {
                return String.class;
            }
        });

        attachListener(component);
    }

    @Override
    public MetaClass getMetaClass() {
        final Datasource ds = getDatasource();
        if (ds != null && metaProperty.getRange().isClass()) {
            return metaProperty.getRange().asClass();
        } else {
            return metaClass;
        }
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        final Datasource ds = getDatasource();
        if (ds != null) {
            throw new IllegalStateException("Datasource is not null");
        }
        this.metaClass = metaClass;
    }

    @Override
    public ListComponent getOwner() {
        return owner;
    }

    @Override
    public void setOwner(ListComponent owner) {
        this.owner = owner;
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            if (datasource == null && metaClass == null) {
                throw new IllegalStateException("Datasource or metaclass must be set for field");
            }

            MetaClass fieldMetaClass = getMetaClass();
            if (fieldMetaClass != null) {
                Class fieldClass = fieldMetaClass.getJavaClass();
                Class<?> valueClass = value.getClass();
                //noinspection unchecked
                if (!fieldClass.isAssignableFrom(valueClass)) {
                    throw new IllegalArgumentException(
                            String.format("Could not set value with class %s to field with class %s",
                                    fieldClass.getCanonicalName(),
                                    valueClass.getCanonicalName())
                    );
                }
            }
        }

        super.setValue(value);
    }

    @Override
    public String getScreen() {
        return screen;
    }

    @Override
    public void setScreen(String screen) {
        this.screen = screen;
    }

    @Override
    public EntityLinkClickHandler getCustomClickHandler() {
        return clickHandler;
    }

    @Override
    public void setCustomClickHandler(EntityLinkClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public WindowManager.OpenType getScreenOpenType() {
        return screenOpenType;
    }

    @Override
    public void setScreenOpenType(WindowManager.OpenType screenOpenType) {
        this.screenOpenType = screenOpenType;
    }

    @Override
    public DialogParams getScreenDialogParams() {
        return screenDialogParams;
    }

    @Override
    public void setScreenDialogParams(DialogParams screenDialogParams) {
        this.screenDialogParams = screenDialogParams;
    }

    @Override
    public Map<String, Object> getScreenParams() {
        return screenParams;
    }

    @Override
    public void setScreenParams(Map<String, Object> screenParams) {
        this.screenParams = screenParams;
    }

    @Nullable
    @Override
    public ScreenCloseListener getScreenCloseListener() {
        return screenCloseListener;
    }

    @Override
    public void setScreenCloseListener(@Nullable ScreenCloseListener closeListener) {
        this.screenCloseListener = closeListener;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        //noinspection unchecked
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        this.metaPropertyPath = metaClass.getPropertyPath(property);
        if (metaPropertyPath == null) {
            throw new RuntimeException(String.format("Property '%s' not found in class %s", property, metaClass));
        }

        this.metaProperty = metaPropertyPath.getMetaProperty();
        if (metaProperty.getRange().isClass()) {
            this.metaClass = metaProperty.getRange().asClass();
        }

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        final Property itemProperty = wrapper.getItemProperty(metaPropertyPath);

        component.setPropertyDataSource(itemProperty);

        //noinspection unchecked
        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        Object newValue = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        setValue(newValue);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (property.equals(metaPropertyPath.toString())) {
                            setValue(value);
                        }
                    }
                }
        );

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
                setValue(newValue);
            }
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
        }
    }

    protected void openEntityEditor() {
        Object value = getValue();

        Entity entity;
        if (value instanceof Entity) {
            entity = (Entity) value;
        } else {
            entity = datasource.getItem();
        }

        if (entity == null) {
            return;
        }

        WindowManager wm;
        Window window = ComponentsHelper.getWindow(this);
        if (window == null) {
            throw new IllegalStateException("Please specify Frame for EntityLinkField");
        } else {
            wm = window.getWindowManager();
        }

        if (screenOpenType == WindowManager.OpenType.DIALOG && screenDialogParams != null) {
            wm.getDialogParams().copyFrom(screenDialogParams);
        }

        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
            Messages messages = AppBeans.get(Messages.NAME);
            wm.showNotification(
                    messages.getMessage(EntityLinkField.class, "OpenAction.objectIsDeleted"),
                    IFrame.NotificationType.HUMANIZED);
            return;
        }

        DataSupplier dataSupplier = window.getDsContext().getDataSupplier();
        entity = dataSupplier.reload(entity, View.MINIMAL);

        if (entity != null) {
            String windowAlias = screen;
            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
            if (windowAlias == null) {
                windowAlias = windowConfig.getEditorScreenId(entity.getMetaClass());
            }

            final Window.Editor editor = wm.openEditor(
                    windowConfig.getWindowInfo(windowAlias),
                    entity,
                    screenOpenType,
                    screenParams != null ? screenParams : Collections.<String, Object>emptyMap()
            );
            editor.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    // move focus to component
                    component.focus();

                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        Entity item = editor.getItem();
                        afterCommitOpenedEntity(item);
                    }

                    if (screenCloseListener != null) {
                        screenCloseListener.windowClosed(editor, actionId);
                    }
                }
            });
        }
    }

    protected void afterCommitOpenedEntity(Entity item) {
        if (metaProperty.getRange().isClass()) {
            if (getDatasource() != null) {
                boolean modified = getDatasource().isModified();
                setValue(null);
                setValue(item);
                ((DatasourceImplementation) getDatasource()).setModified(modified);
            } else {
                setValue(null);
                setValue(item);
            }
        } else if (owner != null && owner.getDatasource() != null) {
            //noinspection unchecked
            owner.getDatasource().updateItem(item);

            // focus owner
            owner.requestFocus();
        }
    }
}