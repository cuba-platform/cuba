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
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenMode;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Generic UI component to select and display an entity instance. Consists of the text field and the set of buttons
 * defined by actions.
 *
 * @see LookupAction
 * @see OpenAction
 * @see ClearAction
 *
 * @see LookupPickerField
 *
 */
public interface PickerField extends Field, Component.ActionsHolder {

    String NAME = "pickerField";

    CaptionMode getCaptionMode();

    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();

    void setCaptionProperty(String captionProperty);

    MetaClass getMetaClass();

    void setMetaClass(MetaClass metaClass);

    LookupAction addLookupAction();

    ClearAction addClearAction();

    OpenAction addOpenAction();

    void addFieldListener(FieldListener listener);

    void setFieldEditable(boolean editable);

    interface FieldListener {
        void actionPerformed(String text, Object prevValue);
    }

    /**
     * Enumerates standard picker action types. Can create a corresponding action instance.
     */
    enum ActionType {

        LOOKUP("lookup") {
            @Override
            public Action createAction(PickerField pickerField) {
                return new LookupAction(pickerField);
            }
        },

        CLEAR("clear") {
            @Override
            public Action createAction(PickerField pickerField) {
                return new ClearAction(pickerField);
            }
        },

        OPEN("open") {
            @Override
            public Action createAction(PickerField pickerField) {
                return new OpenAction(pickerField);
            }
        };

        private String id;

        ActionType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public abstract Action createAction(PickerField pickerField);
    }

    abstract class StandardAction extends BaseAction {

        protected PickerField pickerField;

        protected ClientConfig clientConfig = AppBeans.<Configuration>get(Configuration.NAME).getConfig(ClientConfig.class);

        public StandardAction(String id, PickerField pickerField) {
            super(id);
            this.pickerField = pickerField;
        }

        public void setEditable(boolean editable) {
            ActionOwner owner = getOwner();
            if (owner != null && owner instanceof Component) {
                ((Component) owner).setVisible(editable);
            }
        }
    }

    /**
     * Action to select an entity instance through the entity lookup screen.
     */
    class LookupAction extends StandardAction {

        public static final String NAME = ActionType.LOOKUP.getId();

        protected String lookupScreen;
        protected OpenType lookupScreenOpenType = OpenType.THIS_TAB;
        protected DialogParams lookupScreenDialogParams;
        protected Map<String, Object> lookupScreenParams;

        protected AfterLookupCloseHandler afterLookupCloseHandler;
        protected AfterLookupSelectionHandler afterLookupSelectionHandler;

        protected WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

        public LookupAction(PickerField pickerField) {
            super(NAME, pickerField);
            caption = "";
            icon = "components/pickerfield/images/lookup-btn.png";
            setShortcut(clientConfig.getPickerLookupShortcut());
        }

        public void setAfterLookupCloseHandler(AfterLookupCloseHandler afterLookupCloseHandler) {
            this.afterLookupCloseHandler = afterLookupCloseHandler;
        }

        public void setAfterLookupSelectionHandler(AfterLookupSelectionHandler afterLookupSelectionHandler) {
            this.afterLookupSelectionHandler = afterLookupSelectionHandler;
        }

        public String getLookupScreen() {
            return lookupScreen;
        }

        /**
         * Set the lookup screen ID explicitly. By default a lookup screen ID is inferred from the entity metaclass
         * name by adding suffix <code>.lookup</code> to it.
         *
         * @param lookupScreen  lookup screen ID, e.g. <code>sec$User.lookup</code>
         */
        public void setLookupScreen(@Nullable String lookupScreen) {
            this.lookupScreen = lookupScreen;
        }

        public OpenType getLookupScreenOpenType() {
            return lookupScreenOpenType;
        }

        /**
         * How to open the lookup screen. By default it is opened in {@link OpenType#THIS_TAB} mode.
         *
         * @param lookupScreenOpenType  open type
         */
        public void setLookupScreenOpenType(OpenType lookupScreenOpenType) {
            this.lookupScreenOpenType = lookupScreenOpenType;
        }

