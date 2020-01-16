/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.HasSecurityAccessValue;
import com.haulmont.cuba.security.role.*;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Class is used to build a new {@link RoleDefinition} from two other role definitions. Roles are joined using the
 * following rules:
 * <ul>
 *     <li>if both roles have explicit permissions on some target then a permission that allows more is selected</li>
 *     <li>if one role has an explicit target and the second role doesn't, but the second role has a default, then the
 *     value that allows more (explicit from the first role or default from the second role) is set as an explicit
 *     permission for the result role</li>
 *     <li>after all explicit permissions are checked, the default values are merged - default with the value that
 *     allows more is set as a default for the resulting role</li>
 * </ul>
 */
public class RoleDefinitionsJoiner {

    /**
     * Builds new {@link RoleDefinition} by joining a collection of role definitions.
     *
     * @return a new RoleDefinition that contains joined permissions
     */
    public static RoleDefinition join(Collection<RoleDefinition> roles) {
        RoleDefinition resultRoleDefinition = RoleDefinitionBuilder.create().build();
        for (RoleDefinition role : roles) {
            resultRoleDefinition = join(resultRoleDefinition, role);
        }
        return resultRoleDefinition;
    }

    /**
     * Builds new {@link RoleDefinition} by joining two role definitions.
     *
     * @return a new RoleDefinition that contains joined permissions
     */
    public static RoleDefinition join(RoleDefinition role1, RoleDefinition role2) {
        return BasicRoleDefinition.builder()
                .withName(role1.getName())
                .withDescription(role1.getDescription())
                .withScreenPermissions(joinScreenPermissions(role1, role2))
                .withEntityPermissions(joinEntityPermissions(role1, role2))
                .withEntityAttributePermissions(joinEntityAttributePermissions(role1, role2))
                .withSpecificPermissions(joinSpecificPermissions(role1, role2))
                .withScreenElementsPermissions(joinScreenElementsPermissions(role1, role2))
                .build();
    }

    private static ScreenPermissionsContainer joinScreenPermissions(RoleDefinition role1, RoleDefinition role2) {
        ScreenPermissionsContainer joinedPermissionsContainer = new ScreenPermissionsContainer();
        Access defaultScreenAccess1 = role1.screenPermissions().getDefaultScreenAccess();
        Access defaultScreenAccess2 = role2.screenPermissions().getDefaultScreenAccess();
        joinPermissions(joinedPermissionsContainer,
                role1.screenPermissions().getExplicitPermissions(),
                role2.screenPermissions().getExplicitPermissions(),
                defaultScreenAccess1,
                defaultScreenAccess2);
        Access effectiveDefaultAccess = evaluateEffectiveAccess(defaultScreenAccess1, defaultScreenAccess2);
        joinedPermissionsContainer.setDefaultScreenAccess(effectiveDefaultAccess);
        return joinedPermissionsContainer;
    }

