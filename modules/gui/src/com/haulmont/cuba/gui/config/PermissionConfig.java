/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.03.2009 15:51:46
 *
 * $Id$
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.ConfigurationResourceLoader;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.entity.ui.AttributeTarget;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;
import com.haulmont.cuba.security.entity.ui.MultiplePermissionTarget;
import com.haulmont.cuba.security.entity.ui.OperationPermissionTarget;
import com.haulmont.cuba.security.global.UserSession;
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
            return MessageProvider.getMessage(messagePack, key, locale);
        }

        private void compileScreens() {
            Node<BasicPermissionTarget> root = new Node<BasicPermissionTarget>(
                    new BasicPermissionTarget("menu", getMessage("permissionConfig.screenRoot"), null)
            );
            screens = new Tree<BasicPermissionTarget>(root);

            walkMenu(root);
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
                Node<BasicPermissionTarget> n = new Node<BasicPermissionTarget>(new BasicPermissionTarget("category:" + id, caption, UserSession.getScreenPermissionTarget(clientType, id)));
                node.addChild(n);
                for (MenuItem item : info.getChildren()) {
                    walkMenu(item, n);
                }
            } else {
                if (!"-".equals(info.getId())) {
                    Node<BasicPermissionTarget> n = new Node<BasicPermissionTarget>(
                            new BasicPermissionTarget("item:" + id, caption, UserSession.getScreenPermissionTarget(clientType, id)));
                    node.addChild(n);
                }
            }
        }

        private void compileEntitiesAndAttributes() {
            entities = new ArrayList<OperationPermissionTarget>();
            entityAttributes = new ArrayList<MultiplePermissionTarget>();

            Session session = MetadataProvider.getSession();
            List<MetaModel> modelList = new ArrayList<MetaModel>(session.getModels());
            Collections.sort(modelList, new MetadataObjectAlphabetComparator());

            for (MetaModel model : modelList) {

                List<MetaClass> classList = new ArrayList<MetaClass>(model.getClasses());
                Collections.sort(classList, new MetadataObjectAlphabetComparator());

                for (MetaClass metaClass : classList) {
                    String name = metaClass.getName();
                    // Filter base entity classes
                    if (name.contains("$")) {
                        // Entity target
                        entities.add(new OperationPermissionTarget(metaClass.getJavaClass(), "entity:" + name, name, name));

                        // Target with entity attributes
                        MultiplePermissionTarget attrs = new MultiplePermissionTarget("entity:" + name, name, name);

                        List<MetaProperty> propertyList = new ArrayList<MetaProperty>(metaClass.getProperties());
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
            Node<BasicPermissionTarget> root = new Node<BasicPermissionTarget>(
                    new BasicPermissionTarget("specific", getMessage("permissionConfig.specificRoot"), null));
            specific = new Tree<BasicPermissionTarget>(root);

            final String configName = AppContext.getProperty(PERMISSION_CONFIG_XML_PROP);
            ConfigurationResourceLoader resourceLoader = new ConfigurationResourceLoader();
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String location : tokenizer.getTokenArray()) {
                Resource resource = resourceLoader.getResource(location);
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
                    String incXml = ScriptingProvider.getResourceAsString(fileName);
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
                    Node<BasicPermissionTarget> n = new Node<BasicPermissionTarget>(
                            new BasicPermissionTarget("category:" + id, caption, null));
                    node.addChild(n);
                    walkSpecific(elem, n);
                } else if ("permission".equals(elem.getName())) {
                    Node<BasicPermissionTarget> n = new Node<BasicPermissionTarget>(
                            new BasicPermissionTarget("permission:" + id, caption, id));
                    node.addChild(n);
                }
            }
        }
    }

    @Inject
    private MenuConfig menuConfig;

    private ClientType clientType;
    private String messagePack;

    private List<Item> items = new ArrayList<Item>();

    private Log log = LogFactory.getLog(PermissionConfig.class);

    public PermissionConfig() {
        this.clientType = AppConfig.getClientType();
        this.messagePack = AppConfig.getMessagesPack();
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
