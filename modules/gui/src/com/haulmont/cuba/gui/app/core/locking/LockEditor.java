/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.LookupField;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hasanov
 * @version $Id$
 */
public class LockEditor extends AbstractEditor {

    @Inject
    protected Metadata metadata;

    @Named("nameLookupField")
    protected LookupField lookupField;

    public void init(Map<String, Object> params){
        Map<String, Object> options = new TreeMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                String originalName = originalMetaClass == null ? metaClass.getName() : originalMetaClass.getName();
                options.put(metaClass.getName(), originalName);
            }
        }
        lookupField.setOptionsMap(options);
    }
}
