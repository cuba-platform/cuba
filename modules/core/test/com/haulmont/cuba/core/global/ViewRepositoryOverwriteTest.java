/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ViewRepositoryOverwriteTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private ViewRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        repository = cont.metadata().getViewRepository();

        InputStream stream = ViewRepositoryTest.class.getResourceAsStream("test-overwrite.view.xml");
        ((AbstractViewRepository) repository).deployViews(stream);
    }

    @Test
    public void testDependentUpdated() {
        View dependentView = repository.getView(UserRole.class, "dependent");
        View userView = dependentView.getProperty("user").getView();

        ViewProperty groupProperty = userView.getProperty("group");
        assertNotNull(groupProperty);
        assertEquals(groupProperty.getView().getName(), "_local");
    }
}