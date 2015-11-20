/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.global;

import com.google.common.collect.ArrayListMultimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.security.entity.*;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class that encapsulates an active user session.
 * <p>
 * <p>It contains user attributes, credentials, set of permissions, and methods to check permissions for certain
 * objects.</p>
 * <p>
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
    private List<String> roles = new ArrayList<>();
    private EnumSet<RoleType> roleTypes = EnumSet.noneOf(RoleType.class);
    protected Locale locale;
    protected TimeZone timeZone;
    protected String address;
    protected String clientInfo;
    protected boolean system;

    protected Map<String, Integer>[] permissions;
    protected ArrayListMultimap<String, ConstraintData> constraints;

    protected Map<String, Serializable> attributes;

    /**
     * INTERNAL
     */
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
        if (user.getTimeZone() != null)
            this.timeZone = TimeZone.getTimeZone(user.getTimeZone());

        //noinspection unchecked
        permissions = new Map[PermissionType.values().length];
        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = new HashMap<>();
        }

        constraints = ArrayListMultimap.create();
        attributes = new ConcurrentHashMap<>();
    }

    /**
     * INTERNAL
     */
    public UserSession(UserSession src, User user, Collection<Role> roles, Locale locale) {
        this(src.id, user, roles, locale, src.system);
        this.user = src.user;
        this.substitutedUser = this.user.equals(user) ? null : user;
    }

    /**
     * INTERNAL
     */
    public UserSession(UserSession src) {
        id = src.id;
        user = src.user;
        substitutedUser = src.substitutedUser;
        system = src.system;
        roles = src.roles;
        locale = src.locale;
        timeZone = src.timeZone;
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
     * INTERNAL
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
     * INTERNAL
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
     * INTERNAL
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * User time zone. Can be null.
     */
    @Nullable
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * INTERNAL
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Client IP-address
     */
    public String getAddress() {
        return address;
    }

    /**
     * INTERNAL
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Client application info
     */
    public String getClientInfo() {
        return clientInfo;
    }

    /**
     * INTERNAL
     */
    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    /**
     * INTERNAL
     */
    public void addPermission(PermissionType type, String target, @Nullable String extTarget, int value) {
        Integer currentValue = permissions[type.ordinal()].get(target);
        if (currentValue == null || currentValue < value) {
            permissions[type.ordinal()].put(target, value);
            if (extTarget != null)
                permissions[type.ordinal()].put(extTarget, value);
        }
    }

    /**
     * INTERNAL
     */
    public void removePermission(PermissionType type, String target) {
        permissions[type.ordinal()].remove(target);
    }

    /**
     * INTERNAL
     */
    public Integer getPermissionValue(PermissionType type, String target) {
        return permissions[type.ordinal()].get(target);
    }

    /**
     * Get permissions by type
     */
    public Map<String, Integer> getPermissionsByType(PermissionType type) {
        return Collections.unmodifiableMap(permissions[type.ordinal()]);
    }

    /**
     * Check user permission for the screen
     */
    public boolean isScreenPermitted(String windowAlias) {
        return isPermitted(PermissionType.SCREEN, windowAlias);
    }

    /**
     * Check user permission for the entity operation
     */
    public boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        return isPermitted(PermissionType.ENTITY_OP,
                metaClass.getName() + Permission.TARGET_PATH_DELIMETER + entityOp.getId());
    }

    /**
     * Check user permission for the entity attribute
     */
    public boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access) {
        return isPermitted(PermissionType.ENTITY_ATTR,
                metaClass.getName() + Permission.TARGET_PATH_DELIMETER + property,
                access.getId());
    }

    /**
     * Check specific user permission
     */
    public boolean isSpecificPermitted(String name) {
        return isPermitted(PermissionType.SPECIFIC, name);
    }

    /**
     * Check user permission.
     * <br>Same as {@link #isPermitted(com.haulmont.cuba.security.entity.PermissionType, String, int)}
     * with value=1
     * <br>This method makes sense for permission types with two possible values 0,1
     *
     * @param type   permission type
     * @param target permission target:<ul>
     *               <li>screen
     *               <li>entity operation (view, create, update, delete)
     *               <li>entity attribute name
     *               <li>specific permission name
     *               </ul>
     * @return true if permitted, false otherwise
     */
    public boolean isPermitted(PermissionType type, String target) {
        return isPermitted(type, target, 1);
    }

    /**
     * Check user permission for the specified value.
     *
     * @param type   permission type
     * @param target permission target:<ul>
     *               <li>screen
     *               <li>entity operation (view, create, update, delete)
     *               <li>entity attribute name
     *               <li>specific permission name
     *               </ul>
     * @param value  method returns true if the corresponding {@link com.haulmont.cuba.security.entity.Permission}
     *               record contains value equal or greater than specified
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
     * INTERNAL
     */
    public void addConstraint(Constraint constraint) {
        String entityName = constraint.getEntityName();
        constraints.put(entityName, new ConstraintData(constraint));
    }

    /**
     * INTERNAL
     */
    public List<ConstraintData> getConstraints(String entityName) {
        return Collections.unmodifiableList(constraints.get(entityName));
    }

    /**
     * INTERNAL
     */
    public boolean hasConstraints(String entityName) {
        return constraints.containsKey(entityName);
    }

    /**
     * INTERNAL
     */
    public boolean hasConstraints() {
        return !constraints.isEmpty();
    }

    /**
     * INTERNAL
     */
    public List<ConstraintData> getConstraints(String entityName, Predicate<ConstraintData> predicate) {
        List<ConstraintData> list = constraints.get(entityName);
        return Collections.unmodifiableList(list.stream().filter(predicate).collect(Collectors.toList()));
    }

    /**
     * Get user session attribute. Attribute is a named serializable object bound to session.
     *
     * @param name attribute name. The following names have predefined values:
     *             <ul>
     *             <li>userId - current or substituted user ID</li>
     *             <li>userLogin - current or substituted user login in lower case</li>
     *             </ul>
     * @return attribute value or null if attribute with the given name is not found
     */
    @SuppressWarnings("unchecked")
    @Nullable
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
     *
     * @param name attribute name
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * Set user session attribute. Attribute is a named serializable object bound to session.
     *
     * @param name  attribute name
     * @param value attribute value
     */
    public void setAttribute(String name, Serializable value) {
        attributes.put(name, value);
    }

    /**
     * User session attribute names. Attribute is a named serializable object bound to session.
     */
    public Collection<String> getAttributeNames() {
        //noinspection unchecked
        return new ArrayList(attributes.keySet());
    }

    /**
     * System session is created by <code>LoginWorker.loginSystem()</code> for system users like schedulers and JMX.
     * <p>
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
