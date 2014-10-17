/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface NumberIdSource {

    String NAME = "cuba_NumberIdSource";

    Long createLongId(String entityName);

    Integer createIntegerId(String entityName);
}
