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

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;

@Entity(name = "report$DataSet")
@Table(name = "REPORT_DATA_SET")
@SystemLevel
public class DataSet extends HardDeleteEntity {
    private static final long serialVersionUID = -3706206933129963303L;

    public static final String QUERY_PARAMS_POSTFIX = ".params";

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT", length = 20000)
    private String text;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "ENTITY_PARAM_NAME")
    private String entityParamName;

    @Column(name = "LIST_ENTITIES_PARAM_NAME")
    private String listEntitiesParamName;

    @Column(name = "QUERY_PARAM_NAME")
    private String queryParamName;

    @Column(name = "VIEW_PARAM_NAME")
    private String viewParamName;

    @Column(name = "ENTITY_CLASS_PARAM_NAME")
    private String entityClassParamName;

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

    public String getEntityParamName() {
        return entityParamName;
    }

    public void setEntityParamName(String entityParamName) {
        this.entityParamName = entityParamName;
    }

    public String getEntityClassParamName() {
        return entityClassParamName;
    }

    public void setEntityClassParamName(String entityClassParamName) {
        this.entityClassParamName = entityClassParamName;
    }

    public String getListEntitiesParamName() {
        return listEntitiesParamName;
    }

    public void setListEntitiesParamName(String listEntitiesParamName) {
        this.listEntitiesParamName = listEntitiesParamName;
    }

    public String getQueryParamName() {
        return queryParamName;
    }

    public void setQueryParamName(String queryParamName) {
        this.queryParamName = queryParamName;
    }

    public String getViewParamName() {
        return viewParamName;
    }

    public void setViewParamName(String viewParamName) {
        this.viewParamName = viewParamName;
    }

    public BandDefinition getBandDefinition() {
        return bandDefinition;
    }

    public void setBandDefinition(BandDefinition bandDefinition) {
        this.bandDefinition = bandDefinition;
    }
}
