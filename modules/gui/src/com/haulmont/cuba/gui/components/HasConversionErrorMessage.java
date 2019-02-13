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
 * Component annotated by this interface enables to configure custom error message
 * when conversion to specified datatype fails.
 */
public interface HasConversionErrorMessage {

    /**
     * Sets custom conversion error message that is used when value doesn't correspond to datatype.
     *
     * @param conversionErrorMessage custom conversion error message
     */
    void setConversionErrorMessage(String conversionErrorMessage);

    /**
     * @return custom conversion error message
     */
    String getConversionErrorMessage();
}
