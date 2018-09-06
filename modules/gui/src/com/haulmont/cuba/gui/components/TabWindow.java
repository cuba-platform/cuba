/*
 * Copyright (c) 2008-2018 Haulmont.
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
 * JavaDoc
 */
public interface TabWindow extends Window {
    /**
     * Name that is used to register a client type specific screen implementation in
     * {@link com.haulmont.cuba.gui.xml.layout.ComponentsFactory}
     */
    String NAME = "tabWindow";

    String formatTabCaption();

    String formatTabDescription();

    /**
     * Returns how the managed main TabSheet switches a tab with this window: hides or unloads its content.
     *
     * @return one of the {@link ContentSwitchMode} enum values
     */
    ContentSwitchMode getContentSwitchMode();

    /**
     * Sets how the managed main TabSheet switches a tab with this window: hides or unloads its content.
     * <p>
     * Note that: a method invocation will take effect only if {@code cuba.web.mainTabSheetMode} property
     * is set to 'MANAGED'.
     *
     * @param mode one of the {@link ContentSwitchMode} enum values
     */
    void setContentSwitchMode(ContentSwitchMode mode);
}