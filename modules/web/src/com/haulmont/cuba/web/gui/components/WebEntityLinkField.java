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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenMode;
import com.haulmont.cuba.gui.components.EntityLinkField;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.web.widgets.CubaButtonField;
import com.vaadin.v7.data.util.converter.Converter;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class WebEntityLinkField<V> extends WebAbstractField<CubaButtonField, V> implements EntityLinkField<V> {

    protected static final String EMPTY_VALUE_STYLENAME = "empty-value";

    protected EntityLinkClickHandler clickHandler;

    protected String screen;
    protected WindowManager.OpenType screenOpenType = WindowManager.OpenType.THIS_TAB;
    protected DialogParams screenDialogParams;
    protected ScreenCloseListener screenCloseListener;
    protected Map<String, Object> screenParams;

    protected MetaClass metaClass;
    protected ListComponent owner;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;

    public WebEntityLinkField() {
        component = new CubaButtonField();
        component.addClickListener(event -> {
            if (clickHandler != null) {
                clickHandler.onClick(WebEntityLinkField.this);
            } else {
                openEntityEditor();
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
                    return datatype.format(value, locale);
                }

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
        if (ds != null && getMetaProperty().getRange().isClass()) {
            return getMetaProperty().getRange().asClass();
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
            if (getDatasource() == null && metaClass == null) {
                throw new IllegalStateException("Datasource or metaclass must be set for field");
            }
            
            component.removeStyleName(EMPTY_VALUE_STYLENAME);

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
        } else {
            component.addStyleName("empty-value");
        }

        component.setValue(value);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(EMPTY_VALUE_STYLENAME, ""));
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

    @Deprecated
    @Override
    public DialogParams getScreenDialogParams() {
        return screenDialogParams;
    }

    @Deprecated
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

    protected void openEntityEditor() {
        Object value = getValue();

        Entity entity;
        if (value instanceof Entity) {
            entity = (Entity) value;
        } else {
            entity = getDatasource().getItem();
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

        if (screenOpenType.getOpenMode() == OpenMode.DIALOG && screenDialogParams != null) {
            wm.getDialogParams().copyFrom(screenDialogParams);
        }

        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
            Messages messages = AppBeans.get(Messages.NAME);
            wm.showNotification(
                    messages.getMainMessage("OpenAction.objectIsDeleted"),
                    Frame.NotificationType.HUMANIZED);
            return;
        }

        DataSupplier dataSupplier = window.getDsContext().getDataSupplier();
        entity = dataSupplier.reload(entity, View.MINIMAL);

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
        editor.addCloseListener(actionId -> {
            // move focus to component
            component.focus();

            if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                Entity item = editor.getItem();
                afterCommitOpenedEntity(item);
            }

            if (screenCloseListener != null) {
                screenCloseListener.windowClosed(editor, actionId);
            }
        });
    }

    protected void afterCommitOpenedEntity(Entity item) {
        if (getMetaProperty().getRange().isClass()) {
            if (getDatasource() != null) {
                boolean ownerDsModified = false;
                boolean nonModifiedInTable = false;
                if (owner != null && owner.getDatasource() != null) {
                    DatasourceImplementation ownerDs = ((DatasourceImplementation) owner.getDatasource());
                    nonModifiedInTable = !ownerDs.getItemsToUpdate().contains(getDatasource().getItem());

                    ownerDsModified = ownerDs.isModified();
                }

                boolean modified = getDatasource().isModified();
                setValue(null);
                setValue(item);
                ((DatasourceImplementation) getDatasource()).setModified(modified);

                // restore modified for owner datasource
                // remove from items to update if it was not modified before setValue
                if (owner != null && owner.getDatasource() != null) {
                    DatasourceImplementation ownerDs = ((DatasourceImplementation) owner.getDatasource());
                    if (nonModifiedInTable) {
                        ownerDs.getItemsToUpdate().remove(getDatasource().getItem());
                    }
                    ownerDs.setModified(ownerDsModified);
                }
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

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }
}