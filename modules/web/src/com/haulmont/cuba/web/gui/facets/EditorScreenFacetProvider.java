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
import com.haulmont.cuba.gui.builders.EditMode;
import com.haulmont.cuba.gui.components.EditorScreenFacet;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.web.gui.components.WebEditorScreenFacet;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component(EditorScreenFacetProvider.NAME)
public class EditorScreenFacetProvider
        extends AbstractEntityAwareScreenFacetProvider<EditorScreenFacet> {

    public static final String NAME = "cuba_EditorScreenFacetProvider";

    @Inject
    protected Metadata metadata;
    @Inject
    protected BeanLocator beanLocator;

    @Override
    public Class<EditorScreenFacet> getFacetClass() {
        return EditorScreenFacet.class;
    }

    @Override
    public EditorScreenFacet create() {
        WebEditorScreenFacet editorScreenFacet = new WebEditorScreenFacet();
        editorScreenFacet.setBeanLocator(beanLocator);
        return editorScreenFacet;
    }

    @Override
    public String getFacetTag() {
        return "editorScreen";
    }

    @Override
    public void loadFromXml(EditorScreenFacet facet, Element element,
                            ComponentLoader.ComponentContext context) {
        super.loadFromXml(facet, element, context);

        loadEditMode(facet, element);
        loadAddFirst(facet, element);
    }

    @Override
    protected Metadata getMetadata() {
        return metadata;
    }

    protected void loadAddFirst(EditorScreenFacet facet, Element element) {
        String addFirst = element.attributeValue("addFirst");
        if (isNotEmpty(addFirst)) {
            facet.setAddFirst(Boolean.parseBoolean(addFirst));
        }
    }

    protected void loadEditMode(EditorScreenFacet facet, Element element) {
        String editMode = element.attributeValue("editMode");
        if (isNotEmpty(editMode)) {
            facet.setEditMode(EditMode.valueOf(editMode));
        }
    }
}
