/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity(name = "sys$AppFolder")
@Table(name = "SYS_APP_FOLDER")
@PrimaryKeyJoinColumn(name = "FOLDER_ID", referencedColumnName = "ID")
@DiscriminatorValue("A")
@SystemLevel
public class AppFolder extends AbstractSearchFolder {

    @Column(name = "VISIBILITY_SCRIPT", length = 200)
    protected String visibilityScript;

    @Column(name = "QUANTITY_SCRIPT", length = 200)
    protected String quantityScript;

    @Transient
    protected Integer quantity;

    @Override
    public void copyFrom(AbstractSearchFolder srcFolder) {
        super.copyFrom(srcFolder);

        setVisibilityScript(((AppFolder) srcFolder).getVisibilityScript());
        setQuantityScript(((AppFolder) srcFolder).getQuantityScript());
    }

    public String getVisibilityScript() {
        return visibilityScript;
    }

    public void setVisibilityScript(String visibilityScript) {
        this.visibilityScript = visibilityScript;
    }

    public String getQuantityScript() {
        return quantityScript;
    }

    public void setQuantityScript(String quantityScript) {
        this.quantityScript = quantityScript;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getCaption() {
        String s = getLocName();
        if (quantity == null) {
            return s;
        } else {
            return s + " (" + quantity + ")";
        }
    }

    @Override
    public String toString() {
        return getName() + " (" + quantity + ")";
    }
}