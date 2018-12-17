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

package com.haulmont.cuba.gui.builders;

import com.haulmont.cuba.gui.screen.Screen;

import java.util.Objects;
import java.util.function.Consumer;

public class AfterCloseListenerAdapter implements Consumer<Screen.AfterCloseEvent> {

    protected final Consumer<AfterScreenCloseEvent> delegate;

    public AfterCloseListenerAdapter(Consumer<AfterScreenCloseEvent> delegate) {
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void accept(Screen.AfterCloseEvent event) {
        delegate.accept(new AfterScreenCloseEvent(event.getScreen(), event.getCloseAction()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterCloseListenerAdapter that = (AfterCloseListenerAdapter) o;
        return delegate.equals(that.delegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }
}