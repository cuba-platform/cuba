package com.haulmont.cuba.jpql.global;

import java.io.Serializable;

/**
 * Author: Alexander Chevelev
 * Date: 05.11.2010
 * Time: 0:13:47
 */
public class HintRecord implements Serializable {
    public String title;
    public String inlinedValue;

    public HintRecord() {
    }

    public HintRecord(String title, String inlinedValue) {
        this.title = title;
        this.inlinedValue = inlinedValue;
    }
}
