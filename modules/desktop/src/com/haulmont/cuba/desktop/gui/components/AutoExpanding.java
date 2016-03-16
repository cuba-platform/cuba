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

package com.haulmont.cuba.desktop.gui.components;

/**
 *
 * Returns, whether component is trying to gain all available space by default,
 * when size isn't set explicitly.
 * Components not implementing this interface, considered non-expanding by default.
 *
 * In vaadin, this logic is built into client GWT side of components.
 *
 */
public interface AutoExpanding {
    boolean expandsWidth();
    boolean expandsHeight();
}
