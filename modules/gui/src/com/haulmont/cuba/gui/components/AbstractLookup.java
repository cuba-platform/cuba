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

/**
 * Base class for lookup screen controllers.
 *
 */
public class AbstractLookup extends AbstractWindow implements Window.Lookup {

    public AbstractLookup() {
    }

    @Override
    public Component getLookupComponent() {
        return ((Lookup) frame).getLookupComponent();
    }

    @Override
    public void setLookupComponent(Component lookupComponent) {
        ((Lookup) frame).setLookupComponent(lookupComponent);
    }

    @Override
    public Handler getLookupHandler() {
        return ((Lookup) frame).getLookupHandler();
    }

    @Override
    public void setLookupHandler(Handler handler) {
        ((Lookup) frame).setLookupHandler(handler);
    }

    @Override
    public Validator getLookupValidator() {
        return ((Lookup) frame).getLookupValidator();
    }

    @Override
    public void setLookupValidator(Validator validator) {
        if (frame instanceof Window.Lookup) {
            ((Lookup) frame).setLookupValidator(validator);
        }
    }
}