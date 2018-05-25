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

package spec.cuba.core.persistence_xml;

import com.haulmont.cuba.core.app.PersistenceXmlPostProcessor;
import org.dom4j.Document;
import org.dom4j.Element;

public class TestPersistenceXmlPostProcessor implements PersistenceXmlPostProcessor {

    @Override
    public void process(Document ormXml) {
        Element rootEl = ormXml.getRootElement();
        Element puEl = rootEl.element("persistence-unit");
        Element propsEl = puEl.element("properties");
        Element testEl = propsEl.addElement("property");
        testEl.addAttribute("name", "some-prop");
        testEl.addAttribute("value", "some-val");
    }
}
