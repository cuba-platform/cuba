/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.LockDescriptor;
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

    @Inject
    protected LookupField nameLookupField;

    public void init(Map<String, Object> params){
        Map<String, Object> options = new TreeMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                String originalName = originalMetaClass == null ? metaClass.getName() : originalMetaClass.getName();
                options.put(metaClass.getName(), originalName);
            }
        }
        nameLookupField.setOptionsMap(options);
        if (((LockDescriptor)params.get("item".toUpperCase())).getName() != null)
            nameLookupField.setEditable(false);
    }
}
