/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.security.role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class is used to build a new {@link RoleDefinition} from multiple other role definitions. Roles are joined using the
 * following rules:
 * <ul>
 *     <li>if only one role defines a permission on some target then this permission value is set to the resulting role</li>
 *     <li>if both roles have explicit permissions on some target then a permission that allows more is selected</li>
 *     <li>if one role has an explicit target, the second role doesn't, but the second role defines a wildcard
 *     permission, then the value that allows more (explicit from the first role or wildcard from the second role) is
 *     set as an explicit permission for the resulting role</li>
 *     <li>if both roles define wildcard permissions then the wildcard permission that allows more is selected</li>
 * </ul>
 */
public class RoleDefinitionsJoiner {

    /**
     * Builds new {@link RoleDefinition} by joining a collection of role definitions.
     *
     * @return a new RoleDefinition that contains joined permissions
     */
    public static RoleDefinition join(Collection<RoleDefinition> roles) {
        RoleDefinition resultRoleDefinition = BasicRoleDefinition.builder().build();
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
                .withScreenPermissionsContainer(joinScreenPermissions(role1, role2))
                .withEntityPermissionsContainer(joinEntityPermissions(role1, role2))
                .withEntityAttributePermissionsContainer(joinEntityAttributePermissions(role1, role2))
                .withSpecificPermissionsContainer(joinSpecificPermissions(role1, role2))
                .withScreenComponentPermissionsContainer(joinScreenComponentPermissions(role1, role2))
                .build();
    }

    private static ScreenPermissionsContainer joinScreenPermissions(RoleDefinition role1, RoleDefinition role2) {
        ScreenPermissionsContainer joinedPermissionsContainer = new ScreenPermissionsContainer();
        joinPermissions(joinedPermissionsContainer, role1.screenPermissions(), role2.screenPermissions());
        return joinedPermissionsContainer;
    }

    private static EntityPermissionsContainer joinEntityPermissions(RoleDefinition role1, RoleDefinition role2) {
        EntityPermissionsContainer joinedPermissionsContainer = new EntityPermissionsContainer();
        joinPermissions(joinedPermissionsContainer, role1.entityPermissions(), role2.entityPermissions());
        return joinedPermissionsContainer;
    }

    private static EntityAttributePermissionsContainer joinEntityAttributePermissions(RoleDefinition role1, RoleDefinition role2) {
        EntityAttributePermissionsContainer joinedPermissionsContainer = new EntityAttributePermissionsContainer();
        joinPermissions(joinedPermissionsContainer, role1.entityAttributePermissions(), role2.entityAttributePermissions());
        return joinedPermissionsContainer;
    }

    private static SpecificPermissionsContainer joinSpecificPermissions(RoleDefinition role1, RoleDefinition role2) {
        SpecificPermissionsContainer joinedPermissionsContainer = new SpecificPermissionsContainer();
        joinPermissions(joinedPermissionsContainer, role1.specificPermissions(), role2.specificPermissions());
        return joinedPermissionsContainer;
    }

    private static ScreenComponentPermissionsContainer joinScreenComponentPermissions(RoleDefinition role1, RoleDefinition role2) {
        ScreenComponentPermissionsContainer joinedPermissionsContainer = new ScreenComponentPermissionsContainer();
        joinPermissions(joinedPermissionsContainer, role1.screenComponentPermissions(), role2.screenComponentPermissions());
        return joinedPermissionsContainer;
    }


    private static void joinPermissions(PermissionsContainer resultPermissionsContainer,
                                        PermissionsContainer permissionsContainer1,
                                        PermissionsContainer permissionsContainer2) {
        Map<String, Integer> permissionsMap1 = permissionsContainer1.getExplicitPermissions();
        Map<String, Integer> permissionsMap2 = permissionsContainer2.getExplicitPermissions();
        Set<String> processedTargets = new HashSet<>();
        for (Map.Entry<String, Integer> entry : permissionsMap1.entrySet()) {
            String target = entry.getKey();
            Integer value1 = entry.getValue();
            appendPermission(resultPermissionsContainer, target, value1);
            Integer value2 = permissionsMap2.get(target);
            if (value2 == null) {
                value2 = PermissionsUtils.getWildcardPermissionValue(permissionsContainer2,
                        target);
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
            if (value1 == null) {
                value1 = PermissionsUtils.getWildcardPermissionValue(permissionsContainer1,
                        target);
            }
            if (value1 != null) {
                appendPermission(resultPermissionsContainer, target, value1);
            }
        }
    }

    /**
     * Puts permission defined by the {@code target} and {@code value} to explicit permissions collection of the {@code
     * permissionsContainer} if the container doesn't already contain this permission with a greater value
     */
    private static void appendPermission(PermissionsContainer permissionsContainer, String target, int value) {
        Integer currentValue = permissionsContainer.getExplicitPermissions().get(target);
        if (currentValue == null || currentValue < value) {
            permissionsContainer.getExplicitPermissions().put(target, value);
        }
    }

}
