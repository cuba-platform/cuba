/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes;

import java.util.List;

/**
 *
 * @param <T>
 * @author krivopustov
 * @version $Id$
 */
public interface Enumeration<T extends Enum> extends Datatype<T>{
    List<Enum> getValues();
}