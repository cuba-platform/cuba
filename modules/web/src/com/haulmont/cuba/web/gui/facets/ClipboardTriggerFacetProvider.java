/*
 * Copyright (c) 2008-2018 Haulmont.
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

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ClipboardTrigger;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader.ComponentContext;
import com.haulmont.cuba.web.gui.components.WebClipboardTrigger;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component("cuba_ClipboardTriggerFacetProvider")
public class ClipboardTriggerFacetProvider implements FacetProvider<ClipboardTrigger> {
    @Override
    public Class<ClipboardTrigger> getFacetClass() {
        return ClipboardTrigger.class;
    }

    @Override
    public ClipboardTrigger create() {
        return new WebClipboardTrigger();
    }

    @Override
    public String getFacetTag() {
        return "clipboardTrigger";
    }

    @Override
    public void loadFromXml(ClipboardTrigger facet, Element element, ComponentContext context) {
        String id = element.attributeValue("id");
        if (isNotEmpty(id)) {
            facet.setId(id);
        }

        String button = element.attributeValue("button");
        String input = element.attributeValue("input");

        // find components before screen initialization
        context.addInjectTask(new ClipboardRefsInitTask(facet, context, button, input));
    }

    public static class ClipboardRefsInitTask implements ComponentLoader.InjectTask {

        protected final String button;
        protected final String input;
        protected final ClipboardTrigger facet;
        protected final ComponentContext context;

        public ClipboardRefsInitTask(ClipboardTrigger facet, ComponentContext context, String button, String input) {
            this.button = button;
            this.input = input;
            this.facet = facet;
            this.context = context;
        }

        @Override
        public void execute(ComponentContext windowContext, Frame window) {
            Frame frame = context.getFrame();

            if (button != null) {
                com.haulmont.cuba.gui.components.Component component = frame.getComponent(button);
                if (component == null) {
                    throw new GuiDevelopmentException(
                            String.format("Unable to find button %s for ClipboardTrigger", button), context);
                }

                if (!(component instanceof Button)) {
                    throw new GuiDevelopmentException(String.format("Component %s is not Button", button), windowContext);
                }

                facet.setButton((Button) component);
            }

            if (input != null) {
                com.haulmont.cuba.gui.components.Component component = frame.getComponent(input);
                if (component == null) {
                    throw new GuiDevelopmentException(
                            String.format("Unable to find input %s for ClipboardTrigger", input), context);
                }

                if (!(component instanceof TextInputField)) {
                    throw new GuiDevelopmentException(String.format("Component %s is not TextInputField", button), context);
                }

                facet.setInput((TextInputField<?>) component);
            }
        }
    }
}