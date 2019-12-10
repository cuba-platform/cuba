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

import com.haulmont.cuba.security.group.AccessGroupDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component(AccessGroupDefinitionsRepository.NAME)
public class AccessGroupDefinitionsRepositoryImpl implements AccessGroupDefinitionsRepository {
    @Autowired(required = false)
    protected List<AccessGroupDefinition> groupDefinitions;

    protected Map<String, AccessGroupDefinition> groupDefinitionsByName;

    @PostConstruct
    protected void init() {
        groupDefinitionsByName = new ConcurrentHashMap<>();
        if (groupDefinitions != null) {
            for (AccessGroupDefinition groupDefinition : groupDefinitions) {
                groupDefinitionsByName.put(groupDefinition.getName(), groupDefinition);
            }
        }
    }

    @Override
    public AccessGroupDefinition getGroupDefinition(String groupName) {
        AccessGroupDefinition groupDefinition = groupDefinitionsByName.get(groupName);
        if (groupDefinition == null) {
            throw new IllegalStateException(String.format("Unable to find predefined group definition %s", groupName));
        }
        return groupDefinition;
    }

    @Override
    public Collection<AccessGroupDefinition> getGroupDefinitions() {
        return Collections.unmodifiableCollection(groupDefinitionsByName.values());
    }

    @Override
    public void registerGroupDefinition(AccessGroupDefinition groupDefinition) {
        groupDefinitionsByName.put(groupDefinition.getName(), groupDefinition);
    }
}
