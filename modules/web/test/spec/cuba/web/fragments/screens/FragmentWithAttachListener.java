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

import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;

import java.util.ArrayList;
import java.util.List;

@UiController("test_TestAttachFragment")
public class FragmentWithAttachListener extends ScreenFragment {

    public List<Class> eventLog = new ArrayList<>();

    @Subscribe
    protected void onAttach(AttachEvent event) {
        eventLog.add(AttachEvent.class);
    }

    @Subscribe
    protected void onInit(InitEvent event) {
        eventLog.add(InitEvent.class);
    }

    @Subscribe
    protected void onAfterInit(AfterInitEvent event) {
        eventLog.add(AfterInitEvent.class);
    }
}