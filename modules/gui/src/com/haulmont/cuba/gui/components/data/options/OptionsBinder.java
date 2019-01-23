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

package com.haulmont.cuba.gui.components.data.options;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.meta.OptionsBinding;

import java.util.stream.Stream;

@org.springframework.stereotype.Component(OptionsBinder.NAME)
public class OptionsBinder {

    public static final String NAME = "cuba_OptionsBinder";

    public <V> OptionsBinding<V> bind(Options<V> options, Component component,
                                      OptionsTarget<V> optionsTarget) {

        OptionBindingImpl<V> binding = new OptionBindingImpl<>(options, component, optionsTarget);
        binding.bind();
        return binding;
    }

    @FunctionalInterface
    public interface OptionsTarget<V> {
        void setOptions(Stream<V> options);
    }

    public static class OptionBindingImpl<V> implements OptionsBinding<V> {
        protected Options<V> source;
        protected OptionsTarget<V> optionsTarget;
        protected Component component;

        protected Subscription componentValueChangeSubscription;

        protected Subscription sourceStateChangeSupscription;
        protected Subscription sourceOptionsChangeSupscription;
        protected Subscription sourceValueChangeSupscription;

        public OptionBindingImpl(Options<V> source, Component component, OptionsTarget<V> optionsTarget) {
            this.source = source;
            this.component = component;
            this.optionsTarget = optionsTarget;
        }

        @Override
        public Options<V> getSource() {
            return source;
        }

        @Override
        public Component getComponent() {
            return component;
        }

        @Override
        public void activate() {
            if (source.getState() == BindingState.ACTIVE) {
                optionsTarget.setOptions(source.getOptions());
            }
        }

        public <E extends Entity> void bind() {
            if (source instanceof EntityOptions
                    && component instanceof HasValue) {
                this.componentValueChangeSubscription =
                        ((HasValue<?>) component).addValueChangeListener(this::componentValueChanged);

                this.sourceValueChangeSupscription =
                        ((EntityOptions<E>) source).addValueChangeListener(this::optionsSourceValueChanged);
            }

            this.sourceStateChangeSupscription = source.addStateChangeListener(this::optionsSourceStateChanged);
            this.sourceOptionsChangeSupscription = source.addOptionsChangeListener(this::optionsSourceOptionsChanged);
        }

        protected void optionsSourceOptionsChanged(@SuppressWarnings("unused") Options.OptionsChangeEvent<V> event) {
            optionsTarget.setOptions(source.getOptions());
        }

        @SuppressWarnings("unchecked")
        protected void optionsSourceValueChanged(EntityOptions.ValueChangeEvent<? extends Entity> event) {
            ((HasValue) optionsTarget).setValue(event.getValue());
        }

        @SuppressWarnings("unchecked")
        protected void componentValueChanged(HasValue.ValueChangeEvent event) {
            // value could be List / Set / something else
            if (event.getValue() instanceof Entity
                    || event.getValue() == null) {
                EntityOptions entityOptionsSource = (EntityOptions) this.source;
                entityOptionsSource.setSelectedItem((Entity) event.getValue());
            }
        }

        protected void optionsSourceStateChanged(Options.StateChangeEvent event) {
            if (event.getState() == BindingState.ACTIVE) {
                optionsTarget.setOptions(source.getOptions());
            }
        }

        @Override
        public void unbind() {
            if (this.componentValueChangeSubscription != null) {
                this.componentValueChangeSubscription.remove();
                this.componentValueChangeSubscription = null;
            }

            if (this.sourceValueChangeSupscription != null) {
                this.sourceValueChangeSupscription.remove();
                this.sourceValueChangeSupscription = null;
            }

            if (this.sourceOptionsChangeSupscription != null) {
                this.sourceOptionsChangeSupscription.remove();
                this.sourceOptionsChangeSupscription = null;
            }

            if (this.sourceStateChangeSupscription != null) {
                this.sourceStateChangeSupscription.remove();
                this.sourceStateChangeSupscription = null;
            }
        }
    }
}