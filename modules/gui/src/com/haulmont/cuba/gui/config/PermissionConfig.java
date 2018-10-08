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
package com.haulmont.cuba.gui.config;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.entity.AttributeTarget;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.OperationPermissionTarget;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.MappedSuperclass;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GenericUI class holding information about all permission targets.
 */
@Component("cuba_PermissionConfig")
public class PermissionConfig {

    private final Logger log = LoggerFactory.getLogger(PermissionConfig.class);

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected MetadataTools metadataTools;

    public static final String PERMISSION_CONFIG_XML_PROP = "cuba.permissionConfig";

    private class Item {
        private Locale locale;

        private Tree<BasicPermissionTarget> screens;
        private Tree<BasicPermissionTarget> specific;

        private List<OperationPermissionTarget> entities;
        private List<MultiplePermissionTarget> entityAttributes;

        private Item(Locale locale) {
            this.locale = locale;

            compileScreens();
            compileEntitiesAndAttributes();
            compileSpecific();
        }

        private String getMessage(String key) {
            return messages.getMainMessage(key, locale);
        }

        private void compileScreens() {
            Node<BasicPermissionTarget> menuRoot = new Node<>(
                    new BasicPermissionTarget("root:menu", getMessage("permissionConfig.mainMenu"), null)
            );
            walkMenu(menuRoot);

            Node<BasicPermissionTarget> othersRoot = new Node<>(
                    new BasicPermissionTarget("root:others", getMessage("permissionConfig.otherScreens"), null)
            );
            walkOtherScreens(othersRoot, menuRoot);

            screens = new Tree<>(Arrays.asList(menuRoot, othersRoot));
        }

        private void walkMenu(Node<BasicPermissionTarget> node) {
            for (MenuItem info : menuConfig.getRootItems()) {
                walkMenu(info, node);
            }
        }

        private void walkMenu(MenuItem info, Node<BasicPermissionTarget> node) {
            String id = info.getId();
            String caption = menuConfig.getItemCaption(id)
                    .replaceAll("<.+?>", "")
                    .replaceAll("&gt;", "");
            caption = StringEscapeUtils.unescapeHtml4(caption);

            if (info.getChildren() != null && !info.getChildren().isEmpty()) {
                Node<BasicPermissionTarget> n = new Node<>(new BasicPermissionTarget("category:" + id, caption, id));
                node.addChild(n);
                for (MenuItem item : info.getChildren()) {
                    walkMenu(item, n);
                }
            } else {
                if (!info.isSeparator()) {
                    Node<BasicPermissionTarget> n = new Node<>(new BasicPermissionTarget("item:" + id, caption, id));
                    node.addChild(n);
                }
            }
        }

        private void walkOtherScreens(Node<BasicPermissionTarget> othersRoot, Node<BasicPermissionTarget> menuRoot) {
            Set<String> menuItems = new HashSet<>();
            for (Node<BasicPermissionTarget> node : new Tree<>(menuRoot).toList()) {
                menuItems.add(node.getData().getId());
            }

            // filter non-unique windows with specified agent
            windowConfig.getWindows().stream()
                    .map(WindowInfo::getId)
                    .filter(id -> !menuItems.contains("item:" + id))
                    .distinct()
                    .sorted()
                    .forEach(id -> {
                        Node<BasicPermissionTarget> n = new Node<>(new BasicPermissionTarget("item:" + id, id, id));
                        othersRoot.addChild(n);
                    });
        }

        private void compileEntitiesAndAttributes() {
            entities = new ArrayList<>();
            entityAttributes = new ArrayList<>();

            Session session = metadata.getSession();
            List<MetaModel> modelList = new ArrayList<>(session.getModels());
            modelList.sort(new MetadataObjectAlphabetComparator());

            for (MetaModel model : modelList) {

                List<MetaClass> classList = new ArrayList<>(model.getClasses());
                classList.sort(new MetadataObjectAlphabetComparator());

                for (MetaClass metaClass : classList) {
                    String name = metaClass.getName();
                    // Filter base classes
                    if (!metaClass.getJavaClass().isAnnotationPresent(MappedSuperclass.class)) {
                        // Skip classes that have extensions
                        if (metadata.getExtendedEntities().getExtendedClass(metaClass) != null) {
                            continue;
                        }

                        // For extended entities use original metaclass name
                        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                        String entityName = originalMetaClass == null ? name : originalMetaClass.getName();

                        String caption = messages.getTools().getDetailedEntityCaption(metaClass, locale);

                        // Entity target
                        entities.add(new OperationPermissionTarget(metaClass.getJavaClass(),
                                "entity:" + entityName, caption, entityName));

                        // Target with entity attributes
                        MultiplePermissionTarget attrs = new MultiplePermissionTarget(metaClass.getJavaClass(),
                                "entity:" + entityName, caption, entityName);

                        List<MetaProperty> propertyList = new ArrayList<>(metaClass.getProperties());
                        Collection<CategoryAttribute> dynamicAttributes =
                                PermissionConfig.this.dynamicAttributes.getAttributesForMetaClass(metaClass);

                        for (CategoryAttribute dynamicAttribute : dynamicAttributes) {
                            MetaPropertyPath metaPropertyPath =
                                    metadataTools.resolveMetaPropertyPathNN(metaClass,
                                            DynamicAttributesUtils.encodeAttributeCode(dynamicAttribute.getCode()));
                            propertyList.add(metaPropertyPath.getMetaProperty());
                        }
                        propertyList.sort(new MetadataObjectAlphabetComparator());

                        for (MetaProperty metaProperty : propertyList) {
                            String metaPropertyName = metaProperty.getName();
                            attrs.getPermissions().add(new AttributeTarget(metaPropertyName));
                        }
                        entityAttributes.add(attrs);
                    }
                }
            }
        }

