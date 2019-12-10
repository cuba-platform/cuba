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

package com.haulmont.cuba.security.app.group;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.group.*;
import com.haulmont.cuba.security.role.SecurityStorageMode;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.cuba.security.role.SecurityStorageMode.*;

@Service(AccessGroupsService.NAME)
public class AccessGroupServiceBean implements AccessGroupsService {
    @Inject
    protected DataManager dataManager;
    @Inject
    protected AccessGroupDefinitionsRepository groupsRepository;
    @Inject
    protected Metadata metadata;
    @Inject
    protected GlobalConfig globalConfig;
    @Inject
    protected DatatypeRegistry datatypes;

    @Override
    public Collection<Group> getAllGroups() {
        Map<String, Group> groups = new LinkedHashMap<>();

        SecurityStorageMode storageMode = globalConfig.getAccessGroupsStorageMode();

        if (storageMode == MIXED || storageMode == SOURCE_CODE) {
            Collection<AccessGroupDefinition> groupDefinitions = groupsRepository.getGroupDefinitions();
            if (groupDefinitions != null) {
                Map<String, Group> result = groupDefinitions.stream()
                        .collect(Collectors.toMap(AccessGroupDefinition::getName, this::mapToGroup, (a, b) -> b, LinkedHashMap::new));
                for (AccessGroupDefinition groupDefinition : groupDefinitions) {
                    Group group = result.get(groupDefinition.getName());
                    if (groupDefinition.getParent() != null) {
                        group.setParent(result.get(groupDefinition.getParent()));
                    }
                    groups.put(group.getName(), group);
                }
            }
        }

        if (storageMode == MIXED || storageMode == DATABASE) {
            List<Group> dbGroups = dataManager.load(Group.class)
                    .view("group.browse")
                    .query("select g from sec$Group g order by g.name")
                    .list();

            for (Group dbGroup : dbGroups) {
                groups.putIfAbsent(dbGroup.getName(), dbGroup);
            }
        }

        List<Group> sortedGroups = new ArrayList<>(groups.values());
        sortedGroups.sort(Comparator.comparing(Group::getName));

        return sortedGroups;
    }

    @Override
    public Collection<Constraint> getGroupConstraints(Group group) {
        List<Constraint> result;
        if (group == null) {
            result = Collections.emptyList();
        } else if (group.isPredefined()) {
            AccessGroupDefinition groupDefinition = groupsRepository.getGroupDefinition(group.getName());
            SetOfAccessConstraints setOfConstraints = groupDefinition.accessConstraints();
            result = setOfConstraints.getEntityTypes().stream()
                    .flatMap(setOfConstraints::findConstraintsByEntity)
                    .map(c -> mapToConstraint(c, group))
                    .collect(Collectors.toList());
        } else {
            result = dataManager.load(Constraint.class)
                    .view("group.browse")
                    .query("select c from sec$Constraint c where c.group.id = :groupId")
                    .parameter("groupId", group.getId())
                    .list();
        }
        return result;
    }

    @Override
    public Group findPredefinedGroupByName(String name) {
        AccessGroupDefinition groupDefinition = groupsRepository.getGroupDefinition(name);
        return groupDefinition == null ? null : mapToGroup(groupDefinition);
    }

    @Nullable
    @Override
    public Group getUserDefaultGroup() {
        Group group = null;
        SecurityStorageMode storageMode = globalConfig.getAccessGroupsStorageMode();
        if (storageMode == MIXED || storageMode == DATABASE) {
            List<Group> groups = dataManager.load(Group.class)
                    .view(View.MINIMAL)
                    .query("select g from sec$Group g")
                    .maxResults(2)
                    .list();
            if (groups.size() == 1) {
                group = groups.get(0);
            }
        }
        return group;
    }

    @Override
    public Collection<SessionAttribute> getGroupAttributes(Group group) {
        List<SessionAttribute> result;
        if (group == null) {
            result = Collections.emptyList();
        } else if (group.isPredefined()) {
            AccessGroupDefinition groupDefinition = groupsRepository.getGroupDefinition(group.getName());
            Map<String, Serializable> sessionAttributes = groupDefinition.sessionAttributes();
            if (sessionAttributes != null) {
                result = sessionAttributes.entrySet().stream()
                        .map(e -> mapToSessionAttribute(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
            } else {
                result = Collections.emptyList();
            }
        } else {
            result = dataManager.load(SessionAttribute.class)
                    .view(View.LOCAL)
                    .query("select a from sec$SessionAttribute a where a.group.id = :groupId")
                    .parameter("groupId", group.getId())
                    .list();
        }
        return result;
    }

    protected Group mapToGroup(AccessGroupDefinition groupDefinition) {
        Group group = metadata.create(Group.class);
        group.setName(groupDefinition.getName());
        group.setPredefined(true);
        return group;
    }

    protected Constraint mapToConstraint(AccessConstraint accessConstraint, Group group) {
        Constraint constraint = metadata.create(Constraint.class);
        constraint.setPredefined(true);
        constraint.setEntityName(accessConstraint.getEntityType());
        if (accessConstraint instanceof JpqlAccessConstraint) {
            JpqlAccessConstraint jpqlEntityConstraint = (JpqlAccessConstraint) accessConstraint;
            constraint.setWhereClause(jpqlEntityConstraint.getWhere());
            constraint.setJoinClause(jpqlEntityConstraint.getJoin());
        }
        if (accessConstraint.isCustom()) {
            constraint.setOperationType(ConstraintOperationType.CUSTOM);
            constraint.setCode(accessConstraint.getCode());
        } else {
            constraint.setOperationType(ConstraintOperationType.fromEntityOp(accessConstraint.getOperation()));
        }
        constraint.setCheckType(evaluateConstraintType(accessConstraint));
        constraint.setGroup(group);
        return constraint;
    }

    protected SessionAttribute mapToSessionAttribute(String key, Object value) {
        SessionAttribute sessionAttribute = metadata.create(SessionAttribute.class);

        sessionAttribute.setName(key);
        sessionAttribute.setPredefined(true);
        if (value != null) {
            Datatype datatype = datatypes.get(value.getClass());
            if (datatype != null) {
                sessionAttribute.setDatatype(datatype.getName());
                sessionAttribute.setStringValue(datatype.format(value));
            }
        }
        return sessionAttribute;
    }

    protected ConstraintCheckType evaluateConstraintType(AccessConstraint accessConstraint) {
        if (accessConstraint.isInMemory() && accessConstraint instanceof JpqlAccessConstraint) {
            return ConstraintCheckType.DATABASE_AND_MEMORY;
        } else if (accessConstraint instanceof JpqlAccessConstraint) {
            return ConstraintCheckType.DATABASE;
        }
        return ConstraintCheckType.MEMORY;
    }
}

