/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.domain;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DomainDescriptionService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
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
        }
        Collections.sort(classes, new Comparator<MetaClassRepresentation>() {
            public int compare(MetaClassRepresentation o1, MetaClassRepresentation o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Map<String, Object> values = new HashMap<>();
        values.put("knownEntities", classes);

        String[] availableTypes = getAvailableBasicTypes();
        values.put("availableTypes", availableTypes);

        String template = resources.getResourceAsString("/com/haulmont/cuba/core/app/domain/DomainDescription.ftl");
        return TemplateHelper.processTemplate(template, values);
    }

    private boolean readPermitted(MetaClass metaClass) {
        UserSession session = userSessionSource.getUserSession();
        return session.isEntityOpPermitted(metaClass, EntityOp.READ);
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
