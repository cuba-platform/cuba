package com.haulmont.cuba.testmodel.namepattern;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Rushan Zagidullin
 * @since 17.07.2018
 */
@NamePattern("#formatName|name")
@Table(name = "TEST_METHOD_NAME_PATTERN_ENTITY")
@Entity(name = "test$MethodNamePatternEntity")
public class MethodNamePatternEntity extends StandardEntity {
    private static final long serialVersionUID = -833743669993760614L;

    @Column(name = "NAME")
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String formatName() {
        return getName();
    }
}