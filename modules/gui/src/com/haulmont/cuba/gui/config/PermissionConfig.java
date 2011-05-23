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
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.ConfigurationResourceLoader;
import com.haulmont.cuba.gui.AppConfig;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * GenericUI class holding information about all permission targets.
 * <br>Reference can be obtained via {@link com.haulmont.cuba.gui.AppConfig#getPermissionConfig(java.util.Locale)}
 */
public class PermissionConfig {

    /**
     * Non-persistent entity to show permission targets in UI
     */
    @com.haulmont.chile.core.annotations.MetaClass(name = "sec$Target")
    public static class Target
            extends AbstractInstance
            implements Entity<String>
    {
        @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
        private String id;
        @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
        private String caption;
        @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
        private String value;

        private UUID uuid = UUID.randomUUID();

        public Target(String id, String caption, String value) {
            this.caption = caption;
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return caption;
        }

        public UUID getUuid() {
            return uuid;
        }

        public MetaClass getMetaClass() {
            return MetadataProvider.getSession().getClass(getClass());
        }
    }

    private ClientType clientType;
    private String messagePack;
    private Locale locale;

    private Tree<Target> screens;
    private Tree<Target> entities;
    private Tree<Target> specific;

    private Log log = LogFactory.getLog(PermissionConfig.class);

    public PermissionConfig() {
        this.clientType = AppConfig.getInstance().getClientType();
        this.messagePack = AppConfig.getInstance().getMessagesPack();
    }

    public void compile(Locale locale) {
        this.locale = locale;
        compileScreens();
        compileEntities();
        compileSpecific();
    }

    private void compileScreens() {
        Node<Target> root = new Node<Target>(new Target("menu", getMessage("permissionConfig.screenRoot"), null));
        screens = new Tree<Target>(root);

        final MenuConfig config = AppConfig.getInstance().getMenuConfig();
        walkMenu(config, root);
    }

    private void walkMenu(MenuConfig config, Node<Target> node) {
        for (MenuItem info : config.getRootItems()) {
            walkMenu(info, node);
        }
    }

    private void walkMenu(MenuItem info, Node<Target> node) {
        String id = info.getId();
        String caption = MenuConfig.getMenuItemCaption(id).replaceAll("<.+?>", "");
        caption = StringEscapeUtils.unescapeHtml(caption);

        if (info.getChildren() != null && !info.getChildren().isEmpty()) {
            Node<Target> n = new Node<Target>(new Target("category:" + id, caption, UserSession.getScreenPermissionTarget(clientType, id)));
            node.addChild(n);
            for (MenuItem item : info.getChildren()) {
                walkMenu(item, n);
            }
        } else {
            if (!"-".equals(info.getId())) {
                Node<Target> n = new Node<Target>(
                        new Target("item:" + id, caption, UserSession.getScreenPermissionTarget(clientType, id)));
                node.addChild(n);
            }
        }
    }

    private void compileEntities() {
        Node<Target> root = new Node<Target>(new Target("session", getMessage("permissionConfig.entityRoot"), null));
        entities = new Tree<Target>(root);

        Session session = MetadataProvider.getSession();
        List<MetaModel> modelList = new ArrayList<MetaModel>(session.getModels());
        Collections.sort(modelList, new MetadataObjectAlphabetComparator());

        for (MetaModel model: modelList) {
            Node<Target> modelNode = new Node<Target>(new Target("model:" + model.getName(), model.getName(), null));
            root.addChild(modelNode);

            List<MetaClass> classList = new ArrayList<MetaClass>(model.getClasses());
            Collections.sort(classList, new MetadataObjectAlphabetComparator());

            for (MetaClass metaClass: classList) {
                String name = metaClass.getName();
                if (name.contains("$")) {
                    String caption = name + " (" + MessageUtils.getEntityCaption(metaClass) + ")";
                    Node<Target> node = new Node<Target>(new Target("entity:" + name, caption, name));
                    modelNode.addChild(node);
                }
            }
        }
    }

    private void compileSpecific() {
        Node<Target> root = new Node<Target>(new Target("specific", getMessage("permissionConfig.specificRoot"), null));
        specific = new Tree<Target>(root);

        final String configName = AppContext.getProperty(AppConfig.PERMISSION_CONFIG_XML_PROP);
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

    private void compileSpecific(String xml, Node<Target> root) {
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

    private void walkSpecific(Element element, Node<Target> node) {
        //noinspection unchecked
        for (Element elem : (List<Element>) element.elements()) {
            String id = elem.attributeValue("id");
            String caption = getMessage("permission-config." + id);
            if ("category".equals(elem.getName())) {
                Node<Target> n = new Node<Target>(new Target("category:" + id, caption, null));
                node.addChild(n);
                walkSpecific(elem, n);
            } else if ("permission".equals(elem.getName())) {
                Node<Target> n = new Node<Target>(new Target("permission:" + id, caption, id));
                node.addChild(n);
            }
        }
    }

    private String getMessage(String key) {
        return MessageProvider.getMessage(messagePack, key, locale);
    }

    /**
     * All registered screens
     */
    public Tree<Target> getScreens() {
        return screens;
    }

    /**
     * All registered entities
     */
    public Tree<Target> getEntities() {
        return entities;
    }

    /**
     * Entity operations for specified target
     */
    public List<Target> getEntityOperations(Target entityTarget) {
        if (entityTarget == null) return Collections.emptyList();

        final String id = entityTarget.getId();
        if (!id.startsWith("entity:")) return Collections.emptyList();

        MetaClass metaClass = MetadataProvider.getSession().getClass(id.substring("entity:".length()));
        if (metaClass == null)  return Collections.emptyList();

        List<Target> result = new ArrayList<Target>();
        final String value = entityTarget.getValue();

        result.add(new Target(id + ":create", "create", value + ":create"));
        result.add(new Target(id + ":read", "read", value + ":read"));
        result.add(new Target(id + ":delete", "delete", value + ":delete"));

        Class javaClass = metaClass.getJavaClass();
        if (Updatable.class.isAssignableFrom(javaClass)) {
            result.add(new Target(id + ":update", "update", value + ":update"));
        }

        return result;
    }

    /**
     * Entity attributes for specified target
     */
    public List<Target> getEntityAttributes(Target entityTarget) {
        if (entityTarget == null) return Collections.emptyList();

        final String id = entityTarget.getId();
        if (!id.startsWith("entity:")) return Collections.emptyList();

        MetaClass metaClass = MetadataProvider.getSession().getClass(id.substring("entity:".length()));
        if (metaClass == null)  return Collections.emptyList();

        final String value = entityTarget.getValue();

        List<MetaProperty> propertyList = new ArrayList<MetaProperty>(metaClass.getProperties());
        Collections.sort(propertyList, new MetadataObjectAlphabetComparator());

        List<Target> result = new ArrayList<Target>();
        for (MetaProperty metaProperty : propertyList) {
            String caption = metaProperty.getName() + " (" + MessageUtils.getPropertyCaption(metaProperty) + ")";
            result.add(new Target(id + ":" + metaProperty.getName(), caption, value + ":" + metaProperty.getName()));
        }

        return result;
    }

    /**
     * All specific permissions
     */
    public Tree<Target> getSpecific() {
        return specific;
    }

    private class MetadataObjectAlphabetComparator implements Comparator<MetadataObject> {
        public int compare(MetadataObject o1, MetadataObject o2) {
            String n1 = o1 != null ? o1.getName() : null;
            String n2 = o2 != null ? o2.getName() : null;

            return n1 != null ? n1.compareTo(n2) : -1;
        }
    }
}
