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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ActionsHolder;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.PickerField;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class PickerFieldLoader extends AbstractFieldLoader<PickerField> {
    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(PickerField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadTabIndex(resultComponent, element);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            resultComponent.setCaptionMode(CaptionMode.PROPERTY);
            resultComponent.setCaptionProperty(captionProperty);
        }

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            resultComponent.setMetaClass(getMetadata().getClassNN(metaClass));
        }

        loadActions(resultComponent, element);
        if (resultComponent.getActions().isEmpty()) {
            boolean actionsByMetaAnnotations = ComponentsHelper.createActionsByMetaAnnotations(resultComponent);
            if (!actionsByMetaAnnotations) {
                resultComponent.addLookupAction();
                resultComponent.addClearAction();
            }
        }

        loadBuffered(resultComponent, element);
    }

    protected Metadata getMetadata() {
        return beanLocator.get(Metadata.NAME);
    }

    @Override
    protected Action loadDeclarativeAction(ActionsHolder actionsHolder, Element element) {
        return loadPickerDeclarativeAction(actionsHolder, element);
    }
}