    private static EntityPermissionsContainer joinEntityPermissions(RoleDefinition role1, RoleDefinition role2) {
        EntityPermissionsContainer joinedPermissionsContainer = new EntityPermissionsContainer();

        EntityPermissionsContainer entityPermissions1 = role1.entityPermissions();
        EntityPermissionsContainer entityPermissions2 = role2.entityPermissions();

        Map<String, Integer> createPermissions1 = new HashMap<>();
        Map<String, Integer> readPermissions1 = new HashMap<>();
        Map<String, Integer> updatePermissions1 = new HashMap<>();
        Map<String, Integer> deletePermissions1 = new HashMap<>();

        for (Map.Entry<String, Integer> entry : entityPermissions1.getExplicitPermissions().entrySet()) {
            String target = entry.getKey();
            if (target.endsWith(":create")) {
                createPermissions1.put(target, entry.getValue());
            } else if (target.endsWith(":read")) {
                readPermissions1.put(target, entry.getValue());
            } else if (target.endsWith(":update")) {
                updatePermissions1.put(target, entry.getValue());
            } else if (target.endsWith(":delete")) {
                deletePermissions1.put(target, entry.getValue());
            }
        }

        Map<String, Integer> createPermissions2 = new HashMap<>();
        Map<String, Integer> readPermissions2 = new HashMap<>();
        Map<String, Integer> updatePermissions2 = new HashMap<>();
        Map<String, Integer> deletePermissions2 = new HashMap<>();

        for (Map.Entry<String, Integer> entry : entityPermissions2.getExplicitPermissions().entrySet()) {
            String target = entry.getKey();
            if (target.endsWith(":create")) {
                createPermissions2.put(target, entry.getValue());
            } else if (target.endsWith(":read")) {
                readPermissions2.put(target, entry.getValue());
            } else if (target.endsWith(":update")) {
                updatePermissions2.put(target, entry.getValue());
            } else if (target.endsWith(":delete")) {
                deletePermissions2.put(target, entry.getValue());
            }
        }

        joinPermissions(joinedPermissionsContainer,
                createPermissions1,
                createPermissions2,
                entityPermissions1.getDefaultEntityCreateAccess(),
                entityPermissions2.getDefaultEntityCreateAccess());

        joinedPermissionsContainer.setDefaultEntityCreateAccess(evaluateEffectiveAccess(
                entityPermissions1.getDefaultEntityCreateAccess(),
                entityPermissions2.getDefaultEntityCreateAccess()));

        joinPermissions(joinedPermissionsContainer,
                readPermissions1,
                readPermissions2,
                entityPermissions1.getDefaultEntityReadAccess(),
                entityPermissions2.getDefaultEntityReadAccess());

        joinedPermissionsContainer.setDefaultEntityReadAccess(evaluateEffectiveAccess(
                entityPermissions1.getDefaultEntityReadAccess(),
                entityPermissions2.getDefaultEntityReadAccess()));

        joinPermissions(joinedPermissionsContainer,
                updatePermissions1,
                updatePermissions2,
                entityPermissions1.getDefaultEntityUpdateAccess(),
                entityPermissions2.getDefaultEntityUpdateAccess());

        joinedPermissionsContainer.setDefaultEntityUpdateAccess(evaluateEffectiveAccess(
                entityPermissions1.getDefaultEntityUpdateAccess(),
                entityPermissions2.getDefaultEntityUpdateAccess()));

        joinPermissions(joinedPermissionsContainer,
                deletePermissions1,
                deletePermissions2,
                entityPermissions1.getDefaultEntityDeleteAccess(),
                entityPermissions2.getDefaultEntityDeleteAccess());

        joinedPermissionsContainer.setDefaultEntityDeleteAccess(evaluateEffectiveAccess(
                entityPermissions1.getDefaultEntityDeleteAccess(),
                entityPermissions2.getDefaultEntityDeleteAccess()));

        return joinedPermissionsContainer;
    }

    private static EntityAttributePermissionsContainer joinEntityAttributePermissions(RoleDefinition role1,
                                                                                      RoleDefinition role2) {
        EntityAttributePermissionsContainer joinedPermissionsContainer = new EntityAttributePermissionsContainer();
        EntityAttributePermissionsContainer entityAttributePermissions1 = role1.entityAttributePermissions();
        EntityAttributePermissionsContainer entityAttributePermissions2 = role2.entityAttributePermissions();
        EntityAttrAccess defaultEntityAttributeAccess1 = entityAttributePermissions1.getDefaultEntityAttributeAccess();
        EntityAttrAccess defaultEntityAttributeAccess2 = entityAttributePermissions2.getDefaultEntityAttributeAccess();
        joinPermissions(joinedPermissionsContainer,
                entityAttributePermissions1.getExplicitPermissions(),
                entityAttributePermissions2.getExplicitPermissions(),
                defaultEntityAttributeAccess1,
                defaultEntityAttributeAccess2);
        EntityAttrAccess effectiveDefaultAccess = evaluateEffectiveAccess(defaultEntityAttributeAccess1,
                defaultEntityAttributeAccess2);
        joinedPermissionsContainer.setDefaultEntityAttributeAccess(effectiveDefaultAccess);
        return joinedPermissionsContainer;
    }

