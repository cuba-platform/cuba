/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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