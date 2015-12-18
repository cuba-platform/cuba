/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.importexport;

import java.io.Serializable;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class EntityImportViewProperty implements Serializable {

    protected String name;

    protected EntityImportView view;

    protected ReferenceImportBehaviour referenceImportBehaviour;

    public EntityImportViewProperty(String name) {
        this.name = name;
    }

    public EntityImportViewProperty(String name, EntityImportView view) {
        this.name = name;
        this.view = view;
    }

    public EntityImportViewProperty(String name, ReferenceImportBehaviour referenceImportBehaviour) {
        this.name = name;
        this.referenceImportBehaviour = referenceImportBehaviour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityImportView getView() {
        return view;
    }

    public void setView(EntityImportView view) {
        this.view = view;
    }

    public ReferenceImportBehaviour getReferenceImportBehaviour() {
        return referenceImportBehaviour;
    }

    public void setReferenceImportBehaviour(ReferenceImportBehaviour referenceImportBehaviour) {
        this.referenceImportBehaviour = referenceImportBehaviour;
    }
}
