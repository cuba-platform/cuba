/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2009 15:24:15
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity(name = "core$AppFolder")
@Table(name = "SYS_APP_FOLDER")
@PrimaryKeyJoinColumn(name="FOLDER_ID", referencedColumnName = "ID")
@DiscriminatorValue("A")
public class AppFolder extends AbstractSearchFolder {

    @Column(name = "VISIBILITY_SCRIPT", length = 200)
    private String visibilityScript;

    @Column(name = "QUANTITY_SCRIPT", length = 200)
    private String quantityScript;

    @Transient
    private Integer quantity;

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
        String s = super.getCaption();
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
