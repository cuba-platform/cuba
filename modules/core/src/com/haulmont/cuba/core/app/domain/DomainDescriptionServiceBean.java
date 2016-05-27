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

package com.haulmont.cuba.core.app.domain;

import com.haulmont.chile.core.datatypes.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DomainDescriptionService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.security.entity.EntityOp;
import freemarker.template.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service(DomainDescriptionService.NAME)
public class DomainDescriptionServiceBean implements DomainDescriptionService {

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Resources resources;

    @Override
    public String getDomainDescription() {
        List<View> views = ((AbstractViewRepository) viewRepository).getAll();

        List<MetaClassRepresentation> classes = new ArrayList<>();
        List<TemplateHashModel> enums = new ArrayList<>();

        Set<MetaClass> metas = new HashSet<>(metadataTools.getAllPersistentMetaClasses());
        metas.addAll(metadataTools.getAllEmbeddableMetaClasses());
        for (MetaClass meta : metas) {
            if (metadata.getExtendedEntities().getExtendedClass(meta) != null)
                continue;
            if (!readPermitted(meta))
                continue;

            List<View> metaClassViews = new ArrayList<>();
            for (View view : views) {
                if (view.getEntityClass().equals(meta.getJavaClass())) {
                    metaClassViews.add(view);
                }
            }

            MetaClassRepresentation rep = new MetaClassRepresentation(meta, metaClassViews);
            classes.add(rep);

            for (MetaClassRepresentation.MetaClassRepProperty metaProperty : rep.getProperties()) {
                TemplateHashModel enumValues = metaProperty.getEnumValues();
                if (enumValues!=null) enums.add(enumValues);
            }

        }
        Collections.sort(classes, new Comparator<MetaClassRepresentation>() {
            @Override
            public int compare(MetaClassRepresentation o1, MetaClassRepresentation o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Collections.sort(enums, new Comparator<TemplateHashModel>() {
            @Override
            public int compare(TemplateHashModel o1, TemplateHashModel o2) {
                try {
                    return o1.get("name").toString().compareTo(o2.get("name").toString());
                } catch (TemplateModelException e) {
                    return 0;
                }
            }
        });

        Map<String, Object> values = new HashMap<>();
        values.put("knownEntities", classes);

        String[] availableTypes = getAvailableBasicTypes();
        values.put("availableTypes", availableTypes);

        values.put("enums", enums);

        String template = resources.getResourceAsString("/com/haulmont/cuba/core/app/domain/DomainDescription.ftl");
        return TemplateHelper.processTemplate(template, values);
    }

    private boolean readPermitted(MetaClass metaClass) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityOpPermitted(metaClass, EntityOp.READ);
    }

    public String[] getAvailableBasicTypes() {
        Set<String> allAvailableTypes = Datatypes.getNames();
        TreeSet<String> availableTypes = new TreeSet<>();

        //byteArray is not supported as a GET parameter
        for (String type : allAvailableTypes)
            if (!"byteArray".equals(type))
                availableTypes.add(type);

        return availableTypes.toArray(new String[availableTypes.size()]);
    }
}