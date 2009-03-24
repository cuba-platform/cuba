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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.cuba.core.app.ResourceRepositoryService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.*;

public class PermissionConfig {
     @com.haulmont.chile.core.annotations.MetaClass(name = "sec$Target")
     public static class Target
         extends
             AbstractInstance
         implements
             Entity<String>
     {
         protected static transient MethodsCache __cache = new MethodsCache(Target.class);

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

         protected MethodsCache getMethodsCache() {
             return __cache;  
         }

         public UUID getUuid() {
             return uuid;
         }

         public MetaClass getMetaClass() {
             return MetadataProvider.getSession().getClass(getClass());
         }
     }

     private ResourceRepositoryService repository;
     private ClientType clientType;
     private String messagePack;
     private Locale locale;

     private Tree<Target> screens;
     private Tree<Target> entities;
     private Tree<Target> specific;

     public static String getScreenPermissionTarget(ClientType clientType, String screenId) {
         return clientType.getId() + ":" + screenId;
     }

     public PermissionConfig() {
         this.repository = ServiceLocator.lookup(ResourceRepositoryService.JNDI_NAME);
         this.clientType = AppConfig.getInstance().getClientType();
         this.messagePack = AppConfig.getInstance().getMessagesPack();
         this.locale = Locale.getDefault();
     }

     public void compile() {
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
         String caption = getMessage("menu-config." + id);
         if (info.getChildren() != null && !info.getChildren().isEmpty()) {
             Node<Target> n = new Node<Target>(new Target("category:" + id, caption, null));
             node.addChild(n);
             for (MenuItem item : info.getChildren()) {
                walkMenu(item, n);
             }
         } else {
             Node<Target> n = new Node<Target>(new Target("item:" + id, caption, getScreenPermissionTarget(clientType, id)));
             node.addChild(n);
         }
     }

     private void compileEntities() {
         Node<Target> root = new Node<Target>(new Target("session", getMessage("permissionConfig.entityRoot"), null));
         entities = new Tree<Target>(root);

         Session session = MetadataProvider.getSession();
         for (MetaModel model : session.getModels()) {
             Node<Target> modelNode = new Node<Target>(new Target("model:" + model.getName(), model.getName(), null));
             root.addChild(modelNode);
             for (MetaClass metaClass : model.getClasses()) {
                 String name = metaClass.getName();
                 if (name.contains("$")) {
                     Node<Target> node = new Node<Target>(new Target("entity:" + name, name, name));
                     modelNode.addChild(node);
                 }
             }
         }
     }

     private void compileSpecific() {
         Node<Target> root = new Node<Target>(new Target("specific", getMessage("permissionConfig.specificRoot"), null));
         specific = new Tree<Target>(root);

         final String configPath = System.getProperty(AppConfig.PERMISSION_CONFIG_XML_PROP);
         String xml = repository.getResAsString(configPath);
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

     public Tree<Target> getScreens() {
         return screens;
     }

     public Tree<Target> getEntities() {
         return entities;
     }

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

    public List<Target> getEntityAttributes(Target entityTarget) {
        if (entityTarget == null) return Collections.emptyList();

        final String id = entityTarget.getId();
        if (!id.startsWith("entity:")) return Collections.emptyList();

        MetaClass metaClass = MetadataProvider.getSession().getClass(id.substring("entity:".length()));
        if (metaClass == null)  return Collections.emptyList();

        final String value = entityTarget.getValue();

        List<Target> result = new ArrayList<Target>();
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            result.add(new Target(id + ":" + metaProperty.getName(), metaProperty.getName(), value + ":" + metaProperty.getName()));
        }

        return result;
    }

     public Tree<Target> getSpecific() {
         return specific;
     }
 }
