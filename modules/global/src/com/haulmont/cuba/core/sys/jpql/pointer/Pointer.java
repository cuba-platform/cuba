package com.haulmont.cuba.core.sys.jpql.pointer;

import com.haulmont.cuba.core.sys.jpql.DomainModel;

/**
 * Author: Alexander Chevelev
 * Date: 21.10.2010
 * Time: 1:44:24
 */
public interface Pointer {
    Pointer next(DomainModel model, String field);
}
