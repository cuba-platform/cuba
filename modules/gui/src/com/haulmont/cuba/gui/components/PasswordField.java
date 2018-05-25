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

public interface PasswordField extends TextInputField, TextInputField.MaxLengthLimited {

    String NAME = "passwordField";

    /**
     * Return autocomplete attribute value to specify saving it in browser.
     */
    boolean isAutocomplete();

    /**
     * Set autocomplete attribute value to specify saving it in browser.
     * False value disables saving passwords in browser.
     */
    void setAutocomplete(Boolean autocomplete);

    /**
     * Sets CapsLockIndicator component, that will be indicate when caps lock key is active.
     *
     * @param capsLockIndicator capsLockIndicator component
     */
    void setCapsLockIndicator(CapsLockIndicator capsLockIndicator);

    /**
     * @return capsLockIndicator component
     */
    CapsLockIndicator getCapsLockIndicator();

    @SuppressWarnings("unchecked")
    @Override
    String getValue();
}