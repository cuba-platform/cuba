/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 13:27:53
 *
 * $Id$
 */
package com.haulmont.cuba.security.global;

import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.chile.core.model.MetaClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;

/**
 * User session
 */
public class UserSession implements Serializable
{
    private static final long serialVersionUID = -8248326616891177382L;

    private UUID id;
    private User user;
    private User substitutedUser;
    private String[] roles;
    private Locale locale;

    private Map<String, Integer>[] permissions;
    private Map<String, List<String[]>> constraints;

    private final Map<String, Serializable> attributes;

    public static String getScreenPermissionTarget(ClientType clientType, String windowAlias) {
        return clientType.getId() + ":" + windowAlias;
    }

    public static String getEntityOpPermissionTarget(MetaClass metaClass, EntityOp operation) {
        return metaClass.getName() + ":" + operation.getId();
    }

    public static String getEntityAttrPermissionTarget(MetaClass metaClass, String attribute) {
        return metaClass.getName() + ":" + attribute;
    }

    public static String getSpecificPermissionTarget(String name) {
        return name;
    }

    public UserSession(User user, String[] roles, Locale locale) {
        this.id = UuidProvider.createUuid();
        this.user = user;

        this.roles = roles;
        Arrays.sort(this.roles);

        this.locale = locale;

        permissions = new Map[PermissionType.values().length];
        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = new HashMap<String, Integer>();
        }

        constraints = new HashMap<String, List<String[]>>();
        attributes = new ConcurrentHashMap<String, Serializable>();
    }
    
    public UserSession(UserSession src, User user, String[] roles, Locale locale) {
        this(user, roles, locale);
        this.id = src.id;
        this.user = src.user;
        this.substitutedUser = user;
    }

    /**
     * Session ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Logged-in user
     */
    public User getUser() {
        return user;
    }

    /**
     * Substituted user. May be null.
     */
    public User getSubstitutedUser() {
        return substitutedUser;
    }

    /**
     * Returns substituted user if it is not null, logged-in user otherwise.
     */
    public User getCurrentOrSubstitutedUser() {
        return substitutedUser == null ? user : substitutedUser;
    }

    /**
     * User role names
     */
    public String[] getRoles() {
        return roles;
    }

    /**
     * User locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * This method is used by security subsystem
     */
    public void addPermission(PermissionType type, String target, int value) {
        permissions[type.ordinal()].put(target, value);
    }

    /**
     * This method is used by security subsystem
     */
    public Integer getPermissionValue(PermissionType type, String target) {
        return permissions[type.ordinal()].get(target);
    }

    /** Check user permission for the screen */
    public boolean isScreenPermitted(ClientType clientType, String windowAlias) {
        return isPermitted(PermissionType.SCREEN,
                getScreenPermissionTarget(clientType, windowAlias));
    }

    /** Check user permission for the entity operation */
    public boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        return isPermitted(PermissionType.ENTITY_OP,
                getEntityOpPermissionTarget(metaClass, entityOp));
    }

    /** Check user permission for the entity attribute */
    public boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access) {
        return isPermitted(PermissionType.ENTITY_ATTR,
                getEntityAttrPermissionTarget(metaClass, property),
                access.getId());
    }

    /** Check specific user permission */
    public boolean isSpecificPermitted(String name) {
        return isPermitted(PermissionType.SPECIFIC,
                getSpecificPermissionTarget(name));
    }

    /**
     * Check user permission.
     * <br>Same as {@link #isPermitted(com.haulmont.cuba.security.entity.PermissionType, String, int)}
     * with value=1
     * <br>This method makes sense for permission types with two possible values 0,1
     * @param type permission type
     * @param target permission target:<ul>
     * <li>screen
     * <li>entity operation (view, create, update, delete)
     * <li>entity attribute name
     * <li>specific permission name
     * </ul>
     * @return true if permitted, false otherwise
     */
    public boolean isPermitted(PermissionType type, String target) {
        return isPermitted(type, target, 1);
    }

    /**
     * Check user permission for specified value
     * @param type permission type
     * @param target permission target:<ul>
     * <li>screen
     * <li>entity operation (view, create, update, delete)
     * <li>entity attribute name
     * <li>specific permission name
     * </ul>
     * @param value method returns true if the corresponding {@link com.haulmont.cuba.security.entity.Permission}
     * record contains value equal or greater than specified
     * @return true if permitted, false otherwise
     */
    public boolean isPermitted(PermissionType type, String target, int value) {
        Integer p = permissions[type.ordinal()].get(target);
        return p == null || p >= value;
    }

    /**
     * This method is used by security subsystem
     */
    public void addConstraint(String entityName, String joinClause, String whereClause) {
        List<String[]> list = constraints.get(entityName);
        if (list == null) {
            list = new ArrayList<String[]>();
            constraints.put(entityName, list);
        }
        list.add(new String[] {joinClause, whereClause});
    }

    /**
     * This method is used by security subsystem
     */
    public List<String[]> getConstraints(String entityName) {
        List<String[]> list = constraints.get(entityName);
        return list != null ? list : Collections.<String[]>emptyList();
    }

    /**
     * Get user session attribute. Attribute is a named serializable object bound to session.
     * @param name attribute name, or <code>userId</code> to obtain current or substituted user ID
     * @return attribute value
     */
    public <T> T getAttribute(String name) {
        if ("userId".equals(name))
            return (T) getCurrentOrSubstitutedUser().getId();
        if ("userLogin".equals(name))
            return (T) getCurrentOrSubstitutedUser().getLoginLowerCase();
        else
            return (T) attributes.get(name);
    }

    /**
     * Set user session attribute. Attribute is a named serializable object bound to session.
     * @param name attribute name
     * @param value attribute value
     */
    public void setAttribute(String name, Serializable value) {
        attributes.put(name, value);
    }

    /**
     * User session attribute names. Attribute is a named serializable object bound to session.
     */
    public Collection<String> getAttributeNames() {
        return new ArrayList(attributes.keySet());
    }

    public String toString() {
        return id + " [" 
                + user.getLogin() + (substitutedUser == null ? "" : " / " + substitutedUser.getLogin())
                + "]";
    }
}
