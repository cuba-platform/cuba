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

import com.haulmont.cuba.core.app.ResourceRepositoryService;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.bali.datastruct.Node;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.io.StringReader;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

public class PermissionConfig
{
    public static class Target
    {
        private String id;
        private String caption;

        public Target(String id, String caption) {
            this.caption = caption;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public String toString() {
            return caption;
        }
    }

    private ResourceRepositoryService rr;
    private String configRoot;
    private ClientType clientType;
    private String messagePack;
    private Locale locale;

    private Tree<Target> screens;
    private Tree<Target> entities;
    private Tree<Target> specific;

    public static String getScreenPermissionTarget(ClientType clientType, String screenId) {
        return clientType.getId() + ":" + screenId;
    }

    public PermissionConfig(ResourceRepositoryService rr,
                            String configRoot,
                            ClientType clientType,
                            String messagePack,
                            Locale locale)
    {
        this.rr = rr;
        this.configRoot = configRoot;
        this.clientType = clientType;
        this.messagePack = messagePack;
        this.locale = locale;
    }

    public void compile() {
        compileScreens();
        compileEntities();
        compileSpecific();
    }

    private void compileScreens() {
        Node<Target> root = new Node<Target>(new Target(null, getMessage("permissionConfig.screenRoot")));
        screens = new Tree<Target>(root);

        String xml = rr.getResAsString(configRoot + "/client/" + clientType.getConfigPath() + "/menu-config.xml");
        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(new StringReader(xml));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElem = doc.getRootElement();
        walkMenu(rootElem, root);
    }

    private void walkMenu(Element element, Node<Target> node) {
        for (Element elem : (List<Element>) element.elements()) {
            String id = elem.attributeValue("id");
            String caption = getMessage("menu-config." + id);
            if ("menu".equals(elem.getName())) {
                Node<Target> n = new Node<Target>(new Target(null, caption));
                node.addChild(n);
                walkMenu(elem, n);
            }
            else if ("item".equals(elem.getName())) {
                Node<Target> n = new Node<Target>(new Target(getScreenPermissionTarget(clientType, id), caption));
                node.addChild(n);
            }
        }
    }

    private void compileEntities() {
        Node<Target> root = new Node<Target>(new Target(null, getMessage("permissionConfig.entityRoot")));
        entities = new Tree<Target>(root);

        Session session = MetadataProvider.getSession();
        for (MetaModel model : session.getModels()) {
            Node<Target> modelNode = new Node<Target>(new Target(null, model.getName()));
            root.addChild(modelNode);
            for (MetaClass metaClass : model.getClasses()) {
                String name = metaClass.getName();
                if (name.contains("$")) {
                    Node<Target> node = new Node<Target>(new Target(name, metaClass.getName()));
                    modelNode.addChild(node);
                }
            }
        }
    }

    private void compileSpecific() {
        Node<Target> root = new Node<Target>(new Target(null, getMessage("permissionConfig.specificRoot")));
        specific = new Tree<Target>(root);

        String xml = rr.getResAsString(configRoot + "/permission-config.xml");
        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(new StringReader(xml));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElem = doc.getRootElement().element("specific");
        if (rootElem == null)
            return;

        walkSpecific(rootElem, root);
    }

    private void walkSpecific(Element element, Node<Target> node) {
        for (Element elem : (List<Element>) element.elements()) {
            String id = elem.attributeValue("id");
            String caption = getMessage("permission-config." + id);
            if ("category".equals(elem.getName())) {
                Node<Target> n = new Node<Target>(new Target(null, caption));
                node.addChild(n);
                walkSpecific(elem, n);
            }
            else if ("permission".equals(elem.getName())) {
                Node<Target> n = new Node<Target>(new Target(id, caption));
                node.addChild(n);
            }
        }
    }

    private String getMessage(String key) {
        return MessageProvider.getMessage(messagePack, key, locale);
    }

    public Tree<Target> getScreens() {
        return screens;
    }

    public Tree<Target> getEntities() {
        return entities;
    }

    public List<Target> getEntityOperations(Target entityTarget) {
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityTarget.getId());
        List<Target> result = new ArrayList<Target>();

        result.add(new Target(entityTarget.getId() + ":create", "create"));
        result.add(new Target(entityTarget.getId() + ":read", "read"));
        result.add(new Target(entityTarget.getId() + ":delete", "delete"));

        Class javaClass = metaClass.getJavaClass();
        if (Updatable.class.isAssignableFrom(javaClass)) {
            result.add(new Target(entityTarget.getId() + ":update", "update"));
        }

        return result;
    }

    public List<Target> getEntityAttributes(Target entityTarget) {
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityTarget.getId());
        List<Target> result = new ArrayList<Target>();
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            result.add(new Target(entityTarget.getId() + ":" + metaProperty.getName(), metaProperty.getName()));
        }
        return result;
    }

    public Tree<Target> getSpecific() {
        return specific;
    }
}
