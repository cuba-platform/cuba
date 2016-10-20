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
 */

package com.haulmont.cuba.core.sys.serialization;


import org.eclipse.persistence.indirection.ValueHolderInterface;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;

import java.io.Serializable;

public class UnfetchedValueHolder implements ValueHolderInterface, Serializable {
    @Override
    public Object clone() {
        return throwUnfetchedAttributeException();
    }

    @Override
    public Object getValue() {
        return throwUnfetchedAttributeException();
    }

    @Override
    public boolean isInstantiated() {
        return false;
    }

    @Override
    public void setValue(Object value) {
        throwUnfetchedAttributeException();
    }

    protected Object throwUnfetchedAttributeException() {
        throw new IllegalStateException(
                ExceptionLocalization.buildMessage("cannot_get_unfetched_attribute", new Object[]{"", ""}));
    }
}