        private void compileSpecific() {
            Node<BasicPermissionTarget> root = new Node<>(
                    new BasicPermissionTarget("category:specific", getMessage("permissionConfig.specificRoot"), null));
            specific = new Tree<>(root);

            final String configName = AppContext.getProperty(PERMISSION_CONFIG_XML_PROP);
            StringTokenizer tokenizer = new StringTokenizer(configName);
            for (String location : tokenizer.getTokenArray()) {
                Resource resource = resources.getResource(location);
                if (resource.exists()) {
                    InputStream stream = null;
                    try {
                        stream = resource.getInputStream();
                        String xml = IOUtils.toString(stream, StandardCharsets.UTF_8);
                        compileSpecific(xml, root);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to read permission config", e);
                    } finally {
                        IOUtils.closeQuietly(stream);
                    }
                } else {
                    log.warn("Resource {} not found, ignore it", location);
                }
            }
        }

        private void compileSpecific(String xml, Node<BasicPermissionTarget> root) {
            Document doc = Dom4j.readDocument(xml);
            Element rootElem = doc.getRootElement();

            for (Element element : Dom4j.elements(rootElem, "include")) {
                String fileName = element.attributeValue("file");
                if (!StringUtils.isBlank(fileName)) {
                    String incXml = resources.getResourceAsString(fileName);
                    if (incXml == null)
                        throw new RuntimeException("Config file not found: " + fileName);

                    compileSpecific(incXml, root);
                }
            }

            Element specElem = rootElem.element("specific");
            if (specElem == null)
                return;

            walkSpecific(specElem, root);
        }

        private void walkSpecific(Element element, Node<BasicPermissionTarget> node) {
            //noinspection unchecked
            for (Element elem : (List<Element>) element.elements()) {
                String id = elem.attributeValue("id");
                String caption = getMessage("permission-config." + id);
                if ("category".equals(elem.getName())) {
                    Node<BasicPermissionTarget> existingCategory = null;
                    String categoryPermissionId = "category:" + id;
                    for (Node<BasicPermissionTarget> subNode : node.getChildren()) {
                        if (categoryPermissionId.equals(subNode.getData().getId())) {
                            existingCategory = subNode;
                            break;
                        }
                    }

                    Node<BasicPermissionTarget> categoryNode;
                    if (existingCategory == null) {
                        categoryNode = new Node<>(new BasicPermissionTarget(categoryPermissionId, caption, null));
                        node.addChild(categoryNode);
                    } else {
                        categoryNode = existingCategory;
                    }

                    walkSpecific(elem, categoryNode);
                } else if ("permission".equals(elem.getName())) {
                    Node<BasicPermissionTarget> n = new Node<>(
                            new BasicPermissionTarget("permission:" + id, caption, id));
                    node.addChild(n);
                }
            }
        }
    }

    @Inject
    private MenuConfig menuConfig;

    @Inject
    private WindowConfig windowConfig;

    @Inject
    private Resources resources;

    @Inject
    private Messages messages;

    @Inject
    private Metadata metadata;

    private ClientType clientType;

    private List<Item> items = new CopyOnWriteArrayList<>();

    public PermissionConfig() {
        this.clientType = AppConfig.getClientType();
    }

    public ClientType getClientType() {
        return clientType;
    }

    private Item getItem(Locale locale) {
        for (Item item : items) {
            if (item.locale.equals(locale))
                return item;
        }
        Item item = new Item(locale);
        items.add(item);
        return item;
    }

    /**
     * All registered screens
     *
     * @param locale Locale
     * @return Tree with screen targets
     */
    public Tree<BasicPermissionTarget> getScreens(Locale locale) {
        return getItem(locale).screens;
    }

    /**
     * All registered entities
     *
     * @param locale Locale
     * @return List of entity targets
     */
    public List<OperationPermissionTarget> getEntities(Locale locale) {
        return getItem(locale).entities;
    }

    /**
     * All registered entities with attributes
     *
     * @param locale Locale
     * @return List of attribute targets
     */
    public List<MultiplePermissionTarget> getEntityAttributes(Locale locale) {
        return getItem(locale).entityAttributes;
    }

    /**
     * All specific permissions
     *
     * @param locale Locale
     * @return Tree with specific targets
     */
    public Tree<BasicPermissionTarget> getSpecific(Locale locale) {
        return getItem(locale).specific;
    }

    public void clearConfigCache() {
        items.clear();
    }

    protected static class MetadataObjectAlphabetComparator implements Comparator<MetadataObject> {
        @Override
        public int compare(MetadataObject o1, MetadataObject o2) {
            String n1 = o1 != null ? o1.getName() : null;
            String n2 = o2 != null ? o2.getName() : null;

            return n1 != null ? n1.compareTo(n2) : -1;
        }
    }
}