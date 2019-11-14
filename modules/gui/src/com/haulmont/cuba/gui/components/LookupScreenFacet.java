/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.meta.StudioDelegate;
import com.haulmont.cuba.gui.meta.StudioFacet;
import com.haulmont.cuba.gui.meta.StudioProperties;
import com.haulmont.cuba.gui.meta.StudioProperty;
import com.haulmont.cuba.gui.screen.LookupScreen;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Prepares and shows lookup screens.
 */
@StudioFacet(
        caption = "Lookup Screen",
        description = "Prepares and shows lookup screens"
)
@StudioProperties(
        properties = {
                @StudioProperty(name = "id", required = true)
        }
)
public interface LookupScreenFacet<E extends Entity, S extends Screen>
        extends ScreenFacet<S>, EntityAwareScreenFacet<E> {

    /**
     * Sets select handler for the lookup screen.
     */
    @StudioDelegate
    void setSelectHandler(Consumer<Collection<E>> selectHandler);

    /**
     * @return lookup screen select handler
     */
    Consumer<Collection<E>> getSelectHandler();

    /**
     * Sets select validator for the lookup screen.
     */
    @StudioDelegate
    void setSelectValidator(Predicate<LookupScreen.ValidationContext<E>> selectValidator);

    /**
     * @return lookup screen select validator
     */
    Predicate<LookupScreen.ValidationContext<E>> getSelectValidator();

    /**
     * Sets code to transform entities after selection.
     * <p>
     * Applied only if either field or container or listComponent is assigned.
     */
    @StudioDelegate
    void setTransformation(Function<Collection<E>, Collection<E>> transformation);

    /**
     * @return selected entities transformation
     */
    Function<Collection<E>, Collection<E>> getTransformation();
}
