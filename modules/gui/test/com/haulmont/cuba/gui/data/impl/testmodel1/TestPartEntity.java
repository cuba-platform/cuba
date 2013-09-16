/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl.testmodel1;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.apache.commons.lang.ObjectUtils;

import javax.persistence.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Entity(name = "test$PartEntity")
public class TestPartEntity extends BaseUuidEntity {

    @Column(name = "NAME")
    private String partName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DETAIL_ID")
    private TestDetailEntity detail;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        String o = this.partName;
        this.partName = partName;
        if(!ObjectUtils.equals(o, partName))
            propertyChanged("partName", o, partName);
    }

    public TestDetailEntity getDetail() {
        return detail;
    }

    public void setDetail(TestDetailEntity detail) {
        TestDetailEntity o = this.detail;
        this.detail = detail;
        if(!ObjectUtils.equals(o, detail))
            propertyChanged("detail", o, detail);
    }
}
