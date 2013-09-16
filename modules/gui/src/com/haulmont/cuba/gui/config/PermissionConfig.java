/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.security.entity.AttributeTarget;
import com.haulmont.cuba.gui.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.gui.security.entity.OperationPermissionTarget;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * GenericUI class holding information about all permission targets.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_PermissionConfig")
public class PermissionConfig {

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
            return messages.getMessage(messages.getMainMessagePack(), key, locale);
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
            String caption = MenuConfig.getMenuItemCaption(id).replaceAll("<.+?>", "").replaceAll("&gt;", "");
            caption = StringEscapeUtils.unescapeHtml(caption);

            if (info.getChildren() != null && !info.getChildren().isEmpty()) {
                Node<BasicPermissionTarget> n = new Node<>(new BasicPermissionTarget("category:" + id, caption, id));
                node.addChild(n);
                for (MenuItem item : info.getChildren()) {
                    walkMenu(item, n);
                }
            } else {
                if (!"-".equals(info.getId())) {
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

            for (WindowInfo info : windowConfig.getWindows()) {
                String id = info.getId();
                if (!menuItems.contains("item:" + id)) {
                    Node<BasicPermissionTarget> n = new Node<>(new BasicPermissionTarget("item:" + id, id, id));
                    othersRoot.addChild(n);
                }
            }
            Collections.sort(othersRoot.getChildren(), new Comparator<Node<BasicPermissionTarget>>() {
                @Override
                public int compare(Node<BasicPermissionTarget> n1, Node<BasicPermissionTarget> n2) {
                    return n1.getData().getId().compareTo(n2.getData().getId());
                }
            });
        }

        private void compileEntitiesAndAttributes() {
            entities = new ArrayList<>();
            entityAttributes = new ArrayList<>();

            Session session = metadata.getSession();
            List<MetaModel> modelList = new ArrayList<>(session.getModels());
            Collections.sort(modelList, new MetadataObjectAlphabetComparator());

            for (MetaModel model : modelList) {

                List<MetaClass> classList = new ArrayList<>(model.getClasses());
                Collections.sort(classList, new MetadataObjectAlphabetComparator());

                for (MetaClass metaClass : classList) {
                    String name = metaClass.getName();
                    // Filter base entity classes
                    if (name.contains("$")) {
                        // Skip classes that have extensions
                        if (metadata.getExtendedEntities().getExtendedClass(metaClass) != null)
                            continue;

                        // For extended entities use original metaclass name
                        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                        String entityName = originalMetaClass == null ? name : originalMetaClass.getName();

                        // Entity target
                        entities.add(new OperationPermissionTarget(metaClass.getJavaClass(),
                                "entity:" + entityName, name, entityName));

                        // Target with entity attributes
                        MultiplePermissionTarget attrs = new MultiplePermissionTarget(metaClass.getJavaClass(),
                                "entity:" + entityName, name, entityName);

                        List<MetaProperty> propertyList = new ArrayList<>(metaClass.getProperties());
                        Collections.sort(propertyList, new MetadataObjectAlphabetComparator());

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
                    new BasicPermissionTarget("specific", getMessage("permissionConfig.specificRoot"), null));
            specific = new Tree<>(root);

            final String configName = AppContext.getProperty(PERMISSION_CONFIG_XML_PROP);
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String location : tokenizer.getTokenArray()) {
                Resource resource = resources.getResource(location);
                if (resource.exists()) {
                    InputStream stream = null;
                    try {
                        stream = resource.getInputStream();
                        String xml = IOUtils.toString(stream);
                        compileSpecific(xml, root);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        IOUtils.closeQuietly(stream);
                    }
                } else {
                    log.warn("Resource " + location + " not found, ignore it");
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
                    Node<BasicPermissionTarget> n = new Node<>(
                            new BasicPermissionTarget("category:" + id, caption, null));
                    node.addChild(n);
                    walkSpecific(elem, n);
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

    private List<Item> items = new ArrayList<>();

    private Log log = LogFactory.getLog(PermissionConfig.class);

    public PermissionConfig() {
        this.clientType = AppConfig.getClientType();
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

    private class MetadataObjectAlphabetComparator implements Comparator<MetadataObject> {
        @Override
        public int compare(MetadataObject o1, MetadataObject o2) {
            String n1 = o1 != null ? o1.getName() : null;
            String n2 = o2 != null ? o2.getName() : null;

            return n1 != null ? n1.compareTo(n2) : -1;
        }
    }
}
