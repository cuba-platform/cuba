/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl.testmodel1;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.apache.commons.lang.ObjectUtils;

import javax.persistence.*;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Entity(name = "test$MasterEntity")
public class TestMasterEntity extends BaseUuidEntity {

    @Column(name = "NAME")
    private String masterName;

    @OneToMany(mappedBy = "master")
    @Composition
    private Set<TestDetailEntity> details;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "master")
    private TestDetailEntity detail;

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        String o = this.masterName;
        this.masterName = masterName;
        if(!ObjectUtils.equals(o, masterName))
            propertyChanged("masterName", o, masterName);
    }

    public Set<TestDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(Set<TestDetailEntity> details) {
        Set<TestDetailEntity> o = this.details;
        this.details = details;
        if(!ObjectUtils.equals(o, details))
            propertyChanged("details", o, details);
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
