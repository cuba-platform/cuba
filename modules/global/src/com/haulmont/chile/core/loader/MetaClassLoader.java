/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.loader;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface MetaClassLoader {

    void loadPackage(String packageName, List<String> classNames);
}
