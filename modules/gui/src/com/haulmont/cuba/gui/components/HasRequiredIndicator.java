/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

/**
 * An interface implemented by components that can have a required indicator.
 */
public interface HasRequiredIndicator {

    /**
     * Returns whether a required indicator should be shown.
     *
     * @return {@code true} if a required indicator should be shown, {@code false} otherwise
     */
    boolean isRequiredIndicatorVisible();

    /**
     * Sets whether a required indicator should be shown.
     *
     * @param visible {@code true} if a required indicator should be shown, {@code false} otherwise
     */
    void setRequiredIndicatorVisible(boolean visible);
}
