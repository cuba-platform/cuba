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

package com.haulmont.cuba.gui.components;

import java.io.Serializable;
import java.util.List;

public class CompositeValidationException extends ValidationException {
    private List<ViolationCause> causes;

    public CompositeValidationException(String message, List<ViolationCause> causes) {
        super(message);
        this.causes = causes;
    }

    public List<ViolationCause> getCauses() {
        return causes;
    }

    public interface ViolationCause extends Serializable {
        String getMessage();
    }
}