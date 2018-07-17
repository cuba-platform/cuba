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
@NamePattern("%s|name")
@Table(name = "TEST_SIMPLE_NAME_PATTERN_ENTITY")
@Entity(name = "test$SimpleNamePatternEntity")
public class SimpleNamePatternEntity extends StandardEntity {
    private static final long serialVersionUID = -833743669993760614L;

    @Column(name = "NAME")
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}