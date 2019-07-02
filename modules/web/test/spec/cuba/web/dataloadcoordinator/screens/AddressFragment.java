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

package spec.cuba.web.dataloadcoordinator.screens;

import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.DataLoadCoordinator;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.testmodel.petclinic.City;
import com.haulmont.cuba.web.testmodel.petclinic.Country;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UiController("pc_AddressFragment")
@UiDescriptor("address-fragment.xml")
public class AddressFragment extends ScreenFragment {

    @Inject
    private Metadata metadata;

    public static class LoadEvent {
        public final String loader;
        public final LoadContext loadContext;

        public LoadEvent(String loader, LoadContext loadContext) {
            this.loader = loader;
            this.loadContext = loadContext;
        }
    }

    public List<LoadEvent> events = new ArrayList<>();

    @Inject
    private LookupField<City> cityField;

    @Inject
    public DataLoadCoordinator addressDlc;

    @Inject
    public CollectionContainer<Country> countriesDc;

    @Subscribe(id = "countriesDc", target = Target.DATA_CONTAINER)
    private void onCountriesDcItemChange(InstanceContainer.ItemChangeEvent<Country> event) {
        cityField.setValue(null);
    }

    @Install(to = "countriesDl", target = Target.DATA_LOADER)
    private List<Country> countriesDlLoadDelegate(LoadContext<Country> loadContext) {
        events.add(new LoadEvent("countriesDl", loadContext));
        return Collections.singletonList(metadata.create(Country.class));
    }

    @Install(to = "citiesDl", target = Target.DATA_LOADER)
    private List<City> citiesDlLoadDelegate(LoadContext<City> loadContext) {
        events.add(new LoadEvent("citiesDl", loadContext));
        return Collections.singletonList(metadata.create(City.class));
    }
    
    
    
}