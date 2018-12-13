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

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.web.widgets.CubaButtonField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.WindowManager.OpenType;

public class WebEntityLinkField<V> extends WebV8AbstractField<CubaButtonField<V>, V, V>
        implements EntityLinkField<V>, InitializingBean {

    protected static final String EMPTY_VALUE_STYLENAME = "empty-value";

    protected EntityLinkClickHandler clickHandler;

    protected String screen;
    protected OpenMode screenOpenMode = OpenMode.THIS_TAB;
    protected OpenType screenOpenType = OpenType.THIS_TAB;
    protected ScreenCloseListener screenCloseListener;
    protected Map<String, Object> screenParams;

    protected MetaClass metaClass;
    protected ListComponent owner;

    protected Subscription closeListenerSubscription;

    /* Beans */
    protected MetadataTools metadataTools;
    protected ScreenBuilders screenBuilders;

    public WebEntityLinkField() {
        component = createComponent();
        attachValueChangeListener(component);
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Inject
    public void setSceenBuilders(ScreenBuilders screenBuilders) {
        this.screenBuilders = screenBuilders;
    }

    protected CubaButtonField<V> createComponent() {
        return new CubaButtonField<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initComponent();
    }

    protected void initComponent() {
        component.addClickListener(event -> {
            if (clickHandler != null) {
                clickHandler.onClick(WebEntityLinkField.this);
            } else {
                openEntityEditor();
            }
        });
        component.setCaptionFormatter((value, locale) -> {
            if (value == null) {
                return "";
            }

            if (value instanceof Instance) {
                return metadataTools.getInstanceName((Instance) value);
            }

            Datatype datatype = Datatypes.getNN(value.getClass());

            if (locale != null) {
                return datatype.format(value, locale);
            }

            return datatype.format(value);
        });
    }

    @Override
    public MetaClass getMetaClass() {
        MetaProperty metaProperty = getMetaPropertyForEditedValue();
        if (metaProperty != null && metaProperty.getRange().isClass()) {
            return metaProperty.getRange().asClass();
        }
        return metaClass;
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        ValueSource<V> valueSource = getValueSource();

        if (valueSource instanceof EntityValueSource) {
            throw new IllegalStateException("ValueSource is not null");
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
    public void setValue(V value) {
        super.setValue(value);

        if (value != null) {
            if (getValueSource() == null && metaClass == null) {
                throw new IllegalStateException("ValueSource or metaclass must be set for field");
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
    public OpenType getScreenOpenType() {
        return screenOpenType;
    }

    @Override
    public void setScreenOpenType(OpenType screenOpenType) {
        this.screenOpenType = screenOpenType;
        this.screenOpenMode = screenOpenType.getOpenMode();
    }

    @Override
    public OpenMode getOpenMode() {
        return screenOpenMode;
    }

    @Override
    public void setOpenMode(OpenMode openMode) {
        this.screenOpenMode = openMode;
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

        if (closeListenerSubscription != null) {
            closeListenerSubscription.remove();
        }

        if (screenCloseListener != null) {
            closeListenerSubscription = addEditorCloseListener(event -> {
                if (event.getEditorScreen() instanceof AbstractEditor) {
                    screenCloseListener.windowClosed((Window) event.getEditorScreen(), event.getActionId());
                } else {
                    screenCloseListener.windowClosed(null, event.getActionId());
                }
            });
        }
    }

    @Override
    public Subscription addEditorCloseListener(Consumer<EditorCloseEvent> editorCloseListener) {
        return getEventHub().subscribe(EditorCloseEvent.class, editorCloseListener);
    }

    protected void openEntityEditor() {
        V value = getValue();

        Entity entity = null;
        if (value instanceof Entity) {
            entity = (Entity) value;
        } else if (getValueSource() instanceof EntityValueSource) {
            entity = ((EntityValueSource) getValueSource()).getItem();
        }

        if (entity == null) {
            return;
        }

        Window window = ComponentsHelper.getWindow(this);
        if (window == null) {
            throw new IllegalStateException("Please specify Frame for EntityLinkField");
        }

        ScreenContext context = ComponentsHelper.getScreenContext(this);
        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
            Messages messages = AppBeans.get(Messages.NAME);
            context.getNotifications().create(Notifications.NotificationType.HUMANIZED)
                    .withCaption(messages.getMainMessage("OpenAction.objectIsDeleted"))
                    .show();
            return;
        }

        if (window.getFrameOwner() instanceof LegacyFrame) {
            LegacyFrame frameOwner = (LegacyFrame) window.getFrameOwner();

            DataSupplier dataSupplier = frameOwner.getDsContext().getDataSupplier();
            entity = dataSupplier.reload(entity, View.MINIMAL);
        } else {
            DataManager dataManager = beanLocator.get(DataManager.NAME);
            entity = dataManager.reload(entity, View.MINIMAL);
        }

        String windowAlias = screen;
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        if (windowAlias == null) {
            windowAlias = windowConfig.getEditorScreenId(entity.getMetaClass());
        }

        Screen screenEditor = screenBuilders.editor(entity.getMetaClass().getJavaClass(), window.getFrameOwner())
                .withScreenId(windowAlias)
                .editEntity(entity)
                .withOpenMode(screenOpenMode)
                .withOptions(new MapScreenOptions(screenParams != null ? screenParams : new HashMap<>()))
                .build();

        screenEditor.addAfterCloseListener(event -> {
            // move focus to component
            component.focus();

            String closeActionId = null;
            CloseAction closeAction = event.getCloseAction();
            if (closeAction instanceof StandardCloseAction) {
                closeActionId = ((StandardCloseAction) closeAction).getActionId();
            }

            Screen screenSource = null;
            if (StringUtils.isNotEmpty(closeActionId)
                    && Window.COMMIT_ACTION_ID.equals(closeActionId)) {
                Entity item = null;
                screenSource = event.getSource();
                if (screenSource instanceof EditorScreen) {
                    item = ((EditorScreen) screenSource).getEditedEntity();
                }

                if (item != null) {
                    afterCommitOpenedEntity(item);
                }
            }

            fireEditorCloseEvent(screenSource == null ?
                    null : (EditorScreen) screenSource, closeActionId);
        });
        screenEditor.show();
    }

    protected void fireEditorCloseEvent(EditorScreen editorScreen, String closeActionId) {
        publish(EditorCloseEvent.class,
                new EditorCloseEvent<>(this, editorScreen, closeActionId));
    }

    protected void afterCommitOpenedEntity(Entity item) {
        MetaProperty metaProperty = getMetaPropertyForEditedValue();
        if (metaProperty != null && metaProperty.getRange().isClass()) {
            if (getValueSource() != null) {
                boolean ownerDsModified = false;
                boolean nonModifiedInTable = false;

                DatasourceImplementation ownerDs = null;
                CollectionContainer ownerCollectionCont = null;

                if (getCollectionDatasourceFromOwner() != null) {
                    ownerDs = ((DatasourceImplementation) getCollectionDatasourceFromOwner());
                    nonModifiedInTable = !ownerDs.getItemsToUpdate().contains(
                            ((EntityValueSource) getValueSource()).getItem());
                    ownerDsModified = ownerDs.isModified();
                } else if (getCollectionContainerFromOwner() != null) {
                    ownerCollectionCont = ((ContainerDataUnit) owner.getItems()).getContainer();
                    ownerCollectionCont.mute();
                }

                //noinspection unchecked
                setValueSilently((V) item);

                // restore modified for owner datasource
                // remove from items to update if it was not modified before setValue
                if (ownerDs != null) {
                    if (nonModifiedInTable) {
                        ownerDs.getItemsToUpdate().remove(getDatasource().getItem());
                    }
                    ownerDs.setModified(ownerDsModified);
                } else if (ownerCollectionCont != null) {
                    ownerCollectionCont.unmute();
                }
            } else {
                //noinspection unchecked
                setValue((V) item);
            }
            // if we edit property with non Entity type and set ListComponent owner
        } else if (owner != null) {
            if (getCollectionDatasourceFromOwner() != null) {
                //noinspection unchecked
                getCollectionDatasourceFromOwner().updateItem(item);
            } else if (getCollectionContainerFromOwner() != null) {
                //do not listen changes in collection
                getCollectionContainerFromOwner().mute();

                //noinspection unchecked
                getCollectionContainerFromOwner().replaceItem(item);
                setValueSilently(item.getValueEx(getMetaPropertyPath()));

                //listen changes
                getCollectionContainerFromOwner().unmute();
            }

            if (owner instanceof Focusable) {
                // focus owner
                ((Focusable) owner).focus();
            }
            // if we edit property with non Entity type
        } else {
            //noinspection unchecked
            setValueSilently((V) item);
        }
    }

    protected CollectionContainer getCollectionContainerFromOwner() {
        if (owner != null && owner.getItems() != null) {
            if (owner.getItems() instanceof ContainerDataUnit) {
                return ((ContainerDataUnit) owner.getItems()).getContainer();
            }
        }
        return null;
    }

    protected CollectionDatasource getCollectionDatasourceFromOwner() {
        if (owner != null && owner.getItems() != null) {
            return owner.getDatasource();
        }
        return null;
    }

    protected MetaProperty getMetaPropertyForEditedValue() {
        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaPropertyPath metaPropertyPath = ((EntityValueSource) valueSource).getMetaPropertyPath();
            return metaPropertyPath.getMetaProperty();
        }
        return null;
    }

    /**
     * Sets value to the component without triggering change listeners for ContainerValueSource and without changing
     * a modify state of Datasource.
     *
     * @param item value
     */
    protected void setValueSilently(V item) {
        boolean modified = false;
        if (getDatasource() != null) {
            modified = getDatasource().isModified();
        } else {
            ((ContainerValueSource) getValueSource()).getContainer().mute();
        }

        setValue(item);

        if (getDatasource() != null) {
            ((DatasourceImplementation) getDatasource()).setModified(modified);
        } else {
            ((ContainerValueSource) getValueSource()).getContainer().unmute();
        }
    }

    @Override
    public void focus() {
        component.focus();
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