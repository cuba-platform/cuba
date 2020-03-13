/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioFacet;
import com.haulmont.cuba.gui.meta.StudioProperty;

import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Client-side component that connects a {@link Button} and {@link TextField} or {@link TextArea} input.
 * Copies the text content to the clipboard on button click.
 */
@StudioFacet(
        xmlElement = "clipboardTrigger",
        caption = "ClipboardTrigger",
        description = "Copies the text content of the input to the clipboard on button click.",
        category = "Non-visual"
)
public interface ClipboardTrigger extends Facet {

    /**
     * Sets input field: {@link TextField} or {@link TextArea}.
     *
     * @param input input field
     */
    @StudioProperty(type = PropertyType.COMPONENT_REF)
    void setInput(TextInputField<?> input);
    /**
     * @return input field
     */
    TextInputField<?> getInput();

    /**
     * Sets target button component.
     *
     * @param button button
     */
    @StudioProperty(type = PropertyType.COMPONENT_REF)
    void setButton(Button button);
    /**
     * @return button
     */
    Button getButton();

    /**
     * @return true if clipboard copying is supported by web browser
     */
    boolean isSupportedByWebBrowser();

    /**
     * Adds {@link CopyEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    Subscription addCopyListener(Consumer<CopyEvent> listener);

    /**
     * Event that is fired when the text content of the input has been copied to the clipboard.
     *
     * @see #addCopyListener(Consumer)
     */
    class CopyEvent extends EventObject {
        private final boolean success;

        public CopyEvent(ClipboardTrigger source, boolean success) {
            super(source);
            this.success = success;
        }

        /**
         * @return true if the text content is set to the client-side clipboard
         */
        public boolean isSuccess() {
            return success;
        }
    }
}