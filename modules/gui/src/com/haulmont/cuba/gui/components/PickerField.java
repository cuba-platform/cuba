/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * PickerField component generic interface.<br/>
 *
 * @see LookupPickerField
 *
 * @version $Id$
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

    public interface FieldListener extends Serializable {
        public void actionPerformed(String text, Object prevValue);
    }

    /**
     * Enumerates standard picker action types. Can create a corresponding action instance.
     */
    public enum ActionType {

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

    public static abstract class StandardAction extends AbstractAction {

        private static final long serialVersionUID = 8103707056784950383L;

        protected PickerField pickerField;

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

    public static class LookupAction extends StandardAction {

        private static final long serialVersionUID = 8632079803045944261L;

        public static final String NAME = ActionType.LOOKUP.getId();

        protected String lookupScreen;
        protected WindowManager.OpenType lookupScreenOpenType = WindowManager.OpenType.THIS_TAB;
        protected Map<String, Object> lookupScreenParams;

        public LookupAction(PickerField pickerField) {
            super(NAME, pickerField);
            caption = "";
            icon = "pickerfield/img/normal/lookup-btn.png";
        }

        public String getLookupScreen() {
            return lookupScreen;
        }

        public void setLookupScreen(@Nullable String lookupScreen) {
            this.lookupScreen = lookupScreen;
        }

        public WindowManager.OpenType getLookupScreenOpenType() {
            return lookupScreenOpenType;
        }

        public void setLookupScreenOpenType(WindowManager.OpenType lookupScreenOpenType) {
            this.lookupScreenOpenType = lookupScreenOpenType;
        }

        public Map<String, Object> getLookupScreenParams() {
            return lookupScreenParams;
        }

        public void setLookupScreenParams(Map<String, Object> lookupScreenParams) {
            this.lookupScreenParams = lookupScreenParams;
        }

        @Override
        public void actionPerform(Component component) {
            if (pickerField.isEditable()) {
                String windowAlias = lookupScreen;
                if (windowAlias == null) {
                    final MetaClass metaClass = pickerField.getMetaClass();
                    if (metaClass == null)
                        throw new IllegalStateException("Please specify metaclass or property for PickerField");
                    windowAlias = metaClass.getName() + ".lookup";
                }
                pickerField.getFrame().openLookup(
                        windowAlias,
                        new Window.Lookup.Handler() {
                            @Override
                            public void handleLookup(Collection items) {
                                if (!items.isEmpty()) {
                                    final Object item = items.iterator().next();
                                    pickerField.setValue(item);
                                }
                            }
                        },
                        lookupScreenOpenType,
                        lookupScreenParams != null ? lookupScreenParams : Collections.<String, Object>emptyMap()
                );
            }
        }
    }

    public static class ClearAction extends StandardAction {

        public static final String NAME = ActionType.CLEAR.getId();

        public ClearAction(PickerField pickerField) {
            super(NAME, pickerField);
            caption = "";
            icon = "pickerfield/img/normal/clear-btn.png";
        }

        @Override
        public void actionPerform(Component component) {
            if (pickerField.isEditable()) {
                pickerField.setValue(null);
            }
        }
    }

    public static class OpenAction extends StandardAction {

        public static final String NAME = ActionType.OPEN.getId();

        protected String editScreen;
        protected WindowManager.OpenType editScreenOpenType = WindowManager.OpenType.THIS_TAB;
        protected Map<String, Object> editScreenParams;

        public OpenAction(PickerField pickerField) {
            super(NAME, pickerField);
            caption = "";
            icon = "pickerfield/img/normal/open-btn.png";
        }

        public String getEditScreen() {
            return editScreen;
        }

        public void setEditScreen(String editScreen) {
            this.editScreen = editScreen;
        }

        public WindowManager.OpenType getEditScreenOpenType() {
            return editScreenOpenType;
        }

        public void setEditScreenOpenType(WindowManager.OpenType editScreenOpenType) {
            this.editScreenOpenType = editScreenOpenType;
        }

        public Map<String, Object> getEditScreenParams() {
            return editScreenParams;
        }

        public void setEditScreenParams(Map<String, Object> editScreenParams) {
            this.editScreenParams = editScreenParams;
        }

        @Override
        public void actionPerform(Component component) {
            Entity entity = getEntity();
            if (entity == null)
                return;

            if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
                pickerField.getFrame().showNotification(
                        MessageProvider.getMessage(ActionsFieldHelper.class, "ActionsFieldHelper.openMsg"),
                        IFrame.NotificationType.HUMANIZED);
                return;
            }

            LoadContext ctx = new LoadContext(entity.getClass());
            ctx.setId(entity.getId());
            ctx.setView(View.MINIMAL);
            entity = ServiceLocator.getDataService().load(ctx);

            if (entity != null) {
                String windowAlias = editScreen;
                if (windowAlias == null)
                    windowAlias = entity.getMetaClass().getName() + ".edit";
                final Window.Editor editor = pickerField.getFrame().openEditor(
                        windowAlias,
                        entity,
                        editScreenOpenType,
                        editScreenParams != null ? editScreenParams : Collections.<String, Object>emptyMap()
                );
                editor.addListener(new Window.CloseListener() {

                    @Override
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                            Entity item = editor.getItem();
                            afterCommitOpenedEntity(item);
                        }
                    }
                });
            }
        }

        protected Entity getEntity() {
            Object value = pickerField.getValue();

            if (value instanceof Entity) {
                return (Entity) value;
            }

            if (pickerField.getDatasource() != null) {
                Entity item = pickerField.getDatasource().getItem();
                if (item != null) {
                    Object dsValue = item.getValue(pickerField.getMetaProperty().getName());
                    if (dsValue instanceof Entity)
                        return (Entity) dsValue;
                }
            }

            return null;
        }

        protected void afterCommitOpenedEntity(Entity item) {
            if (!(pickerField instanceof LookupPickerField))
                return;

            LookupPickerField lookupPickerField = ((LookupPickerField) pickerField);

            CollectionDatasource optionsDatasource = lookupPickerField.getOptionsDatasource();
            if (optionsDatasource != null && optionsDatasource.containsItem(item.getId())) {
                optionsDatasource.updateItem(item);
            }

            boolean modified = lookupPickerField.getDatasource().isModified();
            lookupPickerField.setValue(null);
            lookupPickerField.setValue(item);
            ((DatasourceImplementation) lookupPickerField.getDatasource()).setModified(modified);
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
