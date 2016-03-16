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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.ConfigurationResourceLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.persistence.Entity;
import java.io.*;
import java.util.*;

/**
 * Generates a working persistence.xml file combining classes and properties from a set of given persistence.xml files,
 * defined in <code>cuba.persistenceConfig</code> app property.
 *
 */
public class PersistenceConfigProcessor {

    private String baseDir;
    private List<String> sourceFileNames;
    private String outFileName;

    private Logger log = LoggerFactory.getLogger(getClass());

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void setSourceFiles(List<String> files) {
        sourceFileNames = files;
    }

    public void setOutputFile(String file) {
        outFileName = file;
    }

    public void create() {
        if (sourceFileNames == null || sourceFileNames.isEmpty())
            throw new IllegalStateException("Source file list not set");
        if (StringUtils.isBlank(outFileName))
            throw new IllegalStateException("Output file not set");

        Map<String, String> classes = new LinkedHashMap<>();
        Map<String, String> properties = new HashMap<>();

        properties.putAll(DbmsSpecificFactory.getDbmsFeatures().getJpaParameters());

        for (String fileName : sourceFileNames) {
            Document doc = getDocument(fileName);
            Element puElem = findPersistenceUnitElement(doc.getRootElement());
            if (puElem == null)
                throw new IllegalStateException("No persistence unit named 'cuba' found among multiple units inside " + fileName);
            addClasses(puElem, classes);
            addProperties(puElem, properties);
        }

        for (String name : AppContext.getPropertyNames()) {
            if (name.startsWith("eclipselink.")) {
                properties.put(name, AppContext.getProperty(name));
            }
        }

        File outFile;
        try {
            outFile = new File(outFileName).getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        outFile.getParentFile().mkdirs();

        boolean ormXmlCreated = true;
        String disableOrmGenProp = AppContext.getProperty("cuba.disableOrmXmlGeneration");
        if (!Boolean.parseBoolean(disableOrmGenProp)) {
            MappingFileCreator mappingFileCreator = new MappingFileCreator(classes.values(), properties, outFile.getParentFile());
            ormXmlCreated = mappingFileCreator.create();
        }

        String fileName = sourceFileNames.get(sourceFileNames.size() - 1);
        Document doc = getDocument(fileName);
        Element rootElem = doc.getRootElement();

        Element puElem = findPersistenceUnitElement(rootElem);
        if (puElem == null)
            throw new IllegalStateException("No persistence unit named 'cuba' found among multiple units inside " + fileName);

        String puName = AppContext.getProperty("cuba.persistenceUnitName");
        if (!StringUtils.isEmpty(puName)) {
            puElem.addAttribute("name", puName);
        }

        for (Element element : new ArrayList<>(Dom4j.elements(puElem, "class"))) {
            puElem.remove(element);
        }

        puElem.addElement("provider").setText("org.eclipse.persistence.jpa.PersistenceProvider");

        if (ormXmlCreated) {
            puElem.addElement("mapping-file").setText("orm.xml");
        }

        for (String className : classes.values()) {
            puElem.addElement("class").setText(className);
        }

        puElem.addElement("exclude-unlisted-classes");

        Element propertiesEl = puElem.element("properties");
        if (propertiesEl != null)
            puElem.remove(propertiesEl);

        propertiesEl = puElem.addElement("properties");
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            Element element = propertiesEl.addElement("property");
            element.addAttribute("name", entry.getKey());
            element.addAttribute("value", entry.getValue());
        }

        log.info("Creating file " + outFile);
        OutputStream os = null;
        try {
            os = new FileOutputStream(outFileName);
            Dom4j.writeDocument(doc, true, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private void addClasses(Element puElem, Map<String, String> classes) {
        for (Element element : Dom4j.elements(puElem, "class")) {
            String className = element.getText();
            Class<Object> cls = ReflectionHelper.getClass(className);
            Entity annotation = cls.getAnnotation(Entity.class);
            if (annotation != null) {
                classes.put(annotation.name(), className);
            } else {
                classes.put(className, className);
            }
        }
    }

    private void addProperties(Element puElem, Map<String, String> properties) {
        Element propertiesEl = puElem.element("properties");
        if (propertiesEl != null) {
            for (Element element : Dom4j.elements(propertiesEl, "property")) {
                properties.put(element.attributeValue("name"), element.attributeValue("value"));
            }
        }
    }

    private Element findPersistenceUnitElement(Element rootElem) {
        List<Element> puList = Dom4j.elements(rootElem, "persistence-unit");
        if (puList.size() == 1) {
            return puList.get(0);
        } else {
            for (Element element : puList) {
                if ("cuba".equals(element.attributeValue("name"))) {
                    return element;
                }
            }
        }
        return null;
    }

    private Document getDocument(String fileName) {
        Document doc;
        if (baseDir == null) {
            Resource resource = new ConfigurationResourceLoader().getResource(fileName);
            InputStream stream = null;
            try {
                stream = resource.getInputStream();
                doc = Dom4j.readDocument(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } else {
            if (!fileName.startsWith("/"))
                fileName = "/" + fileName;
            File file = new File(baseDir, fileName);
            if (!file.exists())
                throw new IllegalArgumentException("File not found: " + file.getAbsolutePath());

            doc = Dom4j.readDocument(file);
        }
        return doc;
    }
}
