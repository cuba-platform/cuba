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

import com.haulmont.cuba.gui.components.data.HasValueSource;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Base interface for "fields" - components intended to display and edit value of a certain entity attribute.
 */
public interface Field<V> extends DatasourceComponent<V>, HasValueSource<V>, Component.HasCaption,
        HasValue<V>, Component.Editable, Component.BelongToFrame, Validatable, Component.HasIcon,
        HasContextHelp, HasHtmlCaption, HasHtmlDescription {

    /**
     * @return whether the field must contain a non-null value
     */
    boolean isRequired();
    void setRequired(boolean required);

    String getRequiredMessage();
    /**
     * A message that will be displayed to user if the field is required but has null value
     */
    void setRequiredMessage(String msg);

    /**
     * Add validator instance.
     * {@link ValidationException} this exception must be thrown by the validator if the value is not valid.
     */
    void addValidator(Consumer<? super V> validator);
    void removeValidator(Consumer<V> validator);

    default void addValidators(Consumer<? super V>... validators) {
        for (Consumer<? super V> validator : validators) {
            addValidator(validator);
        }
    }

    /**
     * @return unmodifiable collection with Field validators
     */
    Collection<Consumer<V>> getValidators();

    /**
     * Field validator.<br>
     * Validators are invoked when {@link Validatable#validate()} is called.
     * Editor screen calls {@code validate()} on commit.
     *
     * @deprecated Use typed {@link Consumer} instead.
     */
    @Deprecated
    interface Validator<T> extends Consumer<T> {
        /**
         * @param value field value to validate
         * @throws ValidationException this exception must be thrown by the validator if the value is not valid
         */
        void validate(@Nullable Object value) throws ValidationException;

        @Override
        default void accept(T t) {
            validate(t);
        }
    }
}