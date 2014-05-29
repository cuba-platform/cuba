/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;

import java.util.List;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public interface RelatedEntitiesAssistant {

    String NAME = "relatedEntitiesAssistant";

    String getRelatedEntitiesFilterXml(MetaClass metaClass, List<UUID> ids, Filter component);
}