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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.HasContextHelp;

import java.util.function.Consumer;

/**
 * {@link CompositeComponent} having a context help.
 * Default implementations delegate their execution to {@link CompositeComponent#getComposition()}.
 */
public interface CompositeWithContextHelp extends HasContextHelp {

    @Override
    default String getContextHelpText() {
        HasContextHelp hasContextHelp = (HasContextHelp) ((CompositeComponent) this).getCompositionNN();
        return hasContextHelp.getContextHelpText();
    }

    @Override
    default void setContextHelpText(String contextHelpText) {
        HasContextHelp hasContextHelp = (HasContextHelp) ((CompositeComponent) this).getCompositionNN();
        hasContextHelp.setContextHelpText(contextHelpText);
    }

    @Override
    default boolean isContextHelpTextHtmlEnabled() {
        HasContextHelp hasContextHelp = (HasContextHelp) ((CompositeComponent) this).getCompositionNN();
        return hasContextHelp.isContextHelpTextHtmlEnabled();
    }

    @Override
    default void setContextHelpTextHtmlEnabled(boolean enabled) {
        HasContextHelp hasContextHelp = (HasContextHelp) ((CompositeComponent) this).getCompositionNN();
        hasContextHelp.setContextHelpTextHtmlEnabled(enabled);
    }

    @Override
    default Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        HasContextHelp hasContextHelp = (HasContextHelp) ((CompositeComponent) this).getCompositionNN();
        return hasContextHelp.getContextHelpIconClickHandler();
    }

    @Override
    default void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        HasContextHelp hasContextHelp = (HasContextHelp) ((CompositeComponent) this).getCompositionNN();
        hasContextHelp.setContextHelpIconClickHandler(handler);
    }
}