        @Deprecated
        @Nullable
        public DialogParams getLookupScreenDialogParams() {
            return lookupScreenDialogParams;
        }

        /**
         * Set lookup screen geometry when opening it in {@link OpenType#DIALOG} mode.
         * Doesn't affect other modes.
         *
         * @deprecated Use {@link #setLookupScreenOpenType(OpenType)}
         */
        @Deprecated
        public void setLookupScreenDialogParams(DialogParams lookupScreenDialogParams) {
            this.lookupScreenDialogParams = lookupScreenDialogParams;
        }

        @Nullable
        public Map<String, Object> getLookupScreenParams() {
            return lookupScreenParams;
        }

        /**
         * Parameters to pass to the lookup screen. By default the empty map is passed.
         *
         * @param lookupScreenParams    map of parameters
         */
        public void setLookupScreenParams(Map<String, Object> lookupScreenParams) {
            this.lookupScreenParams = lookupScreenParams;
        }

        @Override
        public void actionPerform(Component component) {
            if (pickerField.isEditable()) {
                String windowAlias = getLookupScreen();
                if (windowAlias == null) {
                    final MetaClass metaClass = pickerField.getMetaClass();
                    if (metaClass == null) {
                        throw new DevelopmentException("Neither metaClass nor datasource/property is specified for the PickerField",
                                "action ID", getId());
                    }
                    windowAlias = windowConfig.getAvailableLookupScreenId(metaClass);
                }

                WindowManager wm;
                Window window = ComponentsHelper.getWindow(pickerField);
                if (window == null) {
                    LogFactory.getLog(PickerField.class).warn("Please specify Frame for PickerField");

                    wm = AppBeans.get(WindowManagerProvider.class).get();
                } else {
                    wm = window.getWindowManager();
                }

                OpenType openType = getLookupScreenOpenType();
                DialogParams dialogParams = getLookupScreenDialogParams();
                Map<String, Object> screenParams = getLookupScreenParams();

                if (openType.getOpenMode() == OpenMode.DIALOG && dialogParams != null) {
                    wm.getDialogParams().copyFrom(dialogParams);
                }

                Window lookupWindow = wm.openLookup(
                        windowConfig.getWindowInfo(windowAlias),
                        new Window.Lookup.Handler() {
                            @Override
                            public void handleLookup(Collection items) {
                                if (!items.isEmpty()) {
                                    final Entity item = (Entity) items.iterator().next();
                                    Object oldValue = pickerField.getValue();

                                    if (pickerField instanceof LookupField) {
                                        LookupField lookupPickerField = ((LookupField) pickerField);

                                        CollectionDatasource optionsDatasource = lookupPickerField.getOptionsDatasource();
                                        if (optionsDatasource != null && optionsDatasource.containsItem(item.getId())) {
                                            optionsDatasource.updateItem(item);
                                        }
                                    }

                                    Entity newValue = transformValueFromLookupWindow(item);

                                    // we need to reset value and assign it again if entity has same id
                                    if (oldValue != item && ObjectUtils.equals(oldValue, newValue)) {
                                        pickerField.setValue(null);
                                    }

                                    pickerField.setValue(newValue);

                                    afterSelect(items);
                                    if (afterLookupSelectionHandler != null) {
                                        afterLookupSelectionHandler.onSelect(items);
                                    }
                                }
                            }
                        },
                        openType,
                        screenParams != null ? screenParams : Collections.emptyMap()
                );
                lookupWindow.addCloseListener(actionId -> {
                    // move focus to owner
                    pickerField.requestFocus();

                    afterCloseLookup(actionId);
                    if (afterLookupCloseHandler != null) {
                        afterLookupCloseHandler.onClose(lookupWindow, actionId);
                    }
                });
            }
        }

