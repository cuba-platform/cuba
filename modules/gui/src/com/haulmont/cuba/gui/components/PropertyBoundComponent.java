/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.model.InstanceContainer;

/**
 * vaadin8 rework !
 */
public interface PropertyBoundComponent {
    // todo remove
    InstanceContainer getEntityContainer();

    // todo remove
    MetaPropertyPath getMetaPropertyPath();

    // todo remove ? TO_BE_DISCUSSED
    void setContainer(InstanceContainer container, String property);
}