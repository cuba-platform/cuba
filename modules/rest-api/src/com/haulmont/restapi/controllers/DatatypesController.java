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
 */

package com.haulmont.restapi.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.restapi.exception.RestAPIException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.List;

/**
 * Controller that is used for getting datatypes information.
 */
@RestController
@RequestMapping(value = "/api/metadata/datatypes", produces = "application/json; charset=UTF-8")
public class DatatypesController {

    protected Logger log = LoggerFactory.getLogger(DatatypesController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String getDatatypes() {
        return getDatatypesJson();
    }

    //todo MG cache JSON
    protected String getDatatypesJson() {
        SAXReader reader = new SAXReader();
        URL resource = Datatypes.class.getResource("/datatypes.xml");
        if (resource == null) {
            log.info("Can't find /datatypes.xml, using default datatypes settings");
            resource = Datatypes.class.getResource("/com/haulmont/chile/core/datatypes/datatypes.xml");
        }

        JsonArray jsonArray = new JsonArray();

        try {
            Document document = reader.read(resource);
            Element element = document.getRootElement();

            List<Element> datatypeElements = element.elements("datatype");
            for (Element datatypeElement : datatypeElements) {
                String datatypeClassName = datatypeElement.attributeValue("class");
                try {
                    Datatype datatype;
                    Class<Datatype> datatypeClass = ReflectionHelper.getClass(datatypeClassName);
                    try {
                        final Constructor<Datatype> constructor = datatypeClass.getConstructor(Element.class);
                        datatype = constructor.newInstance(datatypeElement);
                    } catch (Throwable e) {
                        datatype = datatypeClass.newInstance();
                    }

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("name", datatype.getName());

                    for (Attribute attribute : Dom4j.attributes(datatypeElement)) {
                        String attrName = attribute.getName();
                        if ("class".equals(attrName)) continue;
                        Object attrValue = attribute.getData();
                        jsonObject.addProperty(attrName, attrValue.toString());
                    }

                    jsonArray.add(jsonObject);
                } catch (Throwable e) {
                    log.error(String.format("Fail to parse datatype '%s'", datatypeClassName), e);
                }
            }
        } catch (DocumentException e) {
            log.error("Fail to parse datatype settings", e);
            throw new RestAPIException("Fail to parse datatype settings", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return jsonArray.toString();
    }
}