        /**
         * Hook to be implemented in subclasses. Called by the action for new value selected from Lookup window.
         * Can be used for reloading of entity with different view or to replace value with another value.
         *
         * @param valueFromLookupWindow value selected in Lookup window.
         * @return value that will be set to PickerField
         */
        public Entity transformValueFromLookupWindow(Entity valueFromLookupWindow) {
            return valueFromLookupWindow;
        }

        /**
         * Hook to be implemented in subclasses. Called by the action when the user is selected some items in the
         * lookup screen and the PickerField value is set.
         *
         * @param items collection of entity instances selected by user, never null
         */
        public void afterSelect(Collection items) {
        }

        /**
         * Hook to be implemented in subclasses. Called by the action when the lookup screen is closed.
         *
         * @param actionId  ID of action that closed the screen. The following values are possible:
         *                  <ul>
         *                  <li/>select - user selected some items
         *                  <li/>cancel - user pressed Cancel button
         *                  <li/>close - user closed the lookup screen by other means
         *                  </ul>
         */
        public void afterCloseLookup(String actionId) {
        }
    }

    /**
     * Action to clear the PickerField content.
     */
    class ClearAction extends StandardAction {

        public static final String NAME = ActionType.CLEAR.getId();

        public ClearAction(PickerField pickerField) {
            super(NAME, pickerField);
            caption = "";
            icon = "components/pickerfield/images/clear-btn.png";
            setShortcut(clientConfig.getPickerClearShortcut());
        }

        @Override
        public void actionPerform(Component component) {
            if (pickerField.isEditable()) {
                pickerField.setValue(null);
            }
        }
    }

    interface AfterLookupCloseHandler {
        void onClose(Window window, String actionId);
    }

    interface AfterLookupSelectionHandler {
        void onSelect(Collection items);
    }

    /**
     * Action to open an edit screen for entity instance which is currently set in the PickerField.
     */
    class OpenAction extends StandardAction {

        public static final String NAME = ActionType.OPEN.getId();

        protected String editScreen;
        protected OpenType editScreenOpenType = OpenType.THIS_TAB;
        protected DialogParams editScreenDialogParams;
        protected Map<String, Object> editScreenParams;

        protected WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

        public OpenAction(PickerField pickerField) {
            super(NAME, pickerField);
            caption = "";
            icon = "components/pickerfield/images/open-btn.png";
            setShortcut(clientConfig.getPickerOpenShortcut());
        }

        public String getEditScreen() {
            return editScreen;
        }

        /**
         * Set the edit screen ID explicitly. By default an edit screen ID is inferred from the entity metaclass
         * name by adding suffix <code>.edit</code> to it.
         *
         * @param editScreen  edit screen ID, e.g. <code>sec$User.edit</code>
         */
        public void setEditScreen(String editScreen) {
            this.editScreen = editScreen;
        }

        public OpenType getEditScreenOpenType() {
            return editScreenOpenType;
        }

        /**
         * How to open the edit screen. By default it is opened in {@link OpenType#THIS_TAB} mode.
         *
         * @param editScreenOpenType  open type
         */
        public void setEditScreenOpenType(OpenType editScreenOpenType) {
            this.editScreenOpenType = editScreenOpenType;
        }

        @Deprecated
        @Nullable
        public DialogParams getEditScreenDialogParams() {
            return editScreenDialogParams;
        }

        /**
         * Set edit screen geometry when opening it in {@link OpenType#DIALOG} mode.
         * Doesn't affect other modes.
         *
         * @deprecated Use {@link #setEditScreenOpenType(OpenType)}
         */
        @Deprecated
        public void setEditScreenDialogParams(DialogParams editScreenDialogParams) {
            this.editScreenDialogParams = editScreenDialogParams;
        }

        @Nullable
        public Map<String, Object> getEditScreenParams() {
            return editScreenParams;
        }

