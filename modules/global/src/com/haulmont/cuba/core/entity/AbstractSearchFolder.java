/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2009 15:40:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractSearchFolder extends Folder {

    private static final long serialVersionUID = -2234453892776090930L;

    @Column(name = "FILTER_COMPONENT")
    protected String filterComponentId;

    @Column(name = "FILTER_XML")
    protected String filterXml;

    @Column(name="APPLY_DEFAULT")
    protected Boolean applyDefault;

    public String getFilterComponentId() {
        return filterComponentId;
    }

    public void setFilterComponentId(String filterComponentId) {
        this.filterComponentId = filterComponentId;
    }

    public String getFilterXml() {
        return filterXml;
    }

    public void setFilterXml(String filterXml) {
        this.filterXml = filterXml;
    }

    public Boolean getApplyDefault(){
        return applyDefault;
    }

    public void setApplyDefault(Boolean applyDefault){
        this.applyDefault=applyDefault;
    }
}
