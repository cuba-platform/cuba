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

package com.haulmont.cuba.core.global;


import com.haulmont.cuba.testmodel.petclinic.Pet;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FluentLoaderViewBuilderTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private DataManager dataManager;

    @BeforeEach
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);
    }

    @Test
    public void testUsage() {
        UUID petId = UUID.randomUUID();

        dataManager.load(Pet.class)
                .id(petId)
                .view(viewBuilder -> viewBuilder.addAll(
                        "name",
                        "owner.name",
                        "owner.address.city"))
                /*.one()*/;

        dataManager.load(Pet.class)
                .view(viewBuilder -> viewBuilder.addView(View.MINIMAL).addAll(
                        "owner.name",
                        "owner.address.city"))
                .id(petId)
                /*.one()*/;

        dataManager.load(Pet.class)
                .id(petId)
                .viewProperties(
                        "name",
                        "owner.name",
                        "owner.address.city")
                /*.one()*/;

        dataManager.load(Pet.class)
                .ids(petId)
                .viewProperties(
                        "name",
                        "owner.name",
                        "owner.address.city")
                /*.one()*/;

        dataManager.load(Pet.class)
                .query("...")
                .view(viewBuilder -> viewBuilder.addView(View.LOCAL).addAll(
                        "owner.name",
                        "owner.address.city"))
                /*.list()*/;

        dataManager.load(Pet.class)
                .query("...")
                .viewProperties(
                        "name",
                        "owner.name",
                        "owner.address.city")
                /*.list()*/;
    }

    @Test
    public void testLoadContext() {
        LoadContext<Pet> loadContext = dataManager.load(Pet.class)
                .view(viewBuilder -> viewBuilder.addAll(
                        "name",
                        "owner.name",
                        "owner.address.city"))
                .createLoadContext();

        View view = loadContext.getView();
        assertFalse(containsSystemProperties(view));
        checkPetView(view);

        loadContext = dataManager.load(Pet.class)
                .view(viewBuilder -> viewBuilder.addView(View.LOCAL).addAll(
                        "owner.name",
                        "owner.address.city"))
                .createLoadContext();

        view = loadContext.getView();
        assertFalse(containsSystemProperties(view));
        checkPetView(view);

        loadContext = dataManager.load(Pet.class)
                .view(viewBuilder -> viewBuilder.addView(View.LOCAL).addAll(
                        "owner.name",
                        "owner.address.city"))
                .createLoadContext();

        view = loadContext.getView();
        assertFalse(containsSystemProperties(view));
        checkPetView(view);

        loadContext = dataManager.load(Pet.class)
                .view(viewBuilder -> viewBuilder.addView(View.LOCAL).addSystem().addAll(
                        "owner.name",
                        "owner.address.city"))
                .createLoadContext();

        view = loadContext.getView();
        assertTrue(containsSystemProperties(view));
        checkPetView(view);

        loadContext = dataManager.load(Pet.class)
                .viewProperties(
                        "name",
                        "owner.name",
                        "owner.address.city")
                .createLoadContext();

        view = loadContext.getView();
        assertFalse(containsSystemProperties(view));
        checkPetView(view);

        loadContext = dataManager.load(Pet.class)
                .view(viewBuilder -> viewBuilder.addView(View.LOCAL))
                .viewProperties(
                        "owner.name",
                        "owner.address.city")
                .createLoadContext();

        view = loadContext.getView();
        assertFalse(containsSystemProperties(view));
        checkPetView(view);
    }

    private void checkPetView(View view) {
        assertTrue(view.containsProperty("name"));

        assertNotNull(view.getProperty("owner"));
        View ownerView = view.getProperty("owner").getView();
        assertNotNull(ownerView);
        assertFalse(containsSystemProperties(ownerView));
        assertTrue(ownerView.containsProperty("name"));
        assertTrue(ownerView.containsProperty("address"));

        View addressView = ownerView.getProperty("address").getView();
        assertTrue(addressView.containsProperty("city"));
    }

    private boolean containsSystemProperties(View view) {
        return view.containsProperty("id")
                && view.containsProperty("version")
                && view.containsProperty("deleteTs")
                && view.containsProperty("deletedBy")
                && view.containsProperty("createTs")
                && view.containsProperty("createdBy")
                && view.containsProperty("updateTs")
                && view.containsProperty("updatedBy");
    }
}