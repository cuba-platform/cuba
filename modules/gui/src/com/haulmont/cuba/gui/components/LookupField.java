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

/**
 */
public interface LookupField extends OptionsField, Component.HasInputPrompt {

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