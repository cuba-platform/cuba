/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 11:59:26
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.chile.core.annotations.Aggregation;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import java.util.List;

@Entity(name = "report$BandDefinition")
@Table(name = "REPORT_BAND_DEFINITION")
@NamePattern("%s|name")
@SystemLevel
public class BandDefinition extends HardDeleteEntity {
    private static final long serialVersionUID = 8658220979738705511L;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PARENT_DEFINITION_ID")
    private BandDefinition parentBandDefinition;

    @ManyToOne
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @OneToMany(mappedBy = "parentBandDefinition", cascade = CascadeType.ALL)
    @Aggregation
    @OrderBy("position")
    @OnDelete(value = DeletePolicy.CASCADE)
    private List<BandDefinition> childrenBandDefinitions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bandDefinition", cascade = CascadeType.ALL)
    @Aggregation
    @OnDelete(value = DeletePolicy.CASCADE)
    private List<DataSet> dataSets;

    @Column(name = "ORIENTATION")
    private Integer orientation;

    @Column(name = "POSITION_")
    private Integer position;

    public BandDefinition getParentBandDefinition() {
        return parentBandDefinition;
    }

    public void setParentBandDefinition(BandDefinition parentBandDefinition) {
        this.parentBandDefinition = parentBandDefinition;
    }

    public List<BandDefinition> getChildrenBandDefinitions() {
        return childrenBandDefinitions;
    }

    public void setChildrenBandDefinitions(List<BandDefinition> childrenBandDefinitions) {
        this.childrenBandDefinitions = childrenBandDefinitions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataSet> getDataSets() {
        return dataSets;
    }

    public void setDataSets(List<DataSet> dataSets) {
        this.dataSets = dataSets;
    }

    public Orientation getOrientation() {
        return orientation != null ? Orientation.fromId(orientation) : null;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation != null ? orientation.getId() : null;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position != null && position > 0 ? position : 0;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
