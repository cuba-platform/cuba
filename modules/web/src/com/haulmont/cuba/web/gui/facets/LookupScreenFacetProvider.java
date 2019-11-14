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

package com.haulmont.cuba.web.gui.facets;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.LookupScreenFacet;
import com.haulmont.cuba.web.gui.components.WebLookupScreenFacet;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(LookupScreenFacetProvider.NAME)
public class LookupScreenFacetProvider
        extends AbstractEntityAwareScreenFacetProvider<LookupScreenFacet> {

    public static final String NAME = "cuba_LookupScreenFacetProvider";

    @Inject
    protected BeanLocator beanLocator;
    @Inject
    protected Metadata metadata;

    @Override
    public Class<LookupScreenFacet> getFacetClass() {
        return LookupScreenFacet.class;
    }

    @Override
    public LookupScreenFacet create() {
        WebLookupScreenFacet lookupScreenFacet = new WebLookupScreenFacet();
        lookupScreenFacet.setBeanLocator(beanLocator);
        return lookupScreenFacet;
    }

    @Override
    public String getFacetTag() {
        return "lookupScreen";
    }

    @Override
    protected Metadata getMetadata() {
        return metadata;
    }
}
