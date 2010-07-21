/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 12.05.2010 9:55:06
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "report$DataSet")
@Table(name = "REPORT_DATA_SET")
public class DataSet extends StandardEntity {
    private static final long serialVersionUID = -3706206933129963303L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT", length = 5000)
    private String text;

    @Column(name = "TYPE")
    private Integer type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BAND_DEFINITION")
    private BandDefinition bandDefinition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DataSetType getType() {
        return type != null ? DataSetType.fromId(type) : null;
    }

    public void setType(DataSetType type) {
        this.type = type != null ? type.getId() : null;
    }

    public BandDefinition getBandDefinition() {
        return bandDefinition;
    }

    public void setBandDefinition(BandDefinition bandDefinition) {
        this.bandDefinition = bandDefinition;
    }
}
