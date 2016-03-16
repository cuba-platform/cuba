/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.gui.app.core.appproperties;

import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.config.AppPropertiesLocator;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.HierarchicalDatasourceImpl;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom datasource used in the {@code appproperties-browse.xml} screen
 */
public class AppPropertiesDatasource extends HierarchicalDatasourceImpl<AppPropertyEntity, UUID> {

    @Override
    protected void loadData(Map<String, Object> params) {
        detachListener(data.values());
        data.clear();

        ConfigStorageService configStorageService = AppBeans.get(ConfigStorageService.class);
        List<AppPropertyEntity> entities = configStorageService.getAppProperties();

        AppPropertiesLocator appPropertiesLocator = AppBeans.get(AppPropertiesLocator.class);
        entities.addAll(appPropertiesLocator.getAppProperties());

        String name = (String) params.get("name");
        if (StringUtils.isNotEmpty(name)) {
            entities = entities.stream()
                    .filter(it -> it.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        List<AppPropertyEntity> tree = createEntitiesTree(entities);
        for (AppPropertyEntity entity : tree) {
            data.put(entity.getId(), entity);
            attachListener(entity);
        }
    }

    List<AppPropertyEntity> createEntitiesTree(List<AppPropertyEntity> entities) {
        List<AppPropertyEntity> resultList = new ArrayList<>();
        for (AppPropertyEntity entity : entities) {
            String[] parts = entity.getName().split("\\.");
            AppPropertyEntity parent = null;
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (i < parts.length - 1) {
                    Optional<AppPropertyEntity> parentOpt = resultList.stream()
                            .filter(e -> e.getCategory() && e.getName().equals(part))
                            .findFirst();
                    if (parentOpt.isPresent()) {
                        parent = parentOpt.get();
                    } else {
                        AppPropertyEntity categoryEntity = new AppPropertyEntity();
                        categoryEntity.setParent(parent);
                        categoryEntity.setName(part);
                        resultList.add(categoryEntity);
                        parent = categoryEntity;
                    }

                } else {
                    entity.setParent(parent);
                    entity.setCategory(false);
                    resultList.add(entity);
                }
            }
        }
        // remove duplicates from global configs
        for (Iterator<AppPropertyEntity> iter = resultList.iterator(); iter.hasNext();) {
            AppPropertyEntity entity = iter.next();
            resultList.stream()
                    .filter(e -> e != entity && e.getName().equals(entity.getName()))
                    .findFirst()
                    .ifPresent(e -> iter.remove());
        }

        return resultList;
    }
}
