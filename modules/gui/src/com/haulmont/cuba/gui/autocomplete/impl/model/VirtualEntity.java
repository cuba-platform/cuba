package com.haulmont.cuba.jpql.impl.model;

/**
 * Author: Alexander Chevelev
 * Date: 20.10.2010
 * Time: 22:42:00
 */
public class VirtualEntity extends EntityImpl {
    public static int idx = 0;

    public VirtualEntity() {
        super(VirtualEntity.generateName());
    }

    static synchronized String generateName() {
        return "Virtual#" + idx++;
    }
}
