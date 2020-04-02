package com.haulmont.cuba.testmodel.sales_1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@Entity(name = "sales1$OrderLineB")
public class OrderLineB extends OrderLine {

    @Column(name = "PARAM2")
    protected String param2;

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
