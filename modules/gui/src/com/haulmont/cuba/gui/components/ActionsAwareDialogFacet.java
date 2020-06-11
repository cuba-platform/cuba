package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioElement;
import com.haulmont.cuba.gui.meta.StudioElementsGroup;
import com.haulmont.cuba.gui.meta.StudioProperty;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Base interface for actions aware dialog facets.
 *
 * @see OptionDialogFacet
 * @see InputDialogFacet
 *
 * @param <T> dialog facet type
 */
public interface ActionsAwareDialogFacet<T> {

    /**
     * Sets dialog actions.
     *
     * @param actions actions
     */
    @StudioElementsGroup(xmlElement = "actions", icon = "icon/actions.svg",
            documentationURL = "https://doc.cuba-platform.com/manual-%VERSION%/gui_dialogs.html#gui_option_dialog_actions")
    void setActions(Collection<DialogAction<T>> actions);

    /**
     * @return dialog actions
     */
    Collection<DialogAction<T>> getActions();

    /**
     * The event that is fired when {@link DialogAction#actionHandler} is triggered.
     */
    class DialogActionPerformedEvent<T> {

        protected T dialog;
        protected DialogAction dialogAction;

        public DialogActionPerformedEvent(T dialog, DialogAction dialogAction) {
            this.dialog = dialog;
            this.dialogAction = dialogAction;
        }

        public T getDialog() {
            return dialog;
        }

        public DialogAction getDialogAction() {
            return dialogAction;
        }
    }

    /**
     * Immutable POJO that stores dialog action settings.
     */
    @StudioElement(xmlElement = "action", icon = "icon/action.svg",
            documentationURL = "https://doc.cuba-platform.com/manual-%VERSION%/gui_OptionDialogFacet.html#gui_OptionDialogFacet_actions")
    class DialogAction<T> {

        protected final String id;
        protected final String caption;
        protected final String description;
        protected final String icon;
        protected final boolean primary;

        protected Consumer<DialogActionPerformedEvent<T>> actionHandler;

        public DialogAction(String id, String caption, String description, String icon, boolean primary) {
            this.id = id;
            this.caption = caption;
            this.description = description;
            this.icon = icon;
            this.primary = primary;
        }

        @StudioProperty(type = PropertyType.COMPONENT_ID, required = true)
        public String getId() {
            return id;
        }

        @StudioProperty(type = PropertyType.LOCALIZED_STRING)
        public String getCaption() {
            return caption;
        }

        @StudioProperty(type = PropertyType.LOCALIZED_STRING)
        public String getDescription() {
            return description;
        }

        @StudioProperty(type = PropertyType.ICON_ID)
        public String getIcon() {
            return icon;
        }

        @StudioProperty(name = "primary", defaultValue = "false")
        public boolean isPrimary() {
            return primary;
        }

        public Consumer<DialogActionPerformedEvent<T>> getActionHandler() {
            return actionHandler;
        }

        /**
         * INTERNAL.
         * <p>
         * Intended to set handlers via {@code @Install} annotation.
         *
         * @param actionHandler action handler
         */
        public void setActionHandler(Consumer<DialogActionPerformedEvent<T>> actionHandler) {
            this.actionHandler = actionHandler;
        }
    }
}
