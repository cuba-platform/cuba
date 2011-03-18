package com.haulmont.cuba.jpql.impl.pointer;

import com.haulmont.cuba.jpql.impl.DomainModel;

/**
 * Author: Alexander Chevelev
 * Date: 21.10.2010
 * Time: 1:47:08
 */
public class NoPointer implements Pointer {
    private static final NoPointer instance = new NoPointer();

    //todo ������� �������� ���������� �� ������
    private NoPointer() {
    }

    public static Pointer instance() {
        return instance;
    }

    public Pointer next(DomainModel model, String field) {
        return this;
    }

}
