/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.Session;

import java.util.List;

/**
 * @author abramov
 * @version $Id: MetadataBuildSupport.java 12898 2013-09-16 10:23:29Z krivopustov $
 */
public interface MetadataLoader {
    
    void loadModel(String modelName, List<String> classNames);

    Session postProcess();

    Session getSession();
}