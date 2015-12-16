/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface LookupField extends OptionsField {

    String NAME = "lookupField";

    Object getNullOption();
    void setNullOption(Object nullOption);

    FilterMode getFilterMode();
    void setFilterMode(FilterMode mode);

    /**
     * @return true if the component handles new options entered by user.
     * @see LookupField.NewOptionHandler
     */
    boolean isNewOptionAllowed();
    /**
     * Makes the component handle new options entered by user.
     * @see LookupField.NewOptionHandler
     */
    void setNewOptionAllowed(boolean newOptionAllowed);

    /**
     * @return true if text input allowed
     */
    boolean isTextInputAllowed();
    /**
     * Sets whether it is possible to input text into the field or whether the field area of the component is just used
     * to show what is selected.
     */
    void setTextInputAllowed(boolean textInputAllowed);

    /**
     * @return current handler
     */
    NewOptionHandler getNewOptionHandler();
    /**
     * Set handler.
     * @param newOptionHandler handler instance
     */
    void setNewOptionHandler(NewOptionHandler newOptionHandler);

    enum FilterMode {
            NO,
            STARTS_WITH,
            CONTAINS
    }

    /**
     * @return current input prompt.
     */
    String getInputPrompt();
    /**
     * Sets the input prompt - a textual prompt that is displayed when the field
     * would otherwise be empty, to prompt the user for input.
     *
     * @param inputPrompt input prompt
     */
    void setInputPrompt(String inputPrompt);

    /**
     * Interface to be implemented if {@link #setNewOptionAllowed(boolean)} is set to true.
     */
    interface NewOptionHandler {
        /**
         * Called when user enters a value which is not in the options list, and presses Enter.
         * @param caption value entered by user
         */
        void addNewOption(String caption);
    }
}