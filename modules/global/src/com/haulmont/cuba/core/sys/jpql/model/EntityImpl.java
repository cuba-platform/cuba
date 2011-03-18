package com.haulmont.cuba.core.sys.jpql.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 20:41:31
 */
public class EntityImpl implements Entity {
    private String name;
    private List<String> attributeNames = new ArrayList<String>();
    private Map<String, AttributeImpl> name2attribute = new HashMap<String, AttributeImpl>();
    private String userFriendlyName;

    public EntityImpl(String name) {
        if (name == null)
            throw new NullPointerException("No entity name passed");

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUserFriendlyName() {
        return userFriendlyName;
    }

    public void setUserFriendlyName(String userFriendlyName) {
        this.userFriendlyName = userFriendlyName;
    }

    public void addSingleValueAttribute(Class aClass, String name) {
        addSingleValueAttribute(aClass, name, null);
    }

    public void addSingleValueAttribute(Class aClass, String name, String userFriendlyName) {
        if (aClass == null)
            throw new NullPointerException("No attribute type passed");
        if (name == null)
            throw new NullPointerException("No attribute name passed");

        AttributeImpl attribute = new AttributeImpl(aClass, name);
        attribute.setUserFriendlyName(userFriendlyName);
        attributeNames.add(name);
        name2attribute.put(name, attribute);
    }

    public AttributeImpl getAttributeByName(String attributeName) {
        return name2attribute.get(attributeName);
    }

    public List<Attribute> findAttributesStartingWith(String fieldNamePattern) {
        List<Attribute> result = new ArrayList<Attribute>();
        for (Map.Entry<String, AttributeImpl> entry : name2attribute.entrySet()) {
            if (entry.getKey().startsWith(fieldNamePattern))
                result.add(entry.getValue());
        }
        return result;
    }

    public void addReferenceAttribute(String referencedEntityName, String name) {
        addReferenceAttribute(referencedEntityName, name, null);
    }

    public void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
        if (referencedEntityName == null)
            throw new NullPointerException("No referencedEntityName passed");
        if (name == null)
            throw new NullPointerException("No attribute name passed");

        AttributeImpl attribute = new AttributeImpl(referencedEntityName, name, false);
        attribute.setUserFriendlyName(userFriendlyName);
        attributeNames.add(name);
        name2attribute.put(name, attribute);
    }

    public void addCollectionReferenceAttribute(String referencedEntityName, String name) {
        addCollectionReferenceAttribute(referencedEntityName, name, null);
    }

    public void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
        if (referencedEntityName == null)
            throw new NullPointerException("No referencedEntityName passed");
        if (name == null)
            throw new NullPointerException("No attribute name passed");

        AttributeImpl attribute = new AttributeImpl(referencedEntityName, name, true);
        attribute.setUserFriendlyName(userFriendlyName);
        attributeNames.add(name);
        name2attribute.put(name, attribute);
    }

    public void addAttributeCopy(Attribute attribute) {
        if (attribute == null)
            throw new NullPointerException("No attribute passed");

        attributeNames.add(attribute.getName());
        try {
            name2attribute.put(attribute.getName(), (AttributeImpl) attribute.clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
