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
 */

package com.haulmont.cuba.web.widgets.grid;

import com.vaadin.data.ValidationResult;
import com.vaadin.ui.CustomField;

public abstract class CubaEditorField<T> extends CustomField<T> {

    public abstract boolean isBuffered();

    public abstract void setBuffered(boolean buffered);

    public abstract ValidationResult validate();

    public abstract void commit();

    public abstract void discard();

    public abstract boolean isModified();
}
