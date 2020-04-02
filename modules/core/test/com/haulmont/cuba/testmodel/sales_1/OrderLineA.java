package com.haulmont.cuba.testmodel.sales_1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@Entity(name = "sales1$OrderLineA")
public class OrderLineA extends OrderLine {

    @Column(name = "PARAM1")
    protected String param1;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}