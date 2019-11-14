package com.haulmont.cuba.gui.components;

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

        public String getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

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
