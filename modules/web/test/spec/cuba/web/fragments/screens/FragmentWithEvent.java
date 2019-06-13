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

package spec.cuba.web.fragments.screens;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiController;

import java.util.EventObject;
import java.util.function.Consumer;

@UiController("test_FragmentWithEvent")
public class FragmentWithEvent extends ScreenFragment {

    public void hello() {
        fireEvent(HelloEvent.class, new HelloEvent(this));
    }

    public Subscription addHelloListener(Consumer<HelloEvent> listener) {
        return getEventHub().subscribe(HelloEvent.class, listener);
    }

    public static class HelloEvent extends EventObject {
        public HelloEvent(Object source) {
            super(source);
        }
    }
}