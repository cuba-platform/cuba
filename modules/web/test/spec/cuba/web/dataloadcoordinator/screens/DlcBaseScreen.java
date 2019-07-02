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
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.testmodel.petclinic.Owner;
import com.haulmont.cuba.web.testmodel.petclinic.OwnerCategory;
import com.haulmont.cuba.web.testmodel.petclinic.Pet;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DlcBaseScreen extends Screen {

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
    public DataLoadCoordinator dlc;

    @Inject
    public CollectionContainer<Owner> ownersDc;

    @Inject
    public TextField<String> nameFilterField;

    @Inject
    public PickerField<OwnerCategory> categoryFilterField;

    @Install(to = "ownersDl", target = Target.DATA_LOADER)
    private List<Owner> ownersDlLoadDelegate(LoadContext<Owner> loadContext) {
        events.add(new LoadEvent("ownersDl", loadContext));

        List<Owner> list = new ArrayList<>();
        Owner owner = metadata.create(Owner.class);
        owner.setName("Joe");
        list.add(owner);
        if (loadContext.getQuery().getParameters().isEmpty()) {
            owner = metadata.create(Owner.class);
            owner.setName("Jane");
            list.add(owner);
        }
        return list;
    }

    @Install(to = "petsDl", target = Target.DATA_LOADER)
    private List<Pet> petsDlLoadDelegate(LoadContext<Pet> loadContext) {
        events.add(new LoadEvent("petsDl", loadContext));

        Pet pet = metadata.create(Pet.class);
        pet.setName("Misty");
        return Collections.singletonList(pet);
    }
}