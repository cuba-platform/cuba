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

import com.haulmont.cuba.testmodel.petclinic.Owner;
import com.haulmont.cuba.testmodel.petclinic.Pet;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewBuilderTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testBuild() {
        View view = ViewBuilder.of(Pet.class).build();

        assertNotNull(view);
        assertFalse(containsSystemProperties(view));
        assertFalse(view.containsProperty("name"));
    }

    @Test
    public void testProperty() {
        View view = ViewBuilder.of(Pet.class).add("name").build();

        assertFalse(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));
    }

    @Test
    public void testRefProperty() {
        View view = ViewBuilder.of(Pet.class).add("owner").build();

        assertFalse(containsSystemProperties(view));

        assertNotNull(view.getProperty("owner"));
        View ownerView = view.getProperty("owner").getView();
        assertNotNull(ownerView);
        assertFalse(containsSystemProperties(ownerView));
        assertFalse(ownerView.containsProperty("name"));
    }

    @Test
    public void testInlineRefProperty() {
        View view = ViewBuilder.of(Pet.class)
                .add("owner.name")
                .build();

        assertFalse(containsSystemProperties(view));

        assertNotNull(view.getProperty("owner"));
        View ownerView = view.getProperty("owner").getView();
        assertNotNull(ownerView);
        assertFalse(containsSystemProperties(ownerView));
        assertTrue(ownerView.containsProperty("name"));
    }

    @Test
    public void testRefView() {
        View view = ViewBuilder.of(Pet.class)
                .add("owner", builder -> builder.add("name"))
                .build();

        assertFalse(containsSystemProperties(view));

        assertNotNull(view.getProperty("owner"));
        View ownerView = view.getProperty("owner").getView();
        assertNotNull(ownerView);
        assertFalse(containsSystemProperties(ownerView));
        assertTrue(ownerView.containsProperty("name"));
    }

    @Test
    public void testRefLocalView() {
        View view = ViewBuilder.of(Pet.class)
                .add("owner", View.LOCAL)
                .build();

        assertFalse(containsSystemProperties(view));

        assertNotNull(view.getProperty("owner"));
        View ownerView = view.getProperty("owner").getView();
        assertNotNull(ownerView);
        assertFalse(containsSystemProperties(ownerView));
        assertTrue(ownerView.containsProperty("name"));
        assertFalse(ownerView.containsProperty("address"));
    }

    @Test
    public void testProperties() {
        View view = ViewBuilder.of(Pet.class).addAll("name", "nick").build();

        assertFalse(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));
    }

    @Test
    public void testSystem() {
        View view = ViewBuilder.of(Pet.class).addSystem().addAll("name").build();

        assertTrue(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));

        view = ViewBuilder.of(Pet.class).addSystem().addView(View.LOCAL).build();

        assertTrue(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));
    }

    @Test
    public void testMinimal() {
        View view = ViewBuilder.of(Pet.class).addView(View.MINIMAL).build();

        assertFalse(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));
    }

    @Test
    public void testLocal() {
        View petView = ViewBuilder.of(Pet.class).addView(View.LOCAL).build();

        assertFalse(containsSystemProperties(petView));
        assertTrue(petView.containsProperty("name"));

        View ownerView = ViewBuilder.of(Owner.class).addView(View.LOCAL).build();
        assertFalse(containsSystemProperties(ownerView));
        assertTrue(ownerView.containsProperty("name"));
        assertFalse(ownerView.containsProperty("address"));
    }

    @Test
    public void testBase() {
        View view = ViewBuilder.of(Pet.class).addView(View.BASE).build();

        assertFalse(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));
    }

    @Test
    public void testLocalAndRef() {
        View view = ViewBuilder.of(Pet.class)
                .addView(View.LOCAL)
                .add("owner")
                .build();

        assertFalse(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));

        assertNotNull(view.getProperty("owner"));
        View ownerView = view.getProperty("owner").getView();
        assertNotNull(ownerView);
        assertFalse(containsSystemProperties(ownerView));
        assertFalse(ownerView.containsProperty("name"));
        assertFalse(ownerView.containsProperty("address"));

        view = ViewBuilder.of(Pet.class)
                .addView(View.LOCAL)
                .add("owner.name")
                .add("owner.address.city")
                .build();

        assertFalse(containsSystemProperties(view));
        assertTrue(view.containsProperty("name"));

        assertNotNull(view.getProperty("owner"));
        ownerView = view.getProperty("owner").getView();
        assertNotNull(ownerView);
        assertFalse(containsSystemProperties(ownerView));
        assertTrue(ownerView.containsProperty("name"));
        assertTrue(ownerView.containsProperty("address"));

        View addressView = ownerView.getProperty("address").getView();
        assertTrue(addressView.containsProperty("city"));
    }

    @Test
    public void testMerging() {
        View view1 = ViewBuilder.of(Pet.class)
                .add("owner", View.LOCAL)
                .build();

        View view2 = ViewBuilder.of(Pet.class)
                .addView(view1)
                .add("name")
                .build();

        ViewProperty ownerProp = view2.getProperty("owner");
        assertTrue(ownerProp != null && ownerProp.getView() != null);
        assertTrue(ownerProp.getView().containsProperty("name"));
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