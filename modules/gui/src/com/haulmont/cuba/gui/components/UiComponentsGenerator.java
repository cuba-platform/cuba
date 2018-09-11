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

package com.haulmont.cuba.gui.components;

import javax.inject.Inject;
import java.util.List;

@org.springframework.stereotype.Component(UiComponentsGenerator.NAME)
public class UiComponentsGenerator {
    public static final String NAME = "cuba_UiComponentsGenerator";

    @Inject
    protected List<ComponentGenerationStrategy> componentGenerationStrategies;

    /**
     * Creates a component according to the given {@link ComponentGenerationContext}.
     * <p>
     * Trying to find {@link ComponentGenerationStrategy} implementations. If at least one strategy exists, then:
     * <ol>
     * <li>Iterates over factories according to the {@link org.springframework.core.Ordered} interface.</li>
     * <li>Returns the first created not {@code null} component.</li>
     * </ol>
     *
     * @param context the {@link ComponentGenerationContext} instance
     * @return a component instance for the current client type (web or desktop)
     * @throws IllegalArgumentException if no component can be created for a given context
     */
    public Component generate(ComponentGenerationContext context) {
        List<ComponentGenerationStrategy> strategies = getComponentGenerationStrategies();

        for (ComponentGenerationStrategy strategy : strategies) {
            Component component = strategy.createComponent(context);
            if (component != null) {
                return component;
            }
        }

        throw new IllegalArgumentException(String.format("Can't create component for the '%s' with " +
                "given meta class '%s'", context.getProperty(), context.getMetaClass()));
    }

    protected List<ComponentGenerationStrategy> getComponentGenerationStrategies() {
        return componentGenerationStrategies;
    }
}