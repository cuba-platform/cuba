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
@NamePattern("#{@cuba_NamePatternTestBean.concat('Hello, my name is ', name, '. Sum of numbers = ', @cuba_NamePatternTestBean.sum(number, number2))}|name")
@Table(name = "TEST_SPEL_NAME_PATTERN_ENTITY")
@Entity(name = "test$SpelNamePatternEntity")
public class SpelNamePatternEntity extends StandardEntity {
    private static final long serialVersionUID = -833743669993760614L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "NUMBER")
    protected Integer number;

    @Column(name = "NUMBER_2")
    protected Integer number2;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber2() {
        return number2;
    }

    public void setNumber2(Integer number2) {
        this.number2 = number2;
    }
}