        /**
         * Parameters to pass to the edit screen. By default the empty map is passed.
         *
         * @param editScreenParams    map of parameters
         */
        public void setEditScreenParams(Map<String, Object> editScreenParams) {
            this.editScreenParams = editScreenParams;
        }

        @Override
        public void actionPerform(Component component) {
            Entity entity = getEntity();
            if (entity == null)
                return;

            WindowManager wm;
            Window window = ComponentsHelper.getWindow(pickerField);
            if (window == null) {
                throw new IllegalStateException("Please specify Frame for EntityLinkField");
            } else {
                wm = window.getWindowManager();
            }

            OpenType openType = getEditScreenOpenType();
            DialogParams dialogParams = getEditScreenDialogParams();
            Map<String, Object> screenParams = getEditScreenParams();

            if (openType.getOpenMode() == OpenMode.DIALOG && dialogParams != null) {
                wm.getDialogParams().copyFrom(dialogParams);
            }

            if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
                wm.showNotification(
                        messages.getMessage(getClass(), "OpenAction.objectIsDeleted"),
                        Frame.NotificationType.HUMANIZED);
                return;
            }

            DataSupplier dataSupplier = window.getDsContext().getDataSupplier();
            entity = dataSupplier.reload(entity, View.MINIMAL);

            String windowAlias = getEditScreen();
            if (windowAlias == null) {
                windowAlias = windowConfig.getEditorScreenId(entity.getMetaClass());
            }

            Window.Editor editor = wm.openEditor(
                    windowConfig.getWindowInfo(windowAlias),
                    entity,
                    openType,
                    screenParams != null ? screenParams : Collections.<String, Object>emptyMap()
            );
            editor.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    Entity item = editor.getItem();
                    afterCommitOpenedEntity(item);
                }

                // move focus to owner
                pickerField.requestFocus();

                afterWindowClosed(editor);
            });
        }

        protected Entity getEntity() {
            Object value = pickerField.getValue();

            if (value instanceof Entity) {
                return (Entity) value;
            }

            if (pickerField.getDatasource() != null) {
                Entity item = pickerField.getDatasource().getItem();
                if (item != null) {
                    Object dsValue = item.getValue(pickerField.getMetaPropertyPath().getMetaProperty().getName());
                    if (dsValue instanceof Entity)
                        return (Entity) dsValue;
                }
            }

            return null;
        }

        protected void afterCommitOpenedEntity(Entity item) {
            if (pickerField instanceof LookupField) {
                LookupField lookupPickerField = ((LookupField) pickerField);

                CollectionDatasource optionsDatasource = lookupPickerField.getOptionsDatasource();
                if (optionsDatasource != null && optionsDatasource.containsItem(item.getId())) {
                    optionsDatasource.updateItem(item);
                }
            }

            if (pickerField.getDatasource() != null) {
                boolean modified = pickerField.getDatasource().isModified();
                pickerField.setValue(null);
                pickerField.setValue(item);
                ((DatasourceImplementation) pickerField.getDatasource()).setModified(modified);
            } else {
                pickerField.setValue(null);
                pickerField.setValue(item);
            }
        }

        /**
         * Hook invoked after the editor was closed
         * @param window    the editor window
         */
        protected void afterWindowClosed(Window window) {
        }

        @Override
        public void setEditable(boolean editable) {
            setIcon(getEditableIcon(icon, editable));
        }

        public static final String READONLY = "-readonly";

        protected String getEditableIcon(String icon, boolean editable) {
            if (icon == null)
                return null;

            int dot = icon.lastIndexOf('.');
            if (dot == -1)
                return icon;

            StringBuilder sb = new StringBuilder(icon);
            int len = READONLY.length();
            if (StringUtils.substring(icon, dot - len, dot).equals(READONLY)) {
                if (editable)
                    sb.delete(dot - len, dot);
            } else {
                if (!editable)
                    sb.insert(dot, READONLY);
            }

            return sb.toString();
        }
    }
}