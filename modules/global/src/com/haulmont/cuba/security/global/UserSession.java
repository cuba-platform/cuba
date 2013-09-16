/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.security.entity.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that encapsulates an active user session.
 *
 * <p>It contains user attributes, credentials, set of permissions, and methods to check permissions for certain
 * objects.</p>
 *
 * <p>On the client side a descendant of this class is maintained:
 * <code>com.haulmont.cuba.client.ClientUserSession</code></p>
 *
 * @author krivopustov
 * @version $Id$
 */
public class UserSession implements Serializable {

    private static final long serialVersionUID = -8248326616891177382L;

    protected UUID id;
    protected User user;
    protected User substitutedUser;
    private List<String> roles = new ArrayList<String>();
    private EnumSet<RoleType> roleTypes = EnumSet.noneOf(RoleType.class);
    protected Locale locale;
    protected String address;
    protected String clientInfo;
    protected boolean system;

    protected Map<String, Integer>[] permissions;
    protected Map<String, List<String[]>> constraints;

    protected Map<String, Serializable> attributes;

    public UserSession(UUID id, User user, Collection<Role> roles, Locale locale, boolean system) {
        this.id = id;
        this.user = user;
        this.system = system;

        for (Role role : roles) {
            this.roles.add(role.getName());
            if (role.getType() != null)
                roleTypes.add(role.getType());
        }

        this.locale = locale;

        permissions = new Map[PermissionType.values().length];
        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = new HashMap<String, Integer>();
        }

        constraints = new HashMap<String, List<String[]>>();
        attributes = new ConcurrentHashMap<String, Serializable>();
    }

    public UserSession(UserSession src, User user, Collection<Role> roles, Locale locale) {
        this(src.id, user, roles, locale, src.system);
        this.user = src.user;
        this.substitutedUser = this.user.equals(user) ? null : user;
    }

    public UserSession(UserSession src) {
        id = src.id;
        user = src.user;
        substitutedUser = src.substitutedUser;
        system = src.system;
        roles = src.roles;
        locale = src.locale;
        permissions = src.permissions;
        constraints = src.constraints;
        attributes = src.attributes;
        roleTypes = src.roleTypes;
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
     * Don't do it
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Substituted user. May be null.
     */
    public User getSubstitutedUser() {
        return substitutedUser;
    }

    /**
     * Don't do it
     */
    public void setSubstitutedUser(User substitutedUser) {
        this.substitutedUser = substitutedUser;
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
    public Collection<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    /**
     * User locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Client IP-address
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Client application info
     */
    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    /**
     * This method is used by security subsystem
     */
    public void addPermission(PermissionType type, String target, String extTarget, int value) {
        Integer currentValue = permissions[type.ordinal()].get(target);
        if (currentValue == null || currentValue < value) {
            permissions[type.ordinal()].put(target, value);
            if (extTarget != null)
                permissions[type.ordinal()].put(extTarget, value);
        }
    }

    /**
     * This method is used by security subsystem
     */
    public void removePermission(PermissionType type, String target) {
        permissions[type.ordinal()].remove(target);
    }

    /**
     * This method is used by security subsystem
     */
    public Integer getPermissionValue(PermissionType type, String target) {
        return permissions[type.ordinal()].get(target);
    }

    /** Get permissions by type */
    public Map<String, Integer> getPermissionsByType(PermissionType type) {
        return Collections.unmodifiableMap(permissions[type.ordinal()]);
    }

    /** Check user permission for the screen */
    public boolean isScreenPermitted(String windowAlias) {
        return isPermitted(PermissionType.SCREEN, windowAlias);
    }

    /** Check user permission for the entity operation */
    public boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        return isPermitted(PermissionType.ENTITY_OP,
                metaClass.getName() + Permission.TARGET_PATH_DELIMETER + entityOp.getId());
    }

    /** Check user permission for the entity attribute */
    public boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access) {
        return isPermitted(PermissionType.ENTITY_ATTR,
                metaClass.getName() + Permission.TARGET_PATH_DELIMETER + property,
                access.getId());
    }

    /** Check specific user permission */
    public boolean isSpecificPermitted(String name) {
        return isPermitted(PermissionType.SPECIFIC, name);
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
     * Check user permission for the specified value.
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
        // If we have super-role no need to check anything
        if (roleTypes.contains(RoleType.SUPER))
            return true;
        // Get permission value assigned by the set of permissions
        Integer v = permissions[type.ordinal()].get(target);
        // Get permission value assigned by non-standard roles
        for (RoleType roleType : roleTypes) {
            Integer v1 = roleType.permissionValue(type, target);
            if (v1 != null && (v == null || v < v1)) {
                v = v1;
            }
        }
        // Return true if no value set for this target, or if the value is more than requested
        return v == null || v >= value;
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
     * Remove user session attribute. Attribute is a named serializable object bound to session.
     * @param name attribute name
     */
    public void removeAttribute(String name) {
         attributes.remove(name);
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

    /**
     * System session is created by <code>LoginWorker.loginSystem()</code> for system users like schedulers and JMX.
     * <p/>
     * It is not replicated in cluster.
     */
    public boolean isSystem() {
        return system;
    }

    public String toString() {
        return id + " ["
                + user.getLogin() + (substitutedUser == null ? "" : " / " + substitutedUser.getLogin())
                + "]";
    }
}