    private static SpecificPermissionsContainer joinSpecificPermissions(RoleDefinition role1, RoleDefinition role2) {
        SpecificPermissionsContainer joinedPermissionsContainer = new SpecificPermissionsContainer();
        SpecificPermissionsContainer permissions1 = role1.specificPermissions();
        SpecificPermissionsContainer permissions2 = role2.specificPermissions();
        Access defaultSpecificAccess1 = permissions1.getDefaultSpecificAccess();
        Access defaultSpecificAccess2 = permissions2.getDefaultSpecificAccess();
        joinPermissions(joinedPermissionsContainer,
                role1.specificPermissions().getExplicitPermissions(),
                role2.specificPermissions().getExplicitPermissions(),
                defaultSpecificAccess1,
                defaultSpecificAccess2);
        Access effectiveDefaultAccess = evaluateEffectiveAccess(defaultSpecificAccess1, defaultSpecificAccess2);
        joinedPermissionsContainer.setDefaultSpecificAccess(effectiveDefaultAccess);
        return joinedPermissionsContainer;
    }

    private static ScreenElementsPermissionsContainer joinScreenElementsPermissions(RoleDefinition role1,
                                                                                    RoleDefinition role2) {
        ScreenElementsPermissionsContainer joinedPermissions = new ScreenElementsPermissionsContainer();
        joinPermissions(joinedPermissions,
                role1.screenElementsPermissions().getExplicitPermissions(),
                role2.screenElementsPermissions().getExplicitPermissions(),
                null,
                null);
        return joinedPermissions;
    }

    private static void joinPermissions(PermissionsContainer resultPermissionsContainer,
                                        Map<String, Integer> permissionsMap1,
                                        Map<String, Integer> permissionsMap2,
                                        HasSecurityAccessValue defaultAccess1,
                                        HasSecurityAccessValue defaultAccess2) {
        Set<String> processedTargets = new HashSet<>();
        for (Map.Entry<String, Integer> entry : permissionsMap1.entrySet()) {
            String target = entry.getKey();
            Integer value1 = entry.getValue();
            appendPermission(resultPermissionsContainer, target, value1);
            Integer value2 = permissionsMap2.get(target);
            if (value2 == null && defaultAccess2 != null) {
                value2 = defaultAccess2.getId();
            }
            if (value2 != null) {
                appendPermission(resultPermissionsContainer, target, value2);
            }
            processedTargets.add(target);
        }

        for (Map.Entry<String, Integer> entry : permissionsMap2.entrySet()) {
            String target = entry.getKey();
            if (processedTargets.contains(target)) continue;
            Integer value2 = entry.getValue();
            appendPermission(resultPermissionsContainer, target, value2);

            Integer value1 = permissionsMap1.get(target);
            if (value1 == null && defaultAccess1 != null) {
                value1 = defaultAccess1.getId();
            }
            if (value1 != null) {
                appendPermission(resultPermissionsContainer, target, value1);
            }
        }
    }

    /**
     * Compares two access values and returns the access with a greater value (the one that allows more)
     */
    @Nullable
    private static Access evaluateEffectiveAccess(Access access1, Access access2) {
        if (access1 == null) return access2;
        if (access2 == null) return access1;
        return (access1.getId() > access2.getId()) ? access1 : access2;
    }

    @Nullable
    protected static EntityAttrAccess evaluateEffectiveAccess(EntityAttrAccess access1, EntityAttrAccess access2) {
        if (access1 == null) return access2;
        if (access2 == null) return access1;
        return (access1.getId() > access2.getId()) ? access1 : access2;
    }

    /**
     * Puts permission defined by the {@code target} and {@code value} to explicit permissions collection of the
     * {@code permissionsContainer} if the container doesn't already contain this permission with a greater value
     */
    protected static void appendPermission(PermissionsContainer permissionsContainer, String target, int value) {
        Integer currentValue = permissionsContainer.getExplicitPermissions().get(target);
        if (currentValue == null || currentValue < value) {
            permissionsContainer.getExplicitPermissions().put(target, value);
        }
    }

}
