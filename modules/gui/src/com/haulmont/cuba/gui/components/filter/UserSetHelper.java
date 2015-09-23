/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.gui.components.filter.condition.ConditionType;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
* @author devyatkin
* @version $Id$
*/
public class UserSetHelper {
    public static String generateSetFilter(Set ids, String entityClass, String componentId, String entityAlias) {
        Document document = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("filter");
        Element or = root.addElement("and");

        Element condition = or.addElement("c");
        condition.addAttribute("name", "set");
        condition.addAttribute("inExpr", "true");
        condition.addAttribute("hidden", "true");
        condition.addAttribute("locCaption", "Set filter");
        condition.addAttribute("entityAlias", entityAlias);
        condition.addAttribute("class", entityClass);
        condition.addAttribute("type", ConditionType.CUSTOM.name());

        String listOfId = createIdsString(ids);
        String randomName = RandomStringUtils.randomAlphabetic(10);
        condition.addText(entityAlias + ".id in (:component$" + componentId + "." + randomName + ")");

        Element param = condition.addElement("param");
        param.addAttribute("name", "component$" + componentId + "." + randomName);
        param.addText(listOfId);

        document.add(root);
        return Dom4j.writeDocument(document, true);
    }

    public static Set<String> parseSet(String text) {
        Set<String> set = new HashSet<>();
        if ("NULL".equals(StringUtils.trimToEmpty(text)))
            return set;
        String[] ids = text.split(",");
        for (String id : ids) {
            String s = StringUtils.trimToNull(id);
            if (s != null)
                set.add(s);
        }
        return set;
    }

    public static String createIdsString(Set entities) {
        return createIdsString(new HashSet<>(), entities);
    }

    public static String createIdsString(Set<String> current, Collection entities) {
        Set<String> convertedSet = new HashSet<>();
        for (Object entity : entities) {
            convertedSet.add(((BaseUuidEntity) entity).getId().toString());
        }
        current.addAll(convertedSet);
        if (current.isEmpty()) {
            return "NULL";
        }
        StringBuilder listOfId = new StringBuilder();
        Iterator it = current.iterator();
        while (it.hasNext()) {
            listOfId.append(it.next());
            if (it.hasNext()) {
                listOfId.append(',');
            }
        }
        return listOfId.toString();
    }

    public static String removeIds(Set<String> current, Collection entities) {
        Set<String> convertedSet = new HashSet<>();
        for (Object entity : entities) {
            convertedSet.add(((BaseUuidEntity) entity).getId().toString());
        }
        current.removeAll(convertedSet);
        if (current.isEmpty()) {
            return "NULL";
        }
        StringBuilder listOfId = new StringBuilder();
        Iterator it = current.iterator();
        while (it.hasNext()) {
            listOfId.append(it.next());
            if (it.hasNext()) {
                listOfId.append(',');
            }
        }
        return listOfId.toString();
    }

    public static String removeEntities(String filterXml, Collection ids) {
        Document document;
        try {
            document = DocumentHelper.parseText(filterXml);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element param = document.getRootElement().element("and").element("c").element("param");
        String currentIds = param.getTextTrim();
        Set<String> set = parseSet(currentIds);
        String listOfIds = removeIds(set, ids);
        param.setText(listOfIds);
        return document.asXML();
    }

    public static String addEntities(String filterXml, Collection ids) {
        Document document;
        try {
            document = DocumentHelper.parseText(filterXml);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element param = document.getRootElement().element("and").element("c").element("param");
        String currentIds = param.getTextTrim();
        Set<String> set = parseSet(currentIds);
        String listOfIds = createIdsString(set, ids);
        param.setText(listOfIds);
        return document.asXML();
    }
}