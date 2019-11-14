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

package spec.cuba.web.screenfacet.screens;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ScreenFacet;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@UiController("cuba_ScreenFacetTestScreen")
@UiDescriptor("screen-facet-test-screen.xml")
public class ScreenFacetTestScreen extends Screen {

    @Inject
    public Button button;
    @Inject
    public Action action;

    @Inject
    public ScreenFacet<ScreenToOpenWithFacet> screenIdFacet;
    @Inject
    public ScreenFacet<ScreenToOpenWithFacet> screenClassFacet;

    public boolean afterShowListenerTriggered = false;
    public boolean afterCloseListenerTriggered = false;

    @Subscribe("screenIdFacet")
    public void onScreenAfterShow(ScreenFacet.AfterShowEvent event) {
        afterShowListenerTriggered = true;
    }

    @Subscribe("screenIdFacet")
    public void onScreenAfterClose(ScreenFacet.AfterCloseEvent event) {
        afterCloseListenerTriggered = true;
    }
}
