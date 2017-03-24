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

/**
 * Factory that generates components for {@link FieldGroup} fields defined declaratively.
 */
public interface FieldGroupFieldFactory {
    String NAME = "cuba_FieldGroupFieldFactory";

    /**
     * Generated Component for {@link FieldGroup} using declarative descriptor.
     *
     * @param fieldConfig configuration of field
     * @return generated component info
     */
    GeneratedField createField(FieldGroup.FieldConfig fieldConfig);

    class GeneratedField {
        private Component component;
        private FieldGroup.FieldAttachMode attachMode = FieldGroup.FieldAttachMode.APPLY_DEFAULTS;

        public GeneratedField(Component component) {
            this.component = component;
        }

        public GeneratedField(Component component, FieldGroup.FieldAttachMode attachMode) {
            this.component = component;
            this.attachMode = attachMode;
        }

        public Component getComponent() {
            return component;
        }

        public FieldGroup.FieldAttachMode getAttachMode() {
            return attachMode;
        }
    }
}