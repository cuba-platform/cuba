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
import com.haulmont.cuba.gui.components.data.EntityOptionsSource;
import com.haulmont.cuba.gui.components.data.OptionsBinding;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import java.util.stream.Stream;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component(OptionsBinder.NAME)
public class OptionsBinder {

    public static final String NAME = "cuba_OptionsBinder";

    public <V> OptionsBinding<V> bind(OptionsSource<V> optionsSource, Component component,
                                      OptionsTarget<V> optionsTarget) {

        OptionBindingImpl<V> binding = new OptionBindingImpl<>(optionsSource, component, optionsTarget);
        binding.bind();
        return binding;
    }

    public interface OptionsTarget<V> {
        void setOptions(Stream<V> options);
    }

    public static class OptionBindingImpl<V> implements OptionsBinding<V> {
        protected OptionsSource<V> source;
        protected OptionsTarget<V> optionsTarget;
        protected Component component;

        protected Subscription componentValueChangeSubscription;

        protected Subscription sourceStateChangeSupscription;
        protected Subscription sourceOptionsChangeSupscription;
        protected Subscription sourceValueChangeSupscription;

        public OptionBindingImpl(OptionsSource<V> source, Component component, OptionsTarget<V> optionsTarget) {
            this.source = source;
            this.component = component;
            this.optionsTarget = optionsTarget;
        }

        @Override
        public OptionsSource<V> getSource() {
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

        public void bind() {
            if (source instanceof EntityOptionsSource
                    && component instanceof HasValue) {
                this.componentValueChangeSubscription = ((HasValue) component).addValueChangeListener(this::componentValueChanged);
            }
            // vaadin8 weak references for listeners ?
            this.sourceStateChangeSupscription = source.addStateChangeListener(this::optionsSourceStateChanged);
            this.sourceValueChangeSupscription = source.addValueChangeListener(this::optionsSourceValueChanged);
            this.sourceOptionsChangeSupscription = source.addOptionsChangeListener(this::optionsSourceOptionsChanged);
        }

        protected void optionsSourceOptionsChanged(OptionsSource.OptionsChangeEvent<V> event) {
            optionsTarget.setOptions(source.getOptions());
        }

        protected void optionsSourceValueChanged(OptionsSource.ValueChangeEvent<V> event) {
            optionsTarget.setOptions(source.getOptions());
        }

        @SuppressWarnings("unchecked")
        protected void componentValueChanged(HasValue.ValueChangeEvent event) {
            EntityOptionsSource entityOptionsSource = (EntityOptionsSource) this.source;
            entityOptionsSource.setSelectedItem((Entity) event.getValue());
        }

        protected void optionsSourceStateChanged(OptionsSource.StateChangeEvent<V> event) {
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
        }
    }